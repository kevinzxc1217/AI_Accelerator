package aias_lab9.SystolicArray

import scala.io.Source
import chisel3.iotesters.{PeekPokeTester, Driver}
import scala.language.implicitConversions

class MMIO_RegfileTest(dut: MMIO_Regfile) extends PeekPokeTester(dut) {
  step(10)
}

object MMIO_RegfileTest extends App {
  Driver.execute(
    Array("-tbn", "verilator"),
    () => new MMIO_Regfile(32, 64)
  ) { c: MMIO_Regfile =>
    new MMIO_RegfileTest(c)
  }
}
