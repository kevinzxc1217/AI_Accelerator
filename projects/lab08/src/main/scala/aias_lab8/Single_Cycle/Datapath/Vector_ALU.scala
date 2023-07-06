// Create a module for vector reg arithmetic
package aias_lab8.Single_Cycle.Datapath

import chisel3._
import chisel3.util._ 

object vector_ALU_op{
    val VADD_VV = 0.U
	val VMUL_VX = 1.U
}

import vector_ALU_op._



class VECTOR_ALUIO extends Bundle{
  val vector_src1    = Input(UInt(64.W))
  val vector_src2    = Input(UInt(64.W))
  val vector_ALUSel  = Input(UInt(4.W))
  val vector_out     = Output(UInt(64.W))
}

class Vector_ALU extends Module{
  val io = IO(new VECTOR_ALUIO) 

  val wire_set = Wire(Vec(8,UInt(8.W)))

  //Default Value of wire_set
  wire_set.foreach{wire=>
    wire := 0.U
  }
  printf("io.vector_ALUSel  = %x\n",io.vector_ALUSel)
  switch(io.vector_ALUSel){

    // Improved version
    is(VADD_VV){
      //choise 1
      //wire_set(0) := io.vector_src1(7,0)   + io.vector_src2(7,0)     
      //wire_set(1) := io.vector_src1(15,8)  + io.vector_src2(15,8)
      //wire_set(2) := io.vector_src1(23,16) + io.vector_src2(23,16)
      //wire_set(3) := io.vector_src1(31,24) + io.vector_src2(31,24)
      //wire_set(4) := io.vector_src1(39,32) + io.vector_src2(39,32)
      //wire_set(5) := io.vector_src1(47,40) + io.vector_src2(47,40)
      //wire_set(6) := io.vector_src1(55,48) + io.vector_src2(55,48)
      //wire_set(7) := io.vector_src1(63,56) + io.vector_src2(63,56)  


      //choise 2
	  //64bit = 8個8bit的elem
      val src1_unit = io.vector_src1.asTypeOf(Vec(8,UInt(8.W)))
      val src2_unit = io.vector_src2.asTypeOf(Vec(8,UInt(8.W)))
      
      wire_set.zip(src1_unit zip src2_unit).map{
		case(wire,(src1,src2)) => wire := src1 + src2
		
      }    
    }

	is(VMUL_VX){
      val src1_unit = io.vector_src1.asTypeOf(Vec(8,UInt(8.W)))
      val src2_unit = io.vector_src2.asTypeOf(Vec(8,UInt(8.W)))
      //printf("src1_unit  = %x\n",io.vector_src1)
	  //printf("src2_unit  = %x\n",io.vector_src2)
      wire_set.zip(src1_unit zip src2_unit).map{
		case(wire,(src1,src2)) => wire := src1 * src2
      }    
    }
	
  }

  //test
  //val out = io.vector_src1 * io.vector_src2
  //printf("out  = %x\n",out)

  io.vector_out := wire_set.asUInt
  //printf("vout = %x\n",io.vector_out)
  
}
