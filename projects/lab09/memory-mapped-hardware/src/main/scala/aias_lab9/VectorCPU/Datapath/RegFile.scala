package aias_lab9.VectorCPU.Datapath

import chisel3._

class RegFile(readPorts: Int) extends Module {
  val io = IO(new Bundle {
    val wen   = Input(Bool())
    val waddr = Input(UInt(5.W))
    val wdata = Input(UInt(32.W))
    val raddr = Input(Vec(readPorts, UInt(5.W)))
    val rdata = Output(Vec(readPorts, UInt(32.W)))
    val regs  = Output(Vec(32, UInt(32.W)))
  })

  val regs = RegInit(VecInit(Seq.fill(2)(0.U(32.W)) ++ Seq("h10000".U(32.W) - 1.U) ++ Seq.fill(29)(0.U(32.W))))

  // Wiring
  (io.rdata zip io.raddr).map { case (data, addr) => data := regs(addr) }
  when(io.wen) { regs(io.waddr) := io.wdata }
  regs(0) := 0.U

  io.regs := regs
}
