package aias_lab9.AXILite

import chisel3._
import chisel3.iotesters.{Driver, PeekPokeTester}

class AXIReadBusTest(dut: AXIReadBus) extends PeekPokeTester(dut) {
  for (i <- 0 until 2) {
    poke(dut.io.master.readAddr.valid, false)
    poke(dut.io.master.readAddr.bits.addr, 1)
    poke(dut.io.master.readData.ready, false)

    poke(dut.io.slave(i).readAddr.ready, false)
    poke(dut.io.slave(i).readData.valid, false)
    poke(dut.io.slave(i).readData.bits.data, 0)
  }

  poke(dut.io.master.readAddr.valid, true)
  poke(dut.io.master.readAddr.bits.addr, 0x9008)
  poke(dut.io.master.readData.ready, true)

  poke(dut.io.slave(0).readAddr.ready, true)
  poke(dut.io.slave(0).readData.valid, true)
  poke(dut.io.slave(0).readData.bits.data, 1)

  step(2)

  poke(dut.io.master.readAddr.valid, true)
  poke(dut.io.master.readAddr.bits.addr, 0x19008)
  poke(dut.io.master.readData.ready, true)

  poke(dut.io.slave(1).readAddr.ready, true)
  poke(dut.io.slave(1).readData.valid, true)
  poke(dut.io.slave(1).readData.bits.data, 2)

  poke(dut.io.slave(0).readAddr.ready, false)
  poke(dut.io.slave(0).readData.valid, false)
  poke(dut.io.slave(0).readData.bits.data, 1)

  step(2)

}

object AXIReadBusTest extends App {
  val addr_map = List(("h8000".U, "h10000".U), ("h10000".U, "h20000".U))

  Driver.execute(args, () => new AXIReadBus(2, 32, 64, addr_map)) { c =>
    new AXIReadBusTest(c)
  }
}
