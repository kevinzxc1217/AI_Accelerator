package aias_lab9.SystolicArray

import chisel3._
import chisel3.util._

import aias_lab9.AXILite._

/** Memory_Mapped module
  *
  * includes LocalMem, MMIO_Regfile and control logics of whole Memory_Mapped
  *
  * @param mem_size
  *   the size of LocalMem
  * @param reg_width
  *   the data size of mmio regs
  */
class Memory_Mapped(mem_size: Int, addr_width: Int, data_width: Int, reg_width: Int) extends Module {
  val io = IO(new Bundle {
    // for CPU to access the Reg and Memory
    val slave = new AXILiteSlaveIF(addr_width, data_width)

    /*
      mmio source: SA
      mmio destination: MMIO_Regfile
     */
    val mmio = new MMIO(reg_width)

    // for SA to read/write LocalMem when it still a slave
    //val raddr = Input(UInt(addr_width.W))
    //val rdata = Output(UInt(data_width.W))
    //val wen   = Input(Bool())
    //val waddr = Input(UInt(addr_width.W))
    //val wdata = Input(UInt(data_width.W))
    //val wstrb = Input(UInt((data_width >> 3).W))
  })

  // module declaration
  val rf = Module(new MMIO_Regfile(addr_width, reg_width))
  //val lm = Module(new LocalMem(mem_size, addr_width, data_width))

  // memory offset of internal memory & MMIO reg in memory space
  val ACCEL_REG_BASE_ADDR = 0x100000
  val ACCEL_MEM_BASE_ADDR = 0x200000
  // byte to bits
  val byte = 8

  // slave port default value
  // WriteData channel
  io.slave.writeData.ready := false.B
  // WriteAddr channel
  io.slave.writeAddr.ready := false.B
  // ReadData channel
  io.slave.readData.bits.data := 0.U
  io.slave.readData.valid     := false.B
  io.slave.readData.bits.resp := false.B
  // ReadAddr channel
  io.slave.readAddr.ready := false.B
  // WriteResp channel
  io.slave.writeResp.bits  := false.B
  io.slave.writeResp.valid := false.B

  // rf wiring and default value
  rf.io.mmio <> io.mmio
  rf.io.raddr := 0.U
  rf.io.waddr := 0.U
  rf.io.wdata := 0.U
  rf.io.wen   := false.B

  // lm wiring and default value
  //lm.io.raddr := 0.U
  //lm.io.waddr := 0.U
  //lm.io.wdata := 0.U
  //lm.io.wstrb := 0.U
  //lm.io.wen   := false.B

  // r/w port default value
  //io.rdata := 0.U

  // the registers used for CPU dominated
  val RAReg      = RegInit(0.U(addr_width.W)) // readAddr.bits.addr reg
  val RAReadyReg = RegInit(false.B)           // readAddr.ready reg
  val RDReg      = RegInit(0.U(data_width.W)) // readData.bits.data reg
  val RRReg      = RegInit(false.B)           // readData.bits.resp reg
  val RDValidReg = RegInit(false.B)           // readData.bits.valid reg

  // canDoRead -> master has sent request while slave has not been ready
  // DoRead -> complete handshaking -> do read
  val canDoRead = WireDefault(io.slave.readAddr.valid && !RAReadyReg)
  // io.slave.readAddr.valid && io.slave.readAddr.ready -> handshaking
  val DoRead = RegNext(io.slave.readAddr.valid && io.slave.readAddr.ready && !RDValidReg)

  //// seems weird because read behavior of reg and SyncReadMem through AXI are different...

  val WAReg      = RegInit(0.U(addr_width.W))       // writeAddr.bits.addr reg
  val WAReadyReg = RegInit(false.B)                 // writeAddr.ready reg
  val WDReg      = RegInit(0.U(data_width.W))       // writeData.bits.data reg
  val WSReg      = RegInit(0.U((data_width / 8).W)) // writeData.bits.strb reg
  val WDReadyReg = RegInit(false.B)                 // writeData.ready reg
  val WRValidReg = RegInit(false.B)                 // writeResp.valid reg

  // canDoWrite -> master has sent request to write while slave has not been ready
  // DoWrite -> complete handshaking -> do write
  val canDoWrite = WireDefault(
    (io.slave.writeAddr.valid && !WAReadyReg) &&
      (io.slave.writeData.valid && !WDReadyReg)
  )
  // io.slave.writeAddr.valid && io.slave.writeAddr.ready -> handshaking of write addr channel
  // io.slave.writeData.valid && io.slave.writeData.ready -> handshaking of write data channel
  val DoWrite = WireDefault(
    (io.slave.writeAddr.valid && io.slave.writeAddr.ready) &&
      (io.slave.writeData.valid && io.slave.writeData.ready)
  )

