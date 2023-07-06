package aias_lab5.Hw4

import chisel3._
import chisel3.util._

class top extends Module {
    val io = IO(new Bundle{
        val pc_out = Output(UInt(32.W))
        val alu_out = Output(UInt(32.W))
        val rf_wdata_out = Output(UInt(32.W))
        val brtaken_out = Output(Bool())
        val jmptaken_out = Output(Bool())
		val dm_rdata_out = Output(SInt(32.W))
		val dm_wdata_out = Output(UInt(32.W))
    })

    val pc = Module(new PC())
    val im = Module(new InstMem())
    val dc = Module(new Decoder())
    val rf = Module(new RegFile(2))
    val alu = Module(new ALU())
    val bc = Module(new BranchComp())
    val dm = Module(new DataMem())
	
    //PC
    pc.io.jmptaken :=  false.B // Don't modify //dc.io.ctrl_Jmp
    pc.io.brtaken :=   false.B // Don't modify //dc.io.ctrl_Br 
    pc.io.offset :=   0.U // Don't modify //dc.io.imm.asUInt
    
    //Insruction Memory
    im.io.raddr := pc.io.pc
    
    //Decoder
    dc.io.inst := im.io.rdata
    
    //RegFile
    rf.io.raddr(0) := dc.io.rs1
    rf.io.raddr(1) := dc.io.rs2
	rf.io.wdata := Mux(dc.io.ctrl_WBSel,Mux(dc.io.opcode==="b1101111".U||dc.io.opcode==="b1100111".U,pc.io.pc+4.U,alu.io.out),dm.io.rdata.asUInt)
	rf.io.waddr := 0.U  // Don't modify
    rf.io.wen := false.B // Don't modify

    //ALU
    val rdata_or_zero = WireDefault(0.U(32.W))
    alu.io.src1 := Mux(dc.io.ctrl_ASel,pc.io.pc,rf.io.rdata(0))
    alu.io.src2 := Mux(dc.io.ctrl_BSel
						,Mux(dc.io.ctrl_Br
							,Mux(bc.io.brtaken,dc.io.imm.asUInt,0.U)
							,dc.io.imm.asUInt)
						,rf.io.rdata(1))
						
	alu.io.funct3 := dc.io.funct3
	alu.io.funct7 := dc.io.funct7
    alu.io.opcode := dc.io.opcode
    
    //Data Memory
    dm.io.funct3 := dc.io.funct3
    dm.io.raddr := alu.io.out
    dm.io.wen   := dc.io.ctrl_MemRW
    dm.io.waddr := alu.io.out
    dm.io.wdata := rf.io.rdata(1)
	
    //Branch Comparator
    bc.io.en := dc.io.ctrl_Br // need to be changed
    bc.io.funct3 := dc.io.funct3
    bc.io.src1 := rf.io.rdata(0)
    bc.io.src2 := rf.io.rdata(1)


    //Check Ports
    io.pc_out := pc.io.pc
    io.alu_out := alu.io.out 
    io.rf_wdata_out := rf.io.wdata
    io.brtaken_out := bc.io.brtaken
    io.jmptaken_out := dc.io.ctrl_Jmp//false.B // need to be changed
	io.dm_rdata_out := dm.io.rdata
	io.dm_wdata_out := dm.io.wdata
}