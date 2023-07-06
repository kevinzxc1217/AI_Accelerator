package aias_lab9.AXILite

import chisel3._
import chisel3.iotesters.{Driver, PeekPokeTester}

class AXIWriteBusTest(dut: AXIWriteBus) extends PeekPokeTester(dut) {
  for (i <- 0 until 2) {
    poke(dut.io.master.writeAddr.valid, false)
    poke(dut.io.master.writeAddr.bits.addr, 0)
    poke(dut.io.master.writeData.valid, false)
    poke(dut.io.master.writeData.bits.data, 0)
    poke(dut.io.master.writeData.bits.strb, 0xf)
    poke(dut.io.master.writeResp.ready, false)

    poke(dut.io.slave(i).writeAddr.ready, false)
    poke(dut.io.slave(i).writeData.ready, false)
    poke(dut.io.slave(i).writeResp.valid, false)
    poke(dut.io.slave(i).writeResp.bits, 0)
  }

  poke(dut.io.master.writeAddr.valid, true)
  poke(dut.io.master.writeAddr.bits.addr, 0x9000)
  poke(dut.io.master.writeData.valid, true)
  poke(dut.io.master.writeData.bits.data, 1)
  poke(dut.io.master.writeData.bits.strb, 0xf)
  poke(dut.io.master.writeResp.ready, true)

  poke(dut.io.slave(0).writeAddr.ready, true)
  poke(dut.io.slave(0).writeData.ready, true)
  poke(dut.io.slave(0).writeResp.valid, true)
  poke(dut.io.slave(0).writeResp.bits, 0)

  step(2)

  poke(dut.io.master.writeAddr.valid, true)
  poke(dut.io.master.writeAddr.bits.addr, 0x19000)
  poke(dut.io.master.writeData.valid, true)
  poke(dut.io.master.writeData.bits.data, 1)
  poke(dut.io.master.writeData.bits.strb, 0xf)
  poke(dut.io.master.writeResp.ready, true)

  poke(dut.io.slave(1).writeAddr.ready, true)
  poke(dut.io.slave(1).writeData.ready, true)
  poke(dut.io.slave(1).writeResp.valid, true)
  poke(dut.io.slave(1).writeResp.bits, 0)

  poke(dut.io.slave(0).writeAddr.ready, false)
  poke(dut.io.slave(0).writeData.ready, false)
  poke(dut.io.slave(0).writeResp.valid, false)
  poke(dut.io.slave(0).writeResp.bits, 0)

  step(2)

}

object AXIWriteBusTest extends App {
  val addr_map = List(("h8000".U, "h10000".U), ("h10000".U, "h20000".U))

  Driver.execute(args, () => new AXIWriteBus(2, 32, 64, addr_map)) { c =>
    new AXIWriteBusTest(c)
  }
}
