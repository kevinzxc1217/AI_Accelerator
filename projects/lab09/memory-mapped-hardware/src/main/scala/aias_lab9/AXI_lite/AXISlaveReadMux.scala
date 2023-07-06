package aias_lab9.AXILite

import chisel3._
import chisel3.util._

// signals from readMux to target slave
class readOut(val addrWidth: Int, val dataWidth: Int) extends Bundle {
  val readAddr = Decoupled(new AXILiteAddress(addrWidth))           // output address(from read bus) to slave
  val readData = Flipped(Decoupled(new AXILiteReadData(dataWidth))) // input read data from slave
}
// signals from readBus to readMux
class readIn(val addrWidth: Int, val dataWidth: Int) extends Bundle {
  val readAddr = Flipped(Decoupled(new AXILiteAddress(addrWidth))) // input address from readBus
  val readData = Decoupled(new AXILiteReadData(dataWidth))         // output read data(from slave) to readBus
}

class AXISlaveReadMux(val nMasters: Int, val addrWidth: Int, val dataWidth: Int) extends Module {
  val io = IO(new Bundle {
    val out = new readOut(addrWidth, dataWidth)
    val ins = Vec(nMasters, new readIn(addrWidth, dataWidth))
  })

  // use round-robin arbiter (chisel build-in module)
  val arbiter = Module(new RRArbiter(new AXILiteAddress(addrWidth), nMasters))
  // record which slave port is chosen by arbiter
  val chosen_reg = RegNext(arbiter.io.chosen.asUInt)

  /*
    inputs of arbiter here is write addresses
    not whole write data and addr channels
   */

  // wiring arbiter <---> read addresses from readBus
  for (i <- 0 until nMasters) {
    arbiter.io.in(i) <> io.ins(i).readAddr
  }

  // wiring selected master readAddr <---> io.out.readAddr
  io.out.readAddr <> arbiter.io.out

  // signal from readMux to readBus initialization
  for (i <- 0 until nMasters) {
    io.ins(i).readData.bits.data := io.out.readData.bits.data
    io.ins(i).readData.valid     := false.B
    io.ins(i).readData.bits.resp := 0.U
  }

  /*
    signal direction diagram
      slave read data -----> io.out.readData
      io.out.readData -----> io.ins(chosen_reg).readData
      io.ins(chosen_reg).readData -----> master
   */

  io.ins(chosen_reg).readData <> io.out.readData
}
