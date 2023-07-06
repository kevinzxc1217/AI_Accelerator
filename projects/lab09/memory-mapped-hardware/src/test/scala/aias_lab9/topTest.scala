package aias_lab9.ontop

import scala.io.Source
import chisel3.iotesters.{PeekPokeTester, Driver}
import scala.language.implicitConversions
import java.io._
//import aias_lab9.SystolicArray


class topTest(dut: top) extends PeekPokeTester(dut) {

  implicit def bigint2bool(bi: BigInt) = if (bi != 0) true else false
  
  
  poke(dut.io.ENABLE,0x1)
  while(peek(dut.io.finish) == 0){
  		step(1)
  }
    // peek讀output poke寫入input
	
	
  println("A_mat :")
  poke(dut.io.dm_addr, 0x0)
  var value_0 = String
		.format("%" + 16 + "s", peek(dut.io.dm_data).toString(16))
		.replace(' ','0')
  println(s"MEM[07] ~ MEM[00] : 0x${value_0} ")
  step(5)
  poke(dut.io.dm_addr, 0x8)
  var value_1 = String
		.format("%" + 16 + "s", peek(dut.io.dm_data).toString(16))
		.replace(' ','0')
  println(s"MEM[15] ~ MEM[08] : 0x${value_1} ")
  step(5)
  
  
  println("B_mat :")
  poke(dut.io.dm_addr, 0x10)
  var value_2 = String
		.format("%" + 16 + "s", peek(dut.io.dm_data).toString(16))
		.replace(' ','0')
  println(s"MEM[23] ~ MEM[16] : 0x${value_2} ")
  step(5)
  
  poke(dut.io.dm_addr, 0x18)
  var value_3 = String
		.format("%" + 16 + "s", peek(dut.io.dm_data).toString(16))
		.replace(' ','0')
  println(s"MEM[31] ~ MEM[24] : 0x${value_3} ")
  step(5)
  
  
  println("C_mat :")
  poke(dut.io.dm_addr, 0x20)
  var value_4 = String
		.format("%" + 16 + "s", peek(dut.io.dm_data).toString(16))
		.replace(' ','0')
  println(s"MEM[39] ~ MEM[32] : 0x${value_4} ")
  step(5)
  
  poke(dut.io.dm_addr, 0x28)
  var value_5 = String
		.format("%" + 16 + "s", peek(dut.io.dm_data).toString(16))
		.replace(' ','0')
  println(s"MEM[47] ~ MEM[40] : 0x${value_5} ")
  step(5)
  
  
}

object topTest extends App {
  Driver.execute(
    Array("-tbn", "verilator"),
    () => new top(32,64)
  ) { c: top =>
    new topTest(c)
  }
}
