package aias_lab9.AXILite

import chisel3.iotesters.{Driver, PeekPokeTester}

class AXISlaveReadMuxTest(dut: AXISlaveReadMux) extends PeekPokeTester(dut) {
  step(1)
  poke(dut.io.ins(0).readAddr.bits.addr, 0x8)
  poke(dut.io.ins(1).readAddr.bits.addr, 0x16)
  poke(dut.io.ins(0).readAddr.valid, true)
  poke(dut.io.ins(1).readAddr.valid, false)
  poke(dut.io.ins(0).readData.ready, true)
  poke(dut.io.ins(1).readData.ready, true)
  poke(dut.io.out.readAddr.ready, true)
  poke(dut.io.out.readData.bits.data, 0)
  poke(dut.io.out.readData.bits.resp, 0)
  poke(dut.io.out.readData.valid, false)
  step(1)
  poke(dut.io.out.readData.bits.data, 10)
  poke(dut.io.out.readData.valid, true)
  step(1)
  poke(dut.io.ins(0).readAddr.valid, false)
  poke(dut.io.ins(1).readAddr.valid, true)
  poke(dut.io.out.readAddr.ready, true)
  poke(dut.io.out.readData.bits.data, 0)
  poke(dut.io.out.readData.valid, false)
  step(1)
  poke(dut.io.out.readData.bits.data, 20)
  poke(dut.io.out.readData.valid, true)
  step(1)

}

object AXISlaveReadMuxTest extends App {
  Driver.execute(args, () => new AXISlaveReadMux(2, 32, 64)) { c =>
    new AXISlaveReadMuxTest(c)
  }
}
