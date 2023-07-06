package aias_lab5.Hw4

import chisel3._
import chisel3.util._

object condition{
  val EQ = "b000".U
  val NE = "b001".U
  val LT = "b100".U
  val GE = "b101".U
  val LTU = "b110".U
  val GEU = "b111".U
}

import condition._

class BranchComp extends Module{
    val io = IO(new Bundle{
        val en = Input(Bool())
        val funct3 = Input(UInt(3.W))
        val src1 = Input(UInt(32.W))
        val src2 = Input(UInt(32.W))

        val brtaken = Output(Bool()) //for pc.io.brtaken
    })
    
    //please implement your code below
	io.brtaken := false.B
	when(io.en){
	io.brtaken := MuxLookup(io.funct3,0.U,Seq(
	EQ -> (io.src1 === io.src2),
	NE -> (io.src1 =/= io.src2),
	LT -> (io.src1.asSInt < io.src2.asSInt),
	GE -> (io.src1.asSInt >= io.src2.asSInt),
	LTU -> (io.src1 < io.src2),
	GEU -> (io.src1 >= io.src2),
	))
	}
}