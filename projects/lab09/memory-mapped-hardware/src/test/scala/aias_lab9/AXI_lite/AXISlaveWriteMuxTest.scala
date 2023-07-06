package aias_lab9.AXILite

import chisel3.iotesters.{PeekPokeTester, Driver}

class AXISlaveWriteMuxTest(dut: AXISlaveWriteMux) extends PeekPokeTester(dut) {
  step(1)
  poke(dut.io.ins(0).writeResp.ready, true)
  poke(dut.io.ins(1).writeResp.ready, true)
  poke(dut.io.ins(0).writeAddr.valid, true)
  poke(dut.io.ins(1).writeAddr.valid, false)
  poke(dut.io.ins(0).writeAddr.bits.addr, 0x8)
  poke(dut.io.ins(1).writeAddr.bits.addr, 0x10)
  poke(dut.io.ins(0).writeData.valid, true)
  poke(dut.io.ins(1).writeData.valid, false)
  poke(dut.io.ins(0).writeData.bits.data, 10)
  poke(dut.io.ins(0).writeData.bits.strb, 0xff)
  poke(dut.io.ins(1).writeData.bits.data, 20)
  poke(dut.io.ins(1).writeData.bits.strb, 0xff)
  poke(dut.io.out.writeResp.valid, true)

  poke(dut.io.out.writeAddr.ready, true)
  poke(dut.io.out.writeData.ready, true)
  step(1)
  poke(dut.io.ins(0).writeData.valid, false)
  poke(dut.io.ins(0).writeAddr.valid, false)
  poke(dut.io.ins(1).writeAddr.valid, true)
  poke(dut.io.ins(1).writeData.valid, true)
  poke(dut.io.out.writeResp.bits, 2)
  step(1)

}

object AXISlaveWriteMuxTest extends App {
  chisel3.iotesters.Driver.execute(args, () => new AXISlaveWriteMux(2, 32, 64)) { c =>
    new AXISlaveWriteMuxTest(c)
  }
}
