package aias_lab5.Hw4

import chisel3._
import chisel3.util._

class PC extends Module {
    val io = IO(new Bundle{
        val brtaken = Input(Bool()) 
        val jmptaken =  Input(Bool())
        val offset = Input(UInt(32.W))
        val pc = Output(UInt(32.W))
    })
	
    val pcReg = RegInit(0.U(32.W))
    //pcReg := pcReg + 4.U
	when(io.brtaken){
		pcReg := ((io.offset>>2.U)<<2.U)
	}.elsewhen(io.jmptaken){
		pcReg := ((io.offset>>2.U)<<2.U)
	}.otherwise{
		pcReg := pcReg + 4.U
	}
    io.pc := pcReg
}

