package aias_lab6.Hw2

import chisel3._
import chisel3.util._

class NegIntGen extends Module{
    val io = IO(new Bundle{
        val key_in = Input(UInt(4.W))
        val value = Output(Valid(UInt(32.W)))
    })
    
    //please implement your code below
    val equal = WireDefault(false.B)
    equal := io.key_in === 15.U
    val num = WireDefault(false.B)
    num := io.key_in < 10.U
	
	
	
    val sIdle :: sAccept :: sEqual :: Nil = Enum(3)
    val state = RegInit(sIdle)
    //Next State Decoder
    switch(state){
        is(sIdle){
        state := sAccept
        }
        is(sAccept){
        when(equal) {state := sEqual}
        }
        is(sEqual){
            state := sAccept
        }
    }

    val in_buffer = RegNext(io.key_in)
    val number = RegInit(0.U(32.W))
	val neg = RegInit(0.U(1.W))
	
    when(state === sAccept){
		when(in_buffer < 10.U){
        number := (number<<3.U) + (number<<1.U) + in_buffer}
		//number := (number<<6.U) + (number<<5.U) + (number<<2.U) +in_buffer
		.elsewhen(in_buffer === 11.U){neg := 1.U}
		
    }.elsewhen(state === sEqual){
        in_buffer := 0.U
        number := 0.U
		neg := 0.U
    }

    io.value.valid := Mux(state === sEqual,true.B,false.B)
	when(neg===1.U){io.value.bits := -number}
    .otherwise{io.value.bits := number}
}