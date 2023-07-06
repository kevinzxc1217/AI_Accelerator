package aias_lab9.AXILite

import chisel3._
import chisel3.util._

// ! the required signals on an AXI4-Lite interface
// * 0. Global channel
// ---> ACLK
// ---> ARESETN

// * 1. Write address channel
// --> AWVALID
// --> AWREADY
// --> AWADDR

// * 2. Write data channel
// --> WVALID
// --> WREADY
// --> WDATA
// --> WSTRB

// * 3. Write response channel
// --> BVALID
// --> BREADY
// --> BRESP

// * 4. Read address channel
// --> ARVALID
// --> ARREADY
// --> ARADDR

// * 5. Read data channel
// --> RVALID
// --> RREADY
// --> RDATA
// --> RRESP

// ! Ignore "override def clone...", the statement is irrelevant to this homework
// ! this syntax is due to some problem about nested class

// * Read/Write Address Channel (without ready/valid)
class AXILiteAddress(val addrWidth: Int) extends Bundle {
  val addr = UInt(addrWidth.W) // read/write address

  override def clone = { new AXILiteAddress(addrWidth).asInstanceOf[this.type] }
}

// * Write Data Channel Def (without ready/valid)
class AXILiteWriteData(val dataWidth: Int) extends Bundle {
  val data = UInt(dataWidth.W)       // write data
  val strb = UInt((dataWidth / 8).W) // byte masked

  override def clone = {
    new AXILiteWriteData(dataWidth).asInstanceOf[this.type]
  }
}

// * Read Data channel Def (without ready/valid)
class AXILiteReadData(val dataWidth: Int) extends Bundle {
  val data = UInt(dataWidth.W) // read data from slave to master
  val resp = UInt(2.W)         // read response (2 bits)

  override def clone = {
    new AXILiteReadData(dataWidth).asInstanceOf[this.type]
  }
}

// * Slave Interface Def
class AXILiteSlaveIF(val addrWidth: Int, val dataWidth: Int) extends Bundle {
  // write address channel
  val writeAddr = Flipped(Decoupled(new AXILiteAddress(addrWidth))) // write addr (from master to slave)
  // write data channel
  val writeData = Flipped(Decoupled(new AXILiteWriteData(dataWidth))) // write data (from mater to slave)
  // write response channel
  val writeResp = Decoupled(UInt(2.W)) // write response (from slave to master)
  // read address channel
  val readAddr = Flipped(Decoupled(new AXILiteAddress(addrWidth))) // read address (from master to slave)
  // read data channel
  val readData = Decoupled(new AXILiteReadData(dataWidth)) // read data (from slave to master)

  override def clone = {
    new AXILiteSlaveIF(addrWidth, dataWidth).asInstanceOf[this.type]
  }
}

// * Master Interface Def
class AXILiteMasterIF(val addrWidth: Int, val dataWidth: Int) extends Bundle {
  // write address channel
  val writeAddr = Decoupled(new AXILiteAddress(addrWidth))
  // write data channel
  val writeData = Decoupled(new AXILiteWriteData(dataWidth))
  // write response channel
  val writeResp = Flipped(Decoupled(UInt(2.W)))
  // read address channel
  val readAddr = Decoupled(new AXILiteAddress(addrWidth))
  // read data channel
  val readData = Flipped(Decoupled(new AXILiteReadData(dataWidth)))

  override def clone: AXILiteMasterIF = {
    new AXILiteMasterIF(addrWidth, dataWidth).asInstanceOf[this.type]
  }
}

class Interface extends MultiIOModule {
  /*
   * this Interface class is to generate example verilog code
   * in your homework, you can ignore this class
   */

  val io = IO(new Bundle {
    val slave  = new AXILiteSlaveIF(8, 32)
    val master = new AXILiteMasterIF(8, 32)
  })

  // input signal default values
  io.slave.writeData.ready := false.B
  io.slave.writeAddr.ready := false.B
  io.slave.readAddr.ready  := false.B

  io.slave.writeResp.bits  := 0.U
  io.slave.writeResp.valid := false.B

  io.slave.readData.bits.data := 0.U
  // io.slave.readData.bits.resp := 0.U
  io.slave.readData.valid := false.B

  io.master.writeAddr.bits.addr := 0.U
  io.master.readData.ready      := false.B
  io.master.writeData.valid     := false.B
  io.master.writeAddr.valid     := false.B
  io.master.readAddr.bits.addr  := 0.U
  io.master.writeResp.ready     := false.B
  io.master.writeData.bits.data := 0.U
  io.master.readAddr.valid      := false.B
}

object Interface extends App {
  (new stage.ChiselStage).emitVerilog(
    new Interface(),
    Array("-td", "./generated/Interface")
  )
}
