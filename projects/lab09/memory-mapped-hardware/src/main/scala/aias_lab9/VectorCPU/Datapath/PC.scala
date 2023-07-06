package aias_lab9.VectorCPU.Datapath

import chisel3._
import chisel3.util._

class PC extends Module {
  val io = IO(new Bundle {
    val Hcf     = Input(Bool())
    val PCSel   = Input(UInt(2.W))
    val alu_out = Input(UInt(32.W))
    val pc      = Output(UInt(15.W))
  })

  val pcReg = RegInit(0.U(32.W))

  when(!io.Hcf) {
    pcReg := MuxLookup(
      io.PCSel,
      pcReg + 4.U,
      Seq(
        2.U -> pcReg,
        1.U -> (io.alu_out & ~((3.U)(32.W))),
        0.U -> (pcReg + 4.U)
      )
    )
  }.otherwise {
    pcReg := pcReg
  }

  io.pc := pcReg
}
