package aias_lab9.SystolicArray

import chisel3._
import chisel3.iotesters.{Driver, PeekPokeTester}

class PETest(dut: PE) extends PeekPokeTester(dut) {
  val vec    = Array(1, 2, 3, 4) // Input vector
  val scalar = 3                 // weight
  val answer = for (element <- vec) yield (element * scalar)

  // preload the weight
  poke(dut.io.preload, true)
  poke(dut.io.weight.bits, scalar)
  step(1)
  poke(dut.io.preload, false)
  poke(dut.io.weight.bits, 0)

  for ((v, a) <- (vec zip answer)) {
    poke(dut.io.input.valid, true)
    poke(dut.io.input.bits, v)
    step(1)
    expect(dut.io.fwd_ps.valid, true)
    expect(dut.io.fwd_ps.bits, a)
    println("the output port get: " + peek(dut.io.fwd_ps).toString)
  }
  step(1)
}

object PETest extends App {
  Driver.execute(args, () => new PE) { c: PE =>
    new PETest(c)
  }
}
