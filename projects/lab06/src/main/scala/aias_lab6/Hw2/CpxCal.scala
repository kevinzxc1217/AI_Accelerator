package aias_lab6.Hw2

import chisel3._
import chisel3.util._




class CpxCal extends Module{
    val io = IO(new Bundle{
        val key_in = Input(UInt(4.W))
        val value = Output(Valid(UInt(32.W)))
    })
	
   //please implement your code below
    val operator = WireDefault(false.B)
    operator := io.key_in >= 10.U && io.key_in <= 12.U

    val num = WireDefault(false.B)
    num := io.key_in < 10.U

    val equal = WireDefault(false.B)
    equal := io.key_in === 15.U
    
    val L_brack = WireDefault(false.B)
    L_brack := io.key_in === 13.U
	
	val R_brack = WireDefault(false.B)
    R_brack := io.key_in === 14.U
	
    //Reg Declaration====================================
    val in_buffer = RegNext(io.key_in)
    val src1 = RegInit(0.U(32.W))
    val op = RegInit(0.U(2.W))
    val src2 = RegInit(0.U(32.W))
	val not_op = RegInit(0.U(1.W))
	
	
    //State and Constant Declaration=====================
    val sIdle :: sSrc1 :: sOp :: sSrc2 :: sEqual :: Nil = Enum(5)
    val add = 0.U
    val sub = 1.U
    val mul = 2.U 
    val state = RegInit(sIdle)
	
    //Next State Decoder
    switch(state){
        is(sIdle){
			when(L_brack){not_op := 1.U}
            state := sSrc1
        }
		
        is(sSrc1){
		when(L_brack){not_op := 1.U}
		when(R_brack){not_op := 0.U}
		when(equal) {state := sEqual}
		
		when(not_op ===0.U){
			when(operator) {state := sOp}
			}
        }
		
        is(sOp){
            when(num) {state := sSrc2}
			when(L_brack){
			state := sSrc2
			not_op := 1.U
			}
        }
		
        is(sSrc2){
		when(L_brack){not_op := 1.U}
		when(R_brack){not_op := 0.U}
		when(equal) {state := sEqual}
		when(not_op ===0.U){
			when(operator) {state := sOp}
			}
        }
		
        is(sEqual){
            state := sSrc1
        }
    }
    //==================================================
    when(state === sSrc1){
		when(in_buffer < 10.U){src1 := (src1<<3.U) + (src1<<1.U) + in_buffer}
		when(in_buffer ===14.U){src1 := -src1}
	}
    when(state === sSrc2){
		when(in_buffer < 10.U){src2 := (src2<<3.U) + (src2<<1.U) + in_buffer}
		when(in_buffer ===14.U){src2 := -src2}
	}
    when(state === sOp){
	op := in_buffer - 10.U
		src1 := MuxLookup((op),0.U,Seq( 
		add -> (src1 + src2),
		sub -> (src1 - src2),
		mul -> (src1 * src2)
		))
		src2 :=0.U
	}

    when(state === sEqual){
        src1 := 0.U
        src2 := 0.U
        op := 0.U
        in_buffer := 0.U
		not_op := 0.U
    }

    io.value.valid := Mux(state === sEqual,true.B,false.B)
    io.value.bits := MuxLookup(op,0.U,Seq(
        add -> (src1 + src2),
        sub -> (src1 - src2),
        mul -> (src1 * src2)
    ))
}