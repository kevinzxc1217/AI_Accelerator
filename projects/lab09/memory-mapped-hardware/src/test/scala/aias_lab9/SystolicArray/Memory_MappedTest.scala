package aias_lab9.SystolicArray

import scala.io.Source
import chisel3.iotesters.{PeekPokeTester, Driver}
import scala.language.implicitConversions

class Memory_MappedTest(dut: Memory_Mapped) extends PeekPokeTester(dut) {
  // Read the second value (STATUS) of localMem
  poke(dut.io.slave.readAddr.bits.addr, 0x100008)
  poke(dut.io.slave.readAddr.valid, true)
  poke(dut.io.slave.readData.ready, true)
  step(2)
  poke(dut.io.slave.readAddr.bits.addr, 0x0)
  poke(dut.io.slave.readAddr.valid, false)
  step(1)
  poke(dut.io.slave.readData.ready, false)
  step(1)

  // Read the second double-word value of localMem
  poke(dut.io.slave.readAddr.bits.addr, 0x200008)
  poke(dut.io.slave.readAddr.valid, true)
  poke(dut.io.slave.readData.ready, true)
  step(2)
  poke(dut.io.slave.readAddr.bits.addr, 0x0)
  poke(dut.io.slave.readAddr.valid, false)
  step(1)
  poke(dut.io.slave.readData.ready, false)
  step(1)

  // Write 1 to the first reg (ENABLE) of Regfile
  poke(dut.io.slave.writeAddr.bits.addr, 0x00000)
  poke(dut.io.slave.writeAddr.valid, true)
  poke(dut.io.slave.writeData.bits.data, 1)
  poke(dut.io.slave.writeData.valid, true)
  step(2)
  poke(dut.io.slave.writeAddr.bits.addr, 0x00000)
  poke(dut.io.slave.writeAddr.valid, false)
  poke(dut.io.slave.writeData.bits.data, 0)
  poke(dut.io.slave.writeData.valid, false)

  poke(dut.io.slave.writeResp.ready, true)
  step(1)
  poke(dut.io.slave.writeResp.ready, false)
  step(1)

  step(10)
}

object Memory_MappedTest extends App {
  Driver.execute(
    Array("-tbn", "verilator"),
    () => new Memory_Mapped(0x8000, 32, 64, 32)
  ) { c: Memory_Mapped =>
    new Memory_MappedTest(c)
  }
}
