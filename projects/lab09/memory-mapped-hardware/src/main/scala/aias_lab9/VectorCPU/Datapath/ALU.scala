package aias_lab9.VectorCPU.Datapath

import chisel3._
import chisel3.util._

import aias_lab9.VectorCPU.opcode_map._
import aias_lab9.VectorCPU.alu_op_map._

class ALUIO extends Bundle {
  val src1   = Input(UInt(32.W))
  val src2   = Input(UInt(32.W))
  val ALUSel = Input(UInt(15.W))
  val out    = Output(UInt(32.W))
}

class ALU extends Module {
  val io = IO(new ALUIO)

  val funct7 = Wire(UInt(7.W))
  val mask   = Wire(UInt(5.W))
  val funct3 = Wire(UInt(3.W))
  funct7 := io.ALUSel(14, 8)
  mask   := io.ALUSel(7, 3)
  funct3 := io.ALUSel(2, 0)

  io.out := 0.U
  switch(io.ALUSel) {
    is(ADD) { io.out := io.src1 + io.src2 }
    is(SLL) { io.out := io.src1 << io.src2(4, 0) }
    is(SLT) { io.out := Mux(io.src1.asSInt < io.src2.asSInt, 1.U, 0.U) }
    is(SLTU) { io.out := Mux(io.src1 < io.src2, 1.U, 0.U) }
    is(XOR) { io.out := io.src1 ^ io.src2 }
    is(SRL) { io.out := io.src1 >> io.src2(4, 0) }
    is(OR) { io.out := io.src1 | io.src2 }
    is(AND) { io.out := io.src1 & io.src2 }
    is(SUB) { io.out := io.src1 - io.src2 }
    is(SRA) { io.out := (io.src1.asSInt >> io.src2(4, 0)).asUInt }
    is(MUL) { io.out := io.src1 * io.src2 }

    is(CLZ) {
      when(io.src1(31) === 1.U) { io.out := 0.U }
        .elsewhen(io.src1 === 0.U) { io.out := 32.U }
        .otherwise {
          for (i <- 1 until 32) {
            when(io.src1(31, 31 - i) === Cat(0.U(i.W), 1.U(1.W))) {
              io.out := i.U
            }
          }
        }
    }

    is(CTZ) {
      when(io.src1(0) === 1.U) { io.out := 0.U }
        .elsewhen(io.src1 === 0.U) { io.out := 32.U }
        .otherwise {
          for (i <- 1 until 32) {
            when(io.src1(i, 0) === Cat(1.U(1.W), 0.U(i.W))) {
              io.out := i.U
            }
          }
        }
    }
    is(CPOP) {
      io.out := 0.U(6.W) ## io.src1(0) + 0.U(6.W) ## io.src1(1) + 0.U(6.W) ## io.src1(2) + 0.U(6.W) ## io.src1(3) +
        0.U(6.W) ## io.src1(4) + 0.U(6.W) ## io.src1(5) + 0.U(6.W) ## io.src1(6) + 0.U(6.W) ## io.src1(7) +
        0.U(6.W) ## io.src1(8) + 0.U(6.W) ## io.src1(9) + 0.U(6.W) ## io.src1(10) + 0.U(6.W) ## io.src1(11) +
        0.U(6.W) ## io.src1(12) + 0.U(6.W) ## io.src1(13) + 0.U(6.W) ## io.src1(14) + 0.U(6.W) ## io.src1(15) +
        0.U(6.W) ## io.src1(16) + 0.U(6.W) ## io.src1(17) + 0.U(6.W) ## io.src1(18) + 0.U(6.W) ## io.src1(19) +
        0.U(6.W) ## io.src1(20) + 0.U(6.W) ## io.src1(21) + 0.U(6.W) ## io.src1(22) + 0.U(6.W) ## io.src1(23) +
        0.U(6.W) ## io.src1(24) + 0.U(6.W) ## io.src1(25) + 0.U(6.W) ## io.src1(26) + 0.U(6.W) ## io.src1(27) +
        0.U(6.W) ## io.src1(28) + 0.U(6.W) ## io.src1(29) + 0.U(6.W) ## io.src1(30) + 0.U(6.W) ## io.src1(31)
    }

    is(ANDN) { io.out := io.src1 & (~(io.src2)) }
    is(ORN) { io.out := io.src1 | (~(io.src2)) }
    is(XNOR) { io.out := ~(io.src1 ^ io.src2) }
    is(MIN) { io.out := Mux(io.src1.asSInt < io.src2.asSInt, io.src1, io.src2) }
    is(MINU) { io.out := Mux(io.src1 < io.src2, io.src1, io.src2) }
    is(MAX) { io.out := Mux(io.src1.asSInt < io.src2.asSInt, io.src2, io.src1) }
    is(MAXU) { io.out := Mux(io.src1 < io.src2, io.src2, io.src1) }

    is(SEXT_B) { io.out := Cat(Fill(24, io.src1(7)), io.src1(7, 0)) }
    is(SEXT_H) { io.out := Cat(Fill(16, io.src1(15)), io.src1(15, 0)) }

    is(BCLR) { io.out := io.src1 & ~(1.U(32.W) << io.src2(4, 0)) }
    is(BSET) { io.out := io.src1 | (1.U(32.W) << io.src2(4, 0)) }
    is(BINV) { io.out := io.src1 ^ (1.U(32.W) << io.src2(4, 0)) }
    is(BEXT) { io.out := (io.src1 >> io.src2(4, 0)) & 1.U }

    is(ROL) { io.out := (io.src1 << io.src2(4, 0)) | (io.src1 >> (32.U(6.W) - 0.U(1.W) ## io.src2(4, 0))) }
    is(ROR) { io.out := (io.src1 >> io.src2(4, 0)) | (io.src1 << (32.U(6.W) - 0.U(1.W) ## io.src2(4, 0))) }

    is(SH1ADD) { io.out := io.src2 + (io.src1 << 1.U) }
    is(SH2ADD) { io.out := io.src2 + (io.src1 << 2.U) }
    is(SH3ADD) { io.out := io.src2 + (io.src1 << 3.U) }

    is(REV8) { io.out := Cat(io.src1(7, 0), io.src1(15, 8), io.src1(23, 16), io.src1(31, 24)) }

    is(ZEXT_H) { io.out := Cat(0.U(16.W), io.src1(15, 0)) }

    is(ORC_B) {
      io.out := Cat(
        Mux(io.src1(31, 24) === 0.U, "b00000000".U, "b11111111".U),
        Mux(io.src1(23, 16) === 0.U, "b00000000".U, "b11111111".U),
        Mux(io.src1(15, 8) === 0.U, "b00000000".U, "b11111111".U),
        Mux(io.src1(7, 0) === 0.U, "b00000000".U, "b11111111".U)
      )
    }
  }
}
