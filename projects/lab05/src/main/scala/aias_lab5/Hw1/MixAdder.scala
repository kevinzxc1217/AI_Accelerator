package aias_lab5.Hw1

import chisel3._
import aias_lab5.Lab._

class MixAdder (n:Int) extends Module{
  val io = IO(new Bundle{
      val Cin = Input(UInt(1.W))
      val in1 = Input(UInt((4*n).W))
      val in2 = Input(UInt((4*n).W))
      val Sum = Output(UInt((4*n).W))
      val Cout = Output(UInt(1.W))
  })
  
  //please implement your code below
  
  val CLA_Array = Array.fill(n)(Module(new CLAdder()).io)
  val carry = Wire(Vec(n+1, UInt(1.W)))
  val sum = Wire(Vec(n, UInt(4.W)))
  //val sum   = Wire(Vec(n, Bool()))

  carry(0) := io.Cin	
  //對應adder的輸入輸出
  for (i <- 0 until n) {
    //FA:CLA的
    CLA_Array(i).in1 := io.in1(4*i+3,4*i)
    CLA_Array(i).in2 := io.in2(4*i+3,4*i)
    CLA_Array(i).Cin := carry(i)
	//wire的
    carry(i+1) := CLA_Array(i).Cout
    sum(i) := CLA_Array(i).Sum
  }
  //io的
  io.Sum := sum.asUInt
  io.Cout := carry(n)
  
  //io.Sum := 0.U
  //io.Cout := 0.U
}