  // * read/write will be divided into two parts -> CPU or SA dominated
  // * CPU dominated -> when io.mmio.ENABLE_OUT := false.B -> SA is idle now
  when(!io.mmio.ENABLE_OUT) {
    // * read behavior
    RAReadyReg              := canDoRead
    io.slave.readAddr.ready := RAReadyReg
    RAReg                   := Mux(canDoRead, io.slave.readAddr.bits.addr, RAReg)

    // if ACCEL_MEM_BASE_ADDR <= RAReg < ACCEL_MEM_BASE_ADDR -> CPU tend to read MMIO_Regfile
    rf.io.raddr := Mux(
      (ACCEL_REG_BASE_ADDR.U <= RAReg && RAReg < ACCEL_MEM_BASE_ADDR.U),
      (RAReg - ACCEL_REG_BASE_ADDR.U) >> 2, // divided by 4 because of addr format of RegFile
      0.U
    )
    // if ACCEL_MEM_BASE_ADDR <= RAReg -> CPU tend to read LocalMem
    //lm.io.raddr := Mux((ACCEL_MEM_BASE_ADDR.U <= RAReg), (RAReg - ACCEL_MEM_BASE_ADDR.U), 0.U)

    // when DoRead === true.B -> RDValidReg and RRReg are both HIGH -> complete read request
    RDValidReg := DoRead
    RRReg      := DoRead

    // wiring between regs and output signals of slave port
    io.slave.readData.valid     := RDValidReg
    io.slave.readData.bits.data := RDReg
    io.slave.readData.bits.resp := RRReg

    when(RAReg < ACCEL_MEM_BASE_ADDR.U) {
      RDReg := Mux(DoRead, Cat(0.U(32.W), rf.io.rdata), 0.U)
    }.otherwise {
      //RDReg := Mux(DoRead, lm.io.rdata, 0.U)
	  RDReg := Mux(DoRead, Cat(0.U(32.W), rf.io.rdata), 0.U)
    }

    // * write behavior
    WAReadyReg := canDoWrite
    WDReadyReg := canDoWrite

    io.slave.writeAddr.ready := WAReadyReg
    io.slave.writeData.ready := WDReadyReg

    WAReg := Mux(canDoWrite, io.slave.writeAddr.bits.addr, 0.U)
    WDReg := Mux(canDoWrite, io.slave.writeData.bits.data, 0.U)
    WSReg := Mux(canDoWrite, io.slave.writeData.bits.strb, 0.U)

    when(DoWrite) {
      rf.io.waddr := WAReg >> 2
      //lm.io.waddr := WAReg - ACCEL_MEM_BASE_ADDR.U

      rf.io.wdata := WDReg(31, 0)
      //lm.io.wdata := WDReg

      //lm.io.wstrb := WSReg

      rf.io.wen := Mux(io.slave.writeAddr.bits.addr < ACCEL_MEM_BASE_ADDR.U, true.B, false.B)
      //lm.io.wen := Mux(io.slave.writeAddr.bits.addr < ACCEL_MEM_BASE_ADDR.U, false.B, true.B)
    }

    WRValidReg               := DoWrite && !WRValidReg
    io.slave.writeResp.valid := WRValidReg
  }.otherwise {
    // * SA dominated -> when io.mmio.ENABLE_OUT === false.B
    // reset all registers for CPU dominated
    RAReg      := 0.U
    RAReadyReg := false.B
    RDReg      := 0.U
    RRReg      := false.B
    RDValidReg := false.B
    WAReg      := 0.U
    WAReadyReg := false.B
    WDReg      := 0.U
    WDReadyReg := false.B
    WRValidReg := false.B

    // read value from localMem
    //lm.io.raddr := io.raddr // read addr from SA
    //io.rdata    := lm.io.rdata

    // write value to localMem
    //lm.io.waddr := io.waddr // write addr from SA
    //lm.io.wdata := io.wdata // write data from SA
    //lm.io.wstrb := io.wstrb // write strb from SA
    //lm.io.wen   := io.wen   // write enable from SA

	


    // write status and enable to Regfile (cross MMIO interface from SA to MMIO_Regfile)
    rf.io.mmio.WEN       := io.mmio.WEN
    rf.io.mmio.ENABLE_IN := io.mmio.ENABLE_IN
    rf.io.mmio.STATUS_IN := io.mmio.STATUS_IN
	
	
	

  }
}
