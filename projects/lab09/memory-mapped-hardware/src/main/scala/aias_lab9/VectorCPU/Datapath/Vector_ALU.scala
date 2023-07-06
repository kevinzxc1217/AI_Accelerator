package aias_lab9.VectorCPU.Datapath

import chisel3._
import chisel3.util._

import aias_lab9.VectorCPU.vector_alu_op_map._

class VECTOR_ALUIO extends Bundle {
  val vector_src0   = Input(UInt(64.W))
  val vector_src1   = Input(UInt(64.W))
  val vector_src2   = Input(UInt(64.W))
  val vector_ALUSel = Input(UInt(4.W))
  val vector_out    = Output(UInt(64.W))
}

class Vector_ALU extends Module {
  val io = IO(new VECTOR_ALUIO)

  val wire_set = Wire(Vec(8, UInt(8.W)))

  // Default Value of wire_set
  wire_set.foreach { wire =>
    wire := 0.U
  }

  switch(io.vector_ALUSel) {
    is(VADD_VV) {
      for (i <- 0 until 8) {
        wire_set(i) := io.vector_src1(8 * (i + 1) - 1, 8 * i) + io.vector_src2(8 * (i + 1) - 1, 8 * i)
      }
    }
    is(VMACC_VV) {
      for (i <- 0 until 8) {
        wire_set(i) := ((io.vector_src1(8 * (i + 1) - 1, 8 * i) * io.vector_src2(8 * (i + 1) - 1, 8 * i)) + io
          .vector_src0(8 * (i + 1) - 1, 8 * i))
      }
    }
  }

  io.vector_out := wire_set.asUInt
}
