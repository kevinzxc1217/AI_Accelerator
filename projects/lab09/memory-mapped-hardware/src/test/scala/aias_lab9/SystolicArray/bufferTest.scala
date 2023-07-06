package aias_lab9.SystolicArray

import chisel3._
import chisel3.iotesters.{Driver, PeekPokeTester}

class bufferTest(dut: buffer) extends PeekPokeTester(dut) {
  poke(dut.io.input(0).bits, "h01".U)
  poke(dut.io.input(1).bits, "h02".U)
  poke(dut.io.input(2).bits, "h03".U)
  poke(dut.io.input(3).bits, "h04".U)

  poke(dut.io.input(0).valid, true.B)
  poke(dut.io.input(1).valid, true.B)
  poke(dut.io.input(2).valid, true.B)
  poke(dut.io.input(3).valid, true.B)

  expect(dut.io.output(0).bits, "h01".U)
  expect(dut.io.output(0).valid, true.B)
  step(1)
  expect(dut.io.output(1).bits, "h02".U)
  expect(dut.io.output(1).valid, true.B)
  step(1)
  expect(dut.io.output(2).bits, "h03".U)
  expect(dut.io.output(2).valid, true.B)
  step(1)
  expect(dut.io.output(3).bits, "h04".U)
  expect(dut.io.output(3).valid, true.B)

  // end simulation
  step(3)
}

object bufferTest extends App {
  Driver.execute(args, () => new buffer(4, 8)) { c: buffer =>
    new bufferTest(c)
  }
}
