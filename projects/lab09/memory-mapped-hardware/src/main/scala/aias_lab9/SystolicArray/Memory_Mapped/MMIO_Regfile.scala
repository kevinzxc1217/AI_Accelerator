package aias_lab9.SystolicArray

import chisel3._
import chisel3.util._

import aias_lab9.AXILite._

/** MMIO_regfile module
  *
  * has totally 10 registers inside
  *
  * @param reg_width
  *   bit width of data in each registers
  */
// declare MMIO interface between MMIO_Regfiel and SA
class MMIO(val reg_width: Int) extends Bundle {
  val ENABLE_OUT     = Output(Bool())
  val STATUS_OUT     = Output(Bool())
  val MATA_SIZE      = Output(UInt(reg_width.W))
  val MATB_SIZE      = Output(UInt(reg_width.W))
  val MATC_SIZE      = Output(UInt(reg_width.W))
  val MATA_MEM_ADDR  = Output(UInt(reg_width.W))
  val MATB_MEM_ADDR  = Output(UInt(reg_width.W))
  val MATC_MEM_ADDR  = Output(UInt(reg_width.W))
  val MAT_MEM_STRIDE = Output(UInt(reg_width.W))
  val MAT_BUF        = Output(UInt(reg_width.W))

  val WEN       = Input(Bool())
  val ENABLE_IN = Input(Bool())
  val ENABLE    = Input(Bool())
  val STATUS_IN = Input(Bool())

  override def clone = { new MMIO(reg_width).asInstanceOf[this.type] }
}

class MMIO_Regfile(addr_width: Int, reg_width: Int) extends Module {
  val io = IO(new Bundle {
    // for SystolicArray MMIO
    val mmio = new MMIO(reg_width)

    // for Memory Mapped to r/w reg value
    val raddr = Input(UInt(addr_width.W))
    val rdata = Output(UInt(reg_width.W))

    val wen   = Input(Bool())
    val waddr = Input(UInt(addr_width.W))
    val wdata = Input(UInt(reg_width.W))
  })
  

  // this initial_table is used for lab testing
  // it may become useless when doing your homework
  val initial_table = Seq(
    0.U(reg_width.W),           // ENABLE
    0.U(reg_width.W),           // STATUS
    "h00030003".U(reg_width.W), // MATA_SIZE
    "h00030003".U(reg_width.W), // MATB_SIZE
    "h00030003".U(reg_width.W), // MATC_SIZE
    "h8000".U(reg_width.W),           // MATA_MEM_ADDR
    "h8010".U(reg_width.W),          // MATB_MEM_ADDR
	//"h8100".U(reg_width.W),          // MATB_MEM_ADDR
    "h8020".U(reg_width.W),          // MATC_MEM_ADDR
	//"h8200".U(reg_width.W),          // MATC_MEM_ADDR
    "h010101".U(reg_width.W),   // MAT_MEM_STRIDE
    "h8030".U(reg_width.W)           // MAT_BUF
  )

  
  
  
  
  // totally 10 registers
  val RegFile = RegInit(VecInit(initial_table))

  // MMIO circuit declaration
  // Output
  io.mmio.ENABLE_OUT     := RegNext(RegFile(0)(0).asBool)
  io.mmio.STATUS_OUT     := RegNext(RegFile(1)(0).asBool)
  io.mmio.MATA_SIZE      := RegNext(RegFile(2))
  io.mmio.MATB_SIZE      := RegNext(RegFile(3))
  io.mmio.MATC_SIZE      := RegNext(RegFile(4))
  io.mmio.MATA_MEM_ADDR  := RegNext(RegFile(5))
  io.mmio.MATB_MEM_ADDR  := RegNext(RegFile(6))
  io.mmio.MATC_MEM_ADDR  := RegNext(RegFile(7))
  io.mmio.MAT_MEM_STRIDE := RegNext(RegFile(8))
  io.mmio.MAT_BUF        := RegNext(RegFile(9))

  //新增
  when(io.mmio.ENABLE){
	RegFile(0) := 1.U
  }


  // when io.mmio.WEN === true.B -> SA attempt to write MMIO_RegFIle
  when(io.mmio.WEN) {
    RegFile(1) := io.mmio.STATUS_IN.asUInt
	//RegFile(1) := io.mmio.ENABLE_IN2.asUInt
    RegFile(0) := io.mmio.ENABLE_IN.asUInt
  }

  // when io.wen === true.B -> some master send request(except for SA) to r/w MMIO_RegFIle
  // SA will r/w MMIO_Regfile cross MMIO interface, not io.wen
  io.rdata := RegFile(io.raddr)
  when(io.wen) { RegFile(io.waddr) := io.wdata }
}

object MMIO_Regfile extends App {
  (new chisel3.stage.ChiselStage).emitVerilog(
    new MMIO_Regfile(32, 32),
    args
  )
}
