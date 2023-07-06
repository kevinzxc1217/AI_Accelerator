package aias_lab9.VectorCPU.Datapath

import chisel3._
import chisel3.stage.ChiselStage

class Vector_RegFile(readPorts: Int) extends Module {
  val io = IO(new Bundle {
    val vector_wen   = Input(Bool())
    val vector_waddr = Input(UInt(5.W))
    val vector_wdata = Input(UInt(64.W))
    val vector_raddr = Input(Vec(readPorts, UInt(5.W)))
    val vector_rdata = Output(Vec(readPorts, UInt(64.W)))
    val vector_regs  = Output(Vec(32, UInt(64.W)))
  })

  // 32 * 64 RegFile
  val init_value = Seq.fill(32)(0.U(64.W))

  val vector_regs = RegInit(VecInit(init_value))

  // Read
  (io.vector_rdata.zip(io.vector_raddr)).map { case (data, addr) =>
    data := vector_regs(addr)
  }

  // Write
  when(io.vector_wen) { vector_regs(io.vector_waddr) := io.vector_wdata }

  io.vector_regs := vector_regs
}
