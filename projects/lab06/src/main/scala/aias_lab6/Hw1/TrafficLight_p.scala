package aias_lab6.Hw1

import chisel3._
import chisel3.util._

class TrafficLight_p(Ytime:Int, Gtime:Int, Ptime:Int) extends Module{
  val io = IO(new Bundle{
    val P_button = Input(Bool())
    val H_traffic = Output(UInt(2.W))
    val V_traffic = Output(UInt(2.W))
    val P_traffic = Output(UInt(2.W))
    val timer     = Output(UInt(5.W)) 
  })

  //please implement your code below...
  //parameter declaration
  val Off = 0.U
  val Red = 1.U
  val Yellow = 2.U
  val Green = 3.U
  val PG = 1.U

  
  
  val sIdle :: sHGVR :: sHYVR :: sHRVG :: sHRVY :: sPG :: Nil = Enum(6)
  
  //State register
  val state = RegInit(sIdle)
  val ori_state = RegInit(sIdle)
  
  //Counter============================
  val cntMode = WireDefault(0.U(2.W))
  val cntReg = RegInit(0.U(4.W))
  val cntDone = Wire(Bool())
  cntDone := cntReg === 0.U
  
  when(cntDone){
	//重置
    when(cntMode === 0.U){
	  //綠燈
      cntReg := (Gtime-1).U
    }.elsewhen(cntMode === 1.U){
	  //黃燈
      cntReg := (Ytime-1).U
    }.elsewhen(cntMode === 2.U){
	  //行人
      cntReg := (Ptime-1).U
	}
  }.otherwise{
    //cntDone不等於0時，不斷的將cntReg-1做倒數
    cntReg := cntReg - 1.U
  }
  //Counter end========================
  //Next State Decoder
  //按下行人通行，初始化cntReg
  //ori_state := state
  when(io.P_button) {
	state := sPG
	cntReg := 4.U
	}
  .otherwise{
	  switch(state){
		is(sIdle){
		  state := sHGVR
		}
		//行人開始倒數，完成切回原狀態，並且原時間重新計時
		is(sPG){
		  when(cntDone) {
		  state := ori_state
		  when(ori_state === sHGVR || ori_state === sHRVG)
		  {
			cntReg:= 6.U
		  }
		  .elsewhen(ori_state === sHYVR || ori_state === sHRVY)
		  {
			cntReg:= 2.U
		  }.otherwise{cntReg:= 10.U}
		  }
		}
		is(sHGVR){
		  ori_state := state
		  when(cntDone) {
		  state := sHYVR
		  ori_state := sHYVR
		  }
		}
		is(sHYVR){
		  ori_state := state
		  when(cntDone) {
		  state := sHRVG
		  ori_state := sHRVG
		  }
		}
		is(sHRVG){
		  ori_state := state
		  when(cntDone) {
		  state := sHRVY
		  ori_state := sHRVY
		  }
		  
		}
		is(sHRVY){
		ori_state := state
		  when(cntDone) {
		  state := sHGVR
		  ori_state := sHGVR
		  }
		  
		}
	  }
  }
  
  
  //Output Decoder
  //Default statement
  cntMode := 0.U
  io.H_traffic := Off
  io.V_traffic := Off
  io.P_traffic := Off
  
  switch(state){
    is(sHGVR){
      cntMode := 1.U
      io.H_traffic := Green
      io.V_traffic := Red
    }
    is(sHYVR){
      cntMode := 0.U
      io.H_traffic := Yellow
      io.V_traffic := Red
    }
    is(sHRVG){
      cntMode := 1.U
      io.H_traffic := Red
      io.V_traffic := Green
    }
    is(sHRVY){
      cntMode := 0.U
      io.H_traffic := Red
      io.V_traffic := Yellow
    }
	is(sPG){
      cntMode := 2.U
      io.H_traffic := Red
      io.V_traffic := Red
	  io.P_traffic := PG
    }
  }

  io.timer := cntReg
  
  
  

}