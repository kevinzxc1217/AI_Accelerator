package aias_lab5.Hw4

import chisel3._
import chisel3.util.experimental.loadMemoryFromFile
import firrtl.annotations.MemoryLoadFileType

class InstMem extends Module {
  val io = IO(new Bundle {
    val raddr = Input(UInt(7.W))
    val rdata = Output(UInt(32.W))
  })
  val memory = Mem(32, UInt(32.W))
  loadMemoryFromFile(memory, "./src/main/resource/InstMem.txt",MemoryLoadFileType.Binary)

  io.rdata := memory((io.raddr>>2))
}