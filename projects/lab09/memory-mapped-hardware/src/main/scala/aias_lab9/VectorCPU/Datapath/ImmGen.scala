package aias_lab9.VectorCPU.Datapath

import chisel3._
import chisel3.util._

import aias_lab9.VectorCPU.inst_type._

class ImmGen extends Module {
  val io = IO(new Bundle {
    val inst_31_7 = Input(UInt(25.W))
    val ImmSel    = Input(UInt(3.W))
    val imm       = Output(UInt(32.W))
  })

  val inst_shift = Wire(UInt(32.W))
  inst_shift := Cat(io.inst_31_7, 0.U(7.W))

  val simm = Wire(SInt(32.W))

  simm := MuxLookup(
    io.ImmSel,
    0.S,
    Seq(
      // R-type
      R_type -> 0.S,

      // I-type
      I_type -> inst_shift(31, 20).asSInt,
      // B-type
      B_type -> Cat(inst_shift(31), inst_shift(7), inst_shift(30, 25), inst_shift(11, 8), 0.U(1.W)).asSInt,

      // S-type
      S_type -> Cat(inst_shift(31), inst_shift(30, 25), inst_shift(11, 8), inst_shift(7)).asSInt,

      // U-type
      U_type -> Cat(inst_shift(31, 12), 0.U(12.W)).asSInt,

      // J-type
      J_type -> Cat(inst_shift(31), inst_shift(19, 12), inst_shift(20), inst_shift(30, 21), 0.U(1.W)).asSInt
    )
  )

  io.imm := simm.asUInt
}
