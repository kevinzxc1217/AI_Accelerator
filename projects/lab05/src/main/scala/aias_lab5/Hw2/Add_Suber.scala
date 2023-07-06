package aias_lab5.Hw2

import chisel3._
import chisel3.util._
import aias_lab5.Lab._

class Add_Suber extends Module{
    val io = IO(new Bundle{
    val in_1 = Input(UInt(4.W))
	val in_2 = Input(UInt(4.W))
	val op = Input(Bool()) // 0:ADD 1:SUB
	val out = Output(UInt(4.W))
	val o_f = Output(Bool())
  })

  //please implement your code below
  //這個array存了n個fulladder
  val FA_Array = Array.fill(4)(Module(new FullAdder()).io)
  val carry = Wire(Vec(5, Bool()))
  val sum  = Wire(Vec(4, Bool()))
  carry(0) := io.op
  
  for (i <- 0 until 4) {
    FA_Array(i).A := io.in_1(i)
    FA_Array(i).B := carry(0)^io.in_2(i)
    FA_Array(i).Cin := carry(i)
    carry(i+1) := FA_Array(i).Cout
    sum(i) := FA_Array(i).Sum
  }
  
  io.out := sum.asUInt
  io.o_f := sum(3)^carry(4)
}
