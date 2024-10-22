package aias_lab9.AXILite

import chisel3._
import chisel3.iotesters.{Driver, PeekPokeTester}

class AXILiteXBarTest(dut: AXILiteXBar) extends PeekPokeTester(dut) {
  for (i <- 0 until 2) {
    poke(dut.io.masters(i).writeAddr.valid, false)
    poke(dut.io.masters(i).writeAddr.bits.addr, 0)
    poke(dut.io.masters(i).writeData.valid, false)
    poke(dut.io.masters(i).writeData.bits.data, 0)
    poke(dut.io.masters(i).writeData.bits.strb, 0xf)
    poke(dut.io.masters(i).writeResp.ready, false)
    poke(dut.io.masters(i).readAddr.valid, false)
    poke(dut.io.masters(i).readAddr.bits.addr, 1)
    poke(dut.io.masters(i).readData.ready, false)

    poke(dut.io.slaves(i).writeAddr.ready, false)
    poke(dut.io.slaves(i).writeData.ready, false)
    poke(dut.io.slaves(i).readAddr.ready, false)
    poke(dut.io.slaves(i).readData.valid, false)
    poke(dut.io.slaves(i).readData.bits.data, 0)
    poke(dut.io.slaves(i).writeResp.valid, false)
    poke(dut.io.slaves(i).writeResp.bits, 0)
    poke(dut.io.slaves(i).writeData.bits.strb, 0xf)
  }

  poke(dut.io.masters(0).writeAddr.valid, true)
  poke(dut.io.masters(0).writeAddr.bits.addr, 0x9000)
  poke(dut.io.masters(0).writeData.valid, true)
  poke(dut.io.masters(0).writeData.bits.data, 1)
  poke(dut.io.masters(0).writeData.bits.strb, 0xf)
  poke(dut.io.masters(0).writeResp.ready, true)
  poke(dut.io.masters(0).readAddr.valid, true)
  poke(dut.io.masters(0).readAddr.bits.addr, 0x9008)
  poke(dut.io.masters(0).readData.ready, true)

  poke(dut.io.slaves(0).writeAddr.ready, true)
  poke(dut.io.slaves(0).writeData.ready, true)
  poke(dut.io.slaves(0).readAddr.ready, true)
  poke(dut.io.slaves(0).readData.valid, true)
  poke(dut.io.slaves(0).readData.bits.data, 1)
  poke(dut.io.slaves(0).writeResp.valid, true)
  poke(dut.io.slaves(0).writeResp.bits, 0)

  step(2)

  poke(dut.io.masters(0).writeAddr.valid, true)
  poke(dut.io.masters(0).writeAddr.bits.addr, 0x19000)
  poke(dut.io.masters(0).writeData.valid, true)
  poke(dut.io.masters(0).writeData.bits.data, 1)
  poke(dut.io.masters(0).writeData.bits.strb, 0xf)
  poke(dut.io.masters(0).writeResp.ready, true)
  poke(dut.io.masters(0).readAddr.valid, true)
  poke(dut.io.masters(0).readAddr.bits.addr, 0x19008)
  poke(dut.io.masters(0).readData.ready, true)

  poke(dut.io.slaves(1).writeAddr.ready, true)
  poke(dut.io.slaves(1).writeData.ready, true)
  poke(dut.io.slaves(1).readAddr.ready, true)
  poke(dut.io.slaves(1).readData.valid, true)
  poke(dut.io.slaves(1).readData.bits.data, 2)
  poke(dut.io.slaves(1).writeResp.valid, true)
  poke(dut.io.slaves(1).writeResp.bits, 0)

  poke(dut.io.slaves(0).writeAddr.ready, false)
  poke(dut.io.slaves(0).writeData.ready, false)
  poke(dut.io.slaves(0).readAddr.ready, false)
  poke(dut.io.slaves(0).readData.valid, false)
  poke(dut.io.slaves(0).readData.bits.data, 1)
  poke(dut.io.slaves(0).writeResp.valid, false)
  poke(dut.io.slaves(0).writeResp.bits, 0)

  step(2)

  poke(dut.io.masters(1).writeAddr.valid, true)
  poke(dut.io.masters(1).writeAddr.bits.addr, 0x19000)
  poke(dut.io.masters(1).writeData.valid, true)
  poke(dut.io.masters(1).writeData.bits.data, 1)
  poke(dut.io.masters(1).writeData.bits.strb, 0xf)
  poke(dut.io.masters(1).writeResp.ready, true)
  poke(dut.io.masters(1).readAddr.valid, true)
  poke(dut.io.masters(1).readAddr.bits.addr, 0x19008)
  poke(dut.io.masters(1).readData.ready, true)

  poke(dut.io.slaves(1).writeAddr.ready, true)
  poke(dut.io.slaves(1).writeData.ready, true)
  poke(dut.io.slaves(1).readAddr.ready, true)
  poke(dut.io.slaves(1).readData.valid, true)
  poke(dut.io.slaves(1).readData.bits.data, 2)
  poke(dut.io.slaves(1).writeResp.valid, true)
  poke(dut.io.slaves(1).writeResp.bits, 0)

  step(4)

}

object AXILiteXBarTest extends App {
  // allocation of 2 slaves in memory space
  val addr_map = List(("h8000".U, "h10000".U), ("h100000".U, "h2FFFFF".U))

  Driver.execute(args, () => new AXILiteXBar(2, 2, 32, 64, addr_map)) { c =>
    new AXILiteXBarTest(c)
  }
}
