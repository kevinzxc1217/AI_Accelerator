package aias_lab6.Hw3

import chisel3._
import chisel3.util._

class NumGuess(seed:Int = 1) extends Module{
    require (seed > 0 , "Seed cannot be 0")
    
    val io  = IO(new Bundle{
        val gen = Input(Bool())
        val guess = Input(UInt(16.W))
        val puzzle = Output(Vec(4,UInt(4.W)))
        val ready  = Output(Bool())
        val g_valid  = Output(Bool())
        val A      = Output(UInt(3.W))
        val B      = Output(UInt(3.W))

        //don't care at Hw6-3-2 but should be considered at Bonus
        val s_valid = Input(Bool())
    })
		
	val sIdle :: sSet :: sGues :: Nil =Enum(3)
	val guess_buf = RegInit(VecInit(Seq.fill(4)(0.U(4.W))))
	val pass = RegInit(0.U(1.W))
	val a_buff = RegInit(0.U(3.W))
	val b_buff = RegInit(0.U(3.W))
	val index = RegInit(0.U(3.W))
	val index2 = RegInit(0.U(3.W))
	val state = RegInit(sIdle)
	val prng = Module(new PRNG(1))
	switch(state){
		is(sIdle){
			when(io.gen){state := sSet}
		}
		is(sSet){
			when(io.ready){state := sGues}
		}
		is(sGues){io.ready ===false.B}
	}
	//初始化
	io.ready := false.B
	io.puzzle := prng.io.puzzle
	prng.io.gen := false.B
	io.A      := 0.U
    io.B      := 0.U
	io.g_valid  := false.B
	
	
	
	
	//產生題目
	when(state === sSet){

		//prng.io.gen := true.B
		//io.puzzle := prng.io.puzzle
		pass := 1.U
		for(i <- 0 until 4){
			guess_buf(i) := io.guess((i+1)*4-1,i*4)
		}
		when(pass === 1.U){io.ready := true.B}
	}
	
	//開始猜
	when(state === sGues){
	//如果guess和puzzle有相同位置且一樣的數字，A+1；只有數字則B+1;
		when(index===4.U){
			when(a_buff===4.U){io.ready := false.B}
			.elsewhen(a_buff<4.U){
				io.ready := true.B
				a_buff := 0.U
				b_buff := 0.U
				state := sSet
			}
			
			io.g_valid := true.B
			index := 0.U
		}
		.elsewhen(index<=3.U){
			when(guess_buf(index) === io.puzzle(index)){
				a_buff := a_buff+1.U
			}
			.otherwise{
			//注意guess跟puzzle的index排序是反的
			when(index===0.U){
				when(guess_buf(0) === io.puzzle(0)){b_buff := b_buff+1.U}
				.elsewhen(guess_buf(0) === io.puzzle(1)){b_buff := b_buff+1.U}
				.elsewhen(guess_buf(0) === io.puzzle(2)){b_buff := b_buff+1.U}
				}
			.elsewhen(guess_buf(index-1.U)=/=guess_buf(index)&&index===1.U){
				when(guess_buf(1) === io.puzzle(0)){b_buff := b_buff+1.U}
				.elsewhen(guess_buf(1) === io.puzzle(1)){b_buff := b_buff+1.U}
				.elsewhen(guess_buf(1) === io.puzzle(3)){b_buff := b_buff+1.U}
				}
			.elsewhen(index===2.U&&guess_buf(index-1.U)=/=guess_buf(index)&&guess_buf(index-2.U)=/=guess_buf(index)){		
				when(guess_buf(2) === io.puzzle(0)){b_buff := b_buff+1.U}
				.elsewhen(guess_buf(2) === io.puzzle(2)){b_buff := b_buff+1.U}
				.elsewhen(guess_buf(2) === io.puzzle(3)){b_buff := b_buff+1.U}
				}
			.elsewhen(index===3.U&&guess_buf(3)=/=guess_buf(2)&&guess_buf(3)=/=guess_buf(1)&&guess_buf(3)=/=guess_buf(0)){
				when(guess_buf(3) === io.puzzle(1)){b_buff := b_buff+1.U}
				.elsewhen(guess_buf(3) === io.puzzle(2)){b_buff := b_buff+1.U}
				.elsewhen(guess_buf(3) === io.puzzle(3)){b_buff := b_buff+1.U}
				}
			}
			index := index+1.U
		}
		
		io.A := a_buff
		io.B := b_buff
	}
}