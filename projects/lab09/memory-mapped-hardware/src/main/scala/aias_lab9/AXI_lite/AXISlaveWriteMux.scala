package aias_lab9.AXILite

import chisel3._
import chisel3.util._

// write mux signals from writeMux to target slave
class writeOut(val addrWidth: Int, val dataWidth: Int) extends Bundle {
  val writeResp = Flipped(Decoupled(UInt(2.W)))              // response from slave to check write status
  val writeAddr = Decoupled(new AXILiteAddress(addrWidth))   // output address to slave for writing data
  val writeData = Decoupled(new AXILiteWriteData(dataWidth)) // output data to write into slave
}
// write mux signals from writeBus to writeMux
class writeIns(val addrWidth: Int, val dataWidth: Int) extends Bundle {
  val writeAddr = Flipped(Decoupled(new AXILiteAddress(addrWidth)))   // input address from writeBus
  val writeData = Flipped(Decoupled(new AXILiteWriteData(dataWidth))) // input write data from  writeBus
  val writeResp = Decoupled(UInt(2.W)) // output write response (response from slave) to writeBus
}

class AXISlaveWriteMux(val nMasters: Int, val addrWidth: Int, val dataWidth: Int) extends Module {
  val io = IO(new Bundle {
    val out = new writeOut(addrWidth, dataWidth)                // from writeMux to slave
    val ins = Vec(nMasters, new writeIns(addrWidth, dataWidth)) // from writeBus to writeMux
  })

  // use round-robin arbiter (build-in chisel module)
  val arbiter = Module(new RRArbiter(new AXILiteAddress(addrWidth), nMasters))
  // record which slave port is chosen by arbiter
  val chosen_reg = RegNext(arbiter.io.chosen.asUInt)

  /*
    inputs of arbiter here is write addresses
    not whole write data and addr channels
   */

  // wiring arbiter <---> io.ins(i).writeAddr
  for (i <- 0 until nMasters) {
    arbiter.io.in(i) <> io.ins(i).writeAddr
  }

  // wiring selected signal <---> io.out.writeAddr
  io.out.writeAddr <> arbiter.io.out

  // signal from writeMux to writeBus initialization
  for (i <- 0 until nMasters) {
    io.ins(i).writeData.ready := false.B
    io.ins(i).writeResp.valid := false.B
    io.ins(i).writeResp.bits  := 0.U
  }

  // arbiter.io.chosenasUInt -> which master is chosen
  io.out.writeData <> io.ins(chosen_reg).writeData
  io.ins(chosen_reg).writeResp <> io.out.writeResp
}
