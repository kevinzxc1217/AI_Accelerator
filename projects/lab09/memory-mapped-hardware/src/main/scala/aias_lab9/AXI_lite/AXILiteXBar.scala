package aias_lab9.AXILite

import chisel3._
import chisel3.util._

class AXILiteXBar(
    // * arguments of AXI-Lite Crossbar
    val nMasters: Int,
    val mSlaves: Int,
    val addrWidth: Int,
    val dataWidth: Int,
    val addrMap: List[(UInt, UInt)]
) extends Module {
  val io = IO(new Bundle {
    // master and slave interfaces
    val masters = Flipped(Vec(nMasters, new AXILiteMasterIF(addrWidth, dataWidth)))
    val slaves  = Flipped(Vec(mSlaves, new AXILiteSlaveIF(addrWidth, dataWidth)))
  })

  // * read bus/mux channels
  val readBuses = List.fill(nMasters) {
    Module(new AXIReadBus(mSlaves, addrWidth, dataWidth, addrMap))
  }
  val readMuxes = List.fill(mSlaves) {
    Module(new AXISlaveReadMux(nMasters, addrWidth, dataWidth))
  }

  // * write bus/mux channels
  val writeBuses = List.fill(nMasters) {
    Module(new AXIWriteBus(mSlaves, addrWidth, dataWidth, addrMap))
  }

  val writeMuxes = List.fill(mSlaves) {
    Module(new AXISlaveWriteMux(nMasters, addrWidth, dataWidth))
  }

  // wiring between IO input/output and r/w buses
  for (i <- 0 until nMasters) {
    readBuses(i).io.master.readAddr <> io.masters(i).readAddr
    io.masters(i).readData <> readBuses(i).io.master.readData
    writeBuses(i).io.master.writeAddr <> io.masters(i).writeAddr
    writeBuses(i).io.master.writeData <> io.masters(i).writeData
    io.masters(i).writeResp <> writeBuses(i).io.master.writeResp
  }

  // wiring between IO input/output and r/w muxes
  for (i <- 0 until mSlaves) {
    io.slaves(i).readAddr <> readMuxes(i).io.out.readAddr
    readMuxes(i).io.out.readData <> io.slaves(i).readData
    io.slaves(i).writeAddr <> writeMuxes(i).io.out.writeAddr
    io.slaves(i).writeData <> writeMuxes(i).io.out.writeData
    writeMuxes(i).io.out.writeResp <> io.slaves(i).writeResp
  }

  // wiring between read bus and mux
  for (m <- 0 until nMasters; s <- 0 until mSlaves) yield {
    readBuses(m).io.slave(s) <> readMuxes(s).io.ins(m)
  }

  // wiring between write bus and mux
  for (m <- 0 until nMasters; s <- 0 until mSlaves) yield {
    writeBuses(m).io.slave(s) <> writeMuxes(s).io.ins(m)
  }
}
