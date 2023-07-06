package aias_lab9.SystolicArray

import scala.io.Source
import chisel3.iotesters.{PeekPokeTester, Driver}
import scala.language.implicitConversions

class SATest(dut: SA) extends PeekPokeTester(dut) {
  poke(dut.io.mmio.MATA_MEM_ADDR, 0x200000)
  poke(dut.io.mmio.MATB_MEM_ADDR, 0x200010)
  poke(dut.io.mmio.MATC_MEM_ADDR, 0x200020)
  step(5)
  while (peek(dut.io.mmio.STATUS_IN) != 1) {
    poke(dut.io.mmio.ENABLE_OUT, true)
    step(1)
  }
  poke(dut.io.mmio.ENABLE_OUT, false)
  step(5)
}

object SATest extends App {
  Driver.execute(
    Array("-tbn", "verilator"),
    () => new SA(4, 4, 32, 64, 32)
  ) { c: SA =>
    new SATest(c)
  }
}
