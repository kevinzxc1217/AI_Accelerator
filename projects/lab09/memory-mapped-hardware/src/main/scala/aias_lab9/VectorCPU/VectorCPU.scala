package aias_lab9.VectorCPU

import chisel3._
import chisel3.util._

import aias_lab9.AXILite._

import aias_lab9.VectorCPU.Memory._
import aias_lab9.VectorCPU.Controller._
import aias_lab9.VectorCPU.Datapath._

import aias_lab9.VectorCPU.opcode_map._

class VectorCPU(memAddrWidth: Int, memDataWidth: Int, instrBinaryFile: String) extends Module {
  val io = IO(new Bundle {
    // AXI
    val bus_master = new AXILiteMasterIF(memAddrWidth, memDataWidth)

    // System
    val pc          = Output(UInt(15.W))
    val regs        = Output(Vec(32, UInt(32.W)))
    val vector_regs = Output(Vec(32, UInt(64.W)))
    val Hcf         = Output(Bool())
  })

  // Module
  val im  = Module(new InstMem(15, instrBinaryFile))
  val ct  = Module(new Controller(memAddrWidth, memDataWidth))
  val pc  = Module(new PC())
  val ig  = Module(new ImmGen())
  val rf  = Module(new RegFile(2))
  val alu = Module(new ALU())
  val bc  = Module(new BranchComp())

  val vrf  = Module(new Vector_RegFile(3))
  val valu = Module(new Vector_ALU())

  // wire
  val opcode    = Wire(UInt(7.W))
  val rd        = Wire(UInt(5.W))
  val rs1       = Wire(UInt(5.W))
  val rs2       = Wire(UInt(5.W))
  val funct3    = Wire(UInt(3.W))
  val inst_31_7 = Wire(UInt(25.W))

  val vd     = Wire(UInt(5.W))
  val vs1    = Wire(UInt(5.W))
  val vs2    = Wire(UInt(5.W))
  val funct6 = Wire(UInt(6.W))

  opcode    := im.io.inst(6, 0)
  rd        := im.io.inst(11, 7)
  rs1       := im.io.inst(19, 15)
  rs2       := im.io.inst(24, 20)
  funct3    := im.io.inst(14, 12)
  inst_31_7 := im.io.inst(31, 7)

  vd     := im.io.inst(11, 7)
  vs1    := im.io.inst(19, 15)
  vs2    := im.io.inst(24, 20)
  funct6 := im.io.inst(31, 26)

  // PC
  pc.io.PCSel   := ct.io.PCSel
  pc.io.alu_out := alu.io.out
  pc.io.Hcf     := ct.io.Hcf

  // Insruction Memory
  im.io.raddr := pc.io.pc

  // ImmGen
  ig.io.ImmSel    := ct.io.ImmSel
  ig.io.inst_31_7 := inst_31_7

  // RegFile
  rf.io.raddr(0) := rs1
  rf.io.raddr(1) := rs2
  rf.io.wen      := ct.io.RegWEn
  rf.io.waddr    := rd
  rf.io.wdata := MuxLookup(
    ct.io.WBSel,
    0.U,
    Seq(
      0.U -> MuxLookup(
        funct3,
        io.bus_master.readData.bits.data(31, 0),
        Seq(
          "b000".U(3.W) -> Cat(Fill(24, io.bus_master.readData.bits.data(7)), io.bus_master.readData.bits.data(7, 0)),
          "b001".U(3.W) -> Cat(Fill(16, io.bus_master.readData.bits.data(15)), io.bus_master.readData.bits.data(15, 0)),
          "b010".U(3.W) -> io.bus_master.readData.bits.data(31, 0),
          "b100".U(3.W) -> Cat(0.U(24.W), io.bus_master.readData.bits.data(7, 0)),
          "b101".U(3.W) -> Cat(0.U(16.W), io.bus_master.readData.bits.data(15, 0))
        )
      ),
      1.U -> alu.io.out,      // from ALU
      2.U -> (pc.io.pc + 4.U) // from (PC + 4)
    )
  )

  // Vector RegFile
  vrf.io.vector_raddr(0) := vd
  vrf.io.vector_raddr(1) := vs1
  vrf.io.vector_raddr(2) := vs2
  vrf.io.vector_waddr    := vd
  vrf.io.vector_wen      := ct.io.vector_RegWEn
  vrf.io.vector_wdata := MuxLookup(
    ct.io.vector_WBSel,
    0.U,
    Seq(
      0.U -> valu.io.vector_out,              // from Vector ALU
      1.U -> io.bus_master.readData.bits.data // from DataMemory
    )
  )

  // ALU
  val rdata_or_zero = Mux(ct.io.Lui, 0.U(32.W), rf.io.rdata(0))
  alu.io.src1 := MuxLookup(
    ct.io.ASel,
    0.U,
    Seq(
      0.U -> rdata_or_zero,
      1.U -> pc.io.pc
    )
  )
  alu.io.src2 := MuxLookup(
    ct.io.BSel,
    0.U,
    Seq(
      0.U -> rf.io.rdata(1),
      1.U -> ig.io.imm,
      2.U -> 0.U
    )
  )
  alu.io.ALUSel := ct.io.ALUSel

  // Vector ALU
  valu.io.vector_src0   := vrf.io.vector_rdata(0)
  valu.io.vector_src1   := vrf.io.vector_rdata(1)
  valu.io.vector_src2   := vrf.io.vector_rdata(2)
  valu.io.vector_ALUSel := ct.io.vector_ALUSel

  // Branch Comparator
  bc.io.BrUn := ct.io.BrUn
  bc.io.src1 := rf.io.rdata(0)
  bc.io.src2 := rf.io.rdata(1)

  // Controller
  ct.io.Inst := im.io.inst
  ct.io.BrEq := bc.io.BrEq
  ct.io.BrLT := bc.io.BrLT

  // AXI Bus
  io.bus_master.writeAddr <> ct.io.writeAddr
  io.bus_master.writeData <> ct.io.writeData
  io.bus_master.writeResp <> ct.io.writeResp
  io.bus_master.readAddr <> ct.io.readAddr
  io.bus_master.readData <> ct.io.readData

  io.bus_master.readAddr.bits.addr  := alu.io.out
  io.bus_master.writeAddr.bits.addr := alu.io.out
  io.bus_master.writeData.bits.data := MuxLookup(
    ct.io.MemSel,
    0.U,
    Seq(
      0.U -> rf.io.rdata(1),
      1.U -> vrf.io.vector_rdata(0)
    )
  )
  io.bus_master.writeData.bits.strb := MuxLookup(
    opcode,
    0.U,
    Seq(
      STORE -> (MuxLookup(
        funct3,
        0.U,
        Seq(
          "b000".U(3.W) -> "b1".U,
          "b001".U(3.W) -> "b11".U,
          "b010".U(3.W) -> "b1111".U
        )
      )),
      VS -> "b11111111".U
    )
  )

  // System
  io.pc          := pc.io.pc
  io.regs        := rf.io.regs
  io.vector_regs := vrf.io.vector_regs
  io.Hcf         := ct.io.Hcf
}
