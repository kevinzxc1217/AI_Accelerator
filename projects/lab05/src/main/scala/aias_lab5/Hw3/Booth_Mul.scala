package aias_lab5.Hw3

import chisel3._
import chisel3.util._
import scala.annotation.switch

//------------------Radix 4---------------------
class Booth_Mul(width:Int) extends Module {
  val io = IO(new Bundle{
    val in1 = Input(UInt(width.W))      //Multiplicand 上
    val in2 = Input(UInt(width.W))      //Multiplier 下
    val out = Output(UInt((2*width).W)) //product
  })
  //please implement your code below
  val partial = Wire(Vec(8, SInt((2*width+1).W)))
  val multiplier= Wire(SInt((width+1).W))
  val multiplicand= Wire(SInt((2*width).W))
  val n_multiplicand= Wire(SInt((2*width).W))
  val product = Wire(SInt((2*width+1).W))
  
  
  multiplicand := (io.in1.asSInt)
  n_multiplicand := (((~(io.in1.asSInt))+1.S).asSInt).asSInt
  multiplier := Cat(io.in2,0.U).asSInt
  //operation
  //每次shift2，16bit共8次
  for (i <- 0 until (width/2)) {
  partial(i) := MuxLookup(multiplier(2*i+2,2*i),0.S,Seq(
  "b000".U -> 0.S, //0
  "b001".U -> multiplicand, //1
  "b010".U -> multiplicand ,//1
  "b011".U -> (multiplicand + multiplicand), //2
  "b100".U -> (n_multiplicand + n_multiplicand), //-2
  "b101".U -> n_multiplicand, //-1
  "b110".U -> n_multiplicand, //-1
  "b111".U -> 0.S //0
  ))
  }
  //partial product
  product := (partial(0)<<0) + (partial(1)<<2) + (partial(2)<<4) + (partial(3)<<6) + (partial(4)<<8) + (partial(5)<<10)+ (partial(6)<<12) + (partial(7)<<14)
  
  //Sign_extend , Shift
  io.out := product(2*width,0).asUInt
  //io.out := product.reduce(_+_)
}
