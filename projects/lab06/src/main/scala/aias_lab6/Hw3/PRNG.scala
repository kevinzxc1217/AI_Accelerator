package aias_lab6.Hw3

import chisel3._
import chisel3.util._
class LFSR_Galois (n:Int)extends Module{
    
    val io = IO(new Bundle{
        val seed = Input(Valid(UInt(n.W)))
        val rndNum = Output(UInt(n.W))
    })
    
    val shiftReg = RegInit(VecInit(Seq.fill(n)(false.B)))
    
    when(io.seed.valid){
      shiftReg zip io.seed.bits.asBools map {case(l,r) => l := r}
    }.otherwise{
        
      //Right Barrel Shift Register
      (shiftReg.zipWithIndex).map{
          case(sr,i) => sr := shiftReg((i+1)%n)
      }
    
      //Galois LFSR
      LfsrTaps(n).map{x => {shiftReg(x-1) := shiftReg(x) ^ shiftReg(0)}}
    }

    io.rndNum := shiftReg.asUInt
}
object LfsrTaps {
    def apply(size: Int): Seq[Int] = {
        size match {
            // Seqp[Int] means the taps in LFSR
            case 4 => Seq(3)          //p(x) = x^4+x^3+1
            case 8 => Seq(6,5,4)      //p(x) = x^8+x^6+x^5+x^4+1
            case 16 => Seq(14,13,11) //p(x) = x^16+x^14+x^13+x^11+1
            case _ => throw new Exception("No LFSR taps stored for requested size")
        }
    }
}



class PRNG(seed:Int) extends Module{
    val io = IO(new Bundle{
        val gen = Input(Bool())
        val puzzle = Output(Vec(4,UInt(4.W)))
        val ready = Output(Bool())
    })
	val myReg = RegInit(0.U(4.W))
	val cnt = RegInit(0.U(8.W))
	val cnt2 = RegInit(0.U(3.W))
    val lfsrInst = Module(new LFSR_Galois(4))
    val lfsr = RegInit(VecInit(Seq.fill(4)(0.U(4.W))))
	
	val prep :: finish :: Nil = Enum(2)
	val state = RegInit(prep)
	io.puzzle := lfsr
	io.ready := false.B
	lfsrInst.io.seed.valid := false.B
	lfsrInst.io.seed.bits := 1.U
	
	
	switch(state){
		is(prep){
		when(cnt2 === 4.U){
			cnt2 := 0.U
			state := finish
			}
		}
		is(finish){
		when(io.gen === true.B){
			state := prep
		}
		}
	}
	
	when(state === prep){
		when(cnt===0.U)
		{
			lfsrInst.io.seed.valid := true.B
			lfsrInst.io.seed.bits := 1.U 
		}.otherwise
		{
			lfsrInst.io.seed.valid := false.B
			myReg := lfsrInst.io.rndNum
			when(cnt2===0.U){
				when(myReg===10.U){lfsr(cnt2):=0.U}
				.elsewhen(myReg===11.U){lfsr(cnt2):=1.U}
				.elsewhen(myReg===12.U){lfsr(cnt2):=2.U}
				.elsewhen(myReg===13.U){lfsr(cnt2):=3.U}
				.elsewhen(myReg===14.U){lfsr(cnt2):=4.U}
				.elsewhen(myReg===15.U){lfsr(cnt2):=5.U}
				.elsewhen(myReg===16.U){lfsr(cnt2):=6.U}
				.otherwise{lfsr(cnt2) := myReg}
				cnt2 := cnt2 + 1.U
			}.otherwise{
				when(cnt2===1.U&myReg=/=lfsr(cnt2-1.U)&(myReg-10.U)=/=lfsr(cnt2-1.U)){
					when(myReg===10.U){lfsr(cnt2):=0.U}
					.elsewhen(myReg===11.U){lfsr(cnt2):=1.U}
					.elsewhen(myReg===12.U){lfsr(cnt2):=2.U}
					.elsewhen(myReg===13.U){lfsr(cnt2):=3.U}
					.elsewhen(myReg===14.U){lfsr(cnt2):=4.U}
					.elsewhen(myReg===15.U){lfsr(cnt2):=5.U}
					.elsewhen(myReg===16.U){lfsr(cnt2):=6.U}
					.otherwise{lfsr(cnt2) := myReg}
					cnt2 := cnt2 + 1.U
				}
				.elsewhen(cnt2===2.U&myReg=/=lfsr(cnt2-1.U)&(myReg-10.U)=/=lfsr(cnt2-1.U)&myReg=/=lfsr(cnt2-2.U)&(myReg-10.U)=/=lfsr(cnt2-2.U)){
					when(myReg===10.U){lfsr(cnt2):=0.U}
					.elsewhen(myReg===11.U){lfsr(cnt2):=1.U}
					.elsewhen(myReg===12.U){lfsr(cnt2):=2.U}
					.elsewhen(myReg===13.U){lfsr(cnt2):=3.U}
					.elsewhen(myReg===14.U){lfsr(cnt2):=4.U}
					.elsewhen(myReg===15.U){lfsr(cnt2):=5.U}
					.elsewhen(myReg===16.U){lfsr(cnt2):=6.U}
					.otherwise{lfsr(cnt2) := myReg}
					cnt2 := cnt2 + 1.U
				}
				.elsewhen(cnt2===3.U&myReg=/=lfsr(cnt2-1.U)&(myReg-10.U)=/=lfsr(cnt2-1.U)&myReg=/=lfsr(cnt2-2.U)&(myReg-10.U)=/=lfsr(cnt2-2.U)&myReg=/=lfsr(cnt2-3.U)&(myReg-10.U)=/=lfsr(cnt2-3.U)){
					when(myReg===10.U){lfsr(cnt2):=0.U}
					.elsewhen(myReg===11.U){lfsr(cnt2):=1.U}
					.elsewhen(myReg===12.U){lfsr(cnt2):=2.U}
					.elsewhen(myReg===13.U){lfsr(cnt2):=3.U}
					.elsewhen(myReg===14.U){lfsr(cnt2):=4.U}
					.elsewhen(myReg===15.U){lfsr(cnt2):=5.U}
					.elsewhen(myReg===16.U){lfsr(cnt2):=6.U}
					.otherwise{lfsr(cnt2) := myReg}
					cnt2 := cnt2 + 1.U
				}
			}
				
			
			
			
		}
		
		cnt := cnt+1.U
	}
	
	when(state === finish){
		io.ready := true.B
		io.puzzle := lfsr
		when(io.gen){
			io.ready := false.B
			lfsr := VecInit(Seq.fill(4)(0.U(4.W)))
		}
	}
}
