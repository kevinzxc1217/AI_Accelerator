package aias_lab8.Single_Cycle.Controller

import chisel3._
import chisel3.util._


object opcode_map {
    val LOAD      = "b0000011".U
    val STORE     = "b0100011".U
    val BRANCH    = "b1100011".U
    val JALR      = "b1100111".U
    val JAL       = "b1101111".U
    val OP_IMM    = "b0010011".U
    val OP        = "b0110011".U
    val AUIPC     = "b0010111".U
    val LUI       = "b0110111".U
    val HCF       = "b0001011".U
	
    // Edit for Vector Extension
    val VOP       = "b1010111".U
	val VLOAD     = "b0000111".U
	val VSTORE    = "b0100111".U
}
object type_map{
	val R = 0.U
	val I = 1.U
    val S = 2.U
    val B = 3.U
    val J = 4.U
    val U = 5.U	
}
object ALU_op{
  val ADD  = 0.U
  val SLL  = 1.U
  val SLT  = 2.U
  val SLTU = 3.U
  val XOR  = 4.U
  val SRL  = 5.U
  val OR   = 6.U
  val AND  = 7.U
  val SUB  = 8.U
  val SRA  = 13.U
  val MUL  = 9.U
}

//**************************************************
//**                                              **
//**                                              **
//**      Modification of Vector Extension        **
//**                                              **
//**                                              **
//**************************************************
object vector_ALU_op{
    val VADD = "b000".U
	val VMUL = "b100".U
}


object condition{
  val EQ = "b000".U
  val NE = "b001".U
  val LT = "b100".U
  val GE = "b101".U
  val LTU = "b110".U
  val GEU = "b111".U
}

import opcode_map._,condition._,ALU_op._,vector_ALU_op._,type_map._

class Controller extends Module {
    val io = IO(new Bundle{
        val Inst = Input(UInt(32.W))
        val BrEq = Input(Bool())
        val BrLT = Input(Bool())

        val PCSel = Output(Bool())
        val ImmSel = Output(UInt(3.W))
        val RegWEn = Output(Bool())
        val BrUn = Output(Bool())
        val BSel = Output(Bool())
        val ASel = Output(UInt(2.W))
        val ALUSel = Output(UInt(4.W))
        val MemRW = Output(Bool())
        val WBSel = Output(UInt(2.W))

        //new
        val Lui = Output(Bool())
        val Hcf = Output(Bool())

        // Edit for Vector Extension
        val vector_ALUSel = Output(UInt(4.W))
        val vector_ASel = Output(Bool())
        val vector_BSel = Output(Bool())
        val vector_WBSel = Output(UInt(2.W))
        val vector_RegWEn = Output(Bool())
		val vector_VSel = Output(Bool())
		val MemV = Output(Bool())
    })
    
    val opcode = Wire(UInt(7.W))
    opcode := io.Inst(6,0)

    val funct3 = Wire(UInt(3.W))
    funct3 := io.Inst(14,12)

    val funct7 = Wire(UInt(7.W))
    funct7 := io.Inst(31,25)

    // Edit for Vector Extension
    val funct6 = Wire(UInt(6.W))
    funct6 := io.Inst(31,26)

    //Control signal
	io.RegWEn := MuxLookup(opcode, false.B, Seq(
		LOAD 	-> true.B,
		JALR 	-> true.B,
		JAL 	-> true.B,
		OP_IMM 	-> true.B,
		OP 		-> true.B,
		AUIPC 	-> true.B,
		LUI 	-> true.B
	))
	io.ASel := MuxLookup(opcode,0.U,Seq(
		BRANCH -> 1.U,
		JAL -> 1.U,
		AUIPC -> 1.U,
		LUI -> 2.U,//將src1設成0
	))
    io.BSel := Mux(opcode===OP,false.B,true.B)
    io.BrUn := Mux(funct3===LTU,true.B,false.B)
	io.MemRW := Mux(opcode===STORE,true.B,false.B)
    io.ImmSel := MuxLookup(opcode,0.U,Seq(
		LOAD 	-> I,
		STORE 	-> S,
		BRANCH 	-> B, 
		JALR 	-> I,
		JAL 	-> J,
		OP_IMM 	-> I,   
		AUIPC 	-> U,
		LUI 	-> U
	))
    io.ALUSel := MuxLookup(opcode,(0.U(4.W)),Seq(
		OP -> MuxLookup(funct7,Cat(0.U,funct3),Seq(
				"b0100000".U 	-> Cat(1.U,funct3),
				"b0000001".U 	-> MUL
			  )),
		OP_IMM -> Mux(funct7==="b0100000".U,Cat(1.U,funct3),Cat(0.U,funct3)),
	))
    io.PCSel := MuxLookup(opcode,false.B,Seq(
		JALR 	-> true.B,
		JAL 	-> true.B,
		BRANCH 	-> MuxLookup(funct3,false.B,Seq(
		    EQ -> io.BrEq,
			NE -> !io.BrEq,
			LT -> io.BrLT,
			GE -> !io.BrLT,
			LTU -> io.BrLT,
			GEU -> !io.BrLT
		))
	))
    io.WBSel := MuxLookup(opcode,1.U,Seq(// 0:MEM 1:ALU 2:PC
		LOAD 	-> 0.U,
		JALR 	-> 2.U,
		JAL 	-> 2.U
	))
    io.Lui := opcode === LUI
    io.Hcf := opcode === HCF

    //**************************************************
    //**                                              **
    //**                                              **
    //**      Modification of Vector Extension        **
    //**                                              **
    //**                                              **
    //**************************************************
	
	//???????????????
	io.vector_VSel   := false.B
	
	io.vector_ALUSel := MuxLookup(funct3, 0.U, Seq(
	VADD 	-> 0.U,
	VMUL    -> 1.U
	))
	
	io.MemV := MuxLookup(opcode, 0.U, Seq(
	VLOAD 	-> 1.U,
	VSTORE  -> 1.U
	))
	
	io.vector_ASel := false.B
	
	io.vector_BSel := MuxLookup(opcode, 0.U, Seq(
	VLOAD 	-> 1.U,
	VSTORE  -> 1.U
	))
	
    io.vector_RegWEn := MuxLookup(opcode, 0.U, Seq(
	VLOAD 	-> 1.U,
	VOP     -> 1.U
	))
	
	
    io.vector_WBSel := MuxLookup(opcode, 1.U, Seq(
	VLOAD 	-> 0.U,
	))
}