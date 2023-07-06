package aias_lab9.SystolicArray

import chisel3._
import chisel3.util._

import scala.io.Source

import aias_lab9.AXILite._

/** SA module includes a tile, two buffers and control logic
  * @param rows
  *   the number of row of SA
  * @param cols
  *   the number of col of SA
  * @param addr_width
  *   the bit width of address
  * @param data_width
  *   the bit width of data
  * @param reg_width
  *   the data width of MMIO regs
  */

class SA(rows: Int, cols: Int, addr_width: Int, data_width: Int, reg_width: Int) extends Module {
  val io = IO(new Bundle {
    // connect to Memory_Mapped
    val mmio = Flipped(new MMIO(reg_width))

    // for access localmem when SA still be a slave
    val raddr = Output(UInt(addr_width.W))
    val rdata = Input(UInt(data_width.W))
	// 是否可寫入buffer的output
    val wen   = Output(Bool())
	//sa全算完後的addr
    val waddr = Output(UInt(addr_width.W))
	//sa全算完後的輸出
    val wdata = Output(UInt(data_width.W))
	// masking
    val wstrb = Output(UInt((data_width >> 3).W))
	
	//新增
	val finish = Output(UInt(1.W))
	val raddr_valid = Input(Bool())
	val rdata_valid = Input(Bool())
	val wdata_valid = Input(Bool())
	val wresp_valid = Input(Bool())
	val raddr_prepare = Output(UInt(1.W))
	val ENABLE =  Input(Bool())
	
  })
  // test
  io.mmio.ENABLE := io.ENABLE
  io.finish := io.mmio.STATUS_OUT
  io.wen := false.B
  
  
  io.raddr_prepare := false.B
  //when(stateReg === sPreload){
  //io.raddr_prepare := Mux(io.rdata_valid && weight_cnt <=3.U,true.B,false.B)
  //}
 
 
  
  // constant declaration
  val byte    = 8
  val mat_buf = 0x000000 // 0 because the localMem is still local for SA
  // information from MMIO_Regfile
  val mat_a_rows = io.mmio.MATA_SIZE(11, 0) + 1.U
  val mat_a_cols = io.mmio.MATA_SIZE(27, 16) + 1.U
  val mat_b_rows = io.mmio.MATB_SIZE(11, 0) + 1.U
  val mat_b_cols = io.mmio.MATB_SIZE(27, 16) + 1.U
  val mat_c_rows = io.mmio.MATC_SIZE(11, 0) + 1.U
  val mat_c_cols = io.mmio.MATC_SIZE(27, 16) + 1.U

  // base address wires (for read/write internal mem)
  val a_base_addr = WireDefault(io.mmio.MATA_MEM_ADDR)
  val b_base_addr = WireDefault(io.mmio.MATB_MEM_ADDR)
  val c_base_addr = WireDefault(io.mmio.MATC_MEM_ADDR)

  // other wires
  //val word_writeData = WireDefault(0.U(32.W))
  val word_writeData = RegInit(VecInit(Seq.fill(rows)(0.U(32.W))))
  
  
  // module declaration
  val tile          = Module(new tile(rows, cols, byte)) // single tile in our SA design
  val input_buffer  = Module(new buffer(rows, byte))
  val output_buffer = Module(new buffer(cols, byte))

  // state declaration
  // sStall_1 & sStall_2 is due to the timing of SyncReadMem
  val sIdle ::   sPreload :: sPropagate :: sCheck ::sFinish :: Nil = Enum(5)
  // state register
  val stateReg = RegInit(sIdle)

  // declare counters
  val weight_cnt = RegInit(rows.U(3.W))
  val input_cnt  = RegInit(0.U(3.W))
  val output_cnt = RegInit(0.U(3.W))

 
  
  // use for select partial read data
  val half_rdata   = Wire(UInt((data_width >> 1).W))
  val rdata_select = RegNext(io.raddr)
  val rdata_low   = RegNext(io.rdata)
  
  
  
  
  val test = RegInit(0.U(2.W))
  when(io.rdata_valid){
    //rdata_valid後一個clock
	test := 1.U
  }.elsewhen(test === 1.U){
  //rdata_valid後兩個clock
	test := 2.U
  }.otherwise{
	test := 0.U
  }
  
  // select higher half or lower half of io.data
  // 選擇讀取前或後半段

  half_rdata := Mux(
    //rdata_select(2) === 0.U,
	io.rdata_valid === false.B,
	rdata_low((data_width >> 1) - 1, 0),
	io.rdata(data_width - 1, data_width >> 1)
  )

  // wiring io.rdata <---> tile.io.weight and tile.io.preload
  // 若現在為sPreload階段，則將值放在tile的weight
  //tile.io.preload := 
  tile.io.preload := (weight_cnt ===3.U || weight_cnt ===1.U) && (test === 1.U || io.rdata_valid)

  List.range(0, cols).map { index =>
    tile.io.weight(index).bits  := half_rdata(byte * (index + 1) - 1, byte * index)
    tile.io.weight(index).valid := (weight_cnt ===3.U || weight_cnt ===1.U) && (test === 1.U || io.rdata_valid)
    
  }
  

  // wiring io.rdata <---> input_buffer.io.input
  // 將值放在input_buffer
  List.range(0, rows).map { index =>
    input_buffer.io.input(index).bits  := half_rdata(byte * (index + 1) - 1, byte * index)
    input_buffer.io.input(index).valid := ((stateReg === sPropagate && input_cnt ===0.U) || input_cnt ===2.U) && (test === 1.U || io.rdata_valid)
    //input_buffer.io.input(index).valid := 
  }

  // wiring input_buffer.io.output <---> tile.io.input
  // tile input和input_buffer相接
  // buffer是為了讓初始矩陣擺出我們要的樣子
  tile.io.input <> input_buffer.io.output

  // wiring tile.io.output <---> output_buffer.io.input (notice the order of wiring)
  //tile output和output_buffer相接 
  List.range(0, cols).map { index =>
    output_buffer.io.input(cols - 1 - index) <> tile.io.output(index)
  }

  // assign io.raddr and io.waddr
  // 將a和b的矩陣addr存到raddr
  //io.raddr := 0.U
  

  val pre_addr   = RegNext(io.raddr)

  io.raddr := Mux(
    stateReg === sPreload,// sPreload or sPropagate
	//Mux((weight_cnt-1.U) <<2 ===8.U || (weight_cnt-1.U) <<2 === 0.U && io.rdata_valid,
	Mux(io.raddr_valid,
	b_base_addr+((weight_cnt-1.U) <<2) ,
    pre_addr
    ),
	//Mux( input_cnt<<1 ===8.U || input_cnt <<1 === 0.U,
	Mux(io.raddr_valid,
	a_base_addr+(input_cnt <<2) ,
    pre_addr
    )
	
  )
  
  
  
  
  // 計算出來的值存到waddr
  val pre_waddr   = RegNext(io.waddr)
  
  io.waddr := Mux(output_cnt === 0.U || output_cnt === 2.U,c_base_addr + (output_cnt << 2),pre_waddr)
  //io.waddr := c_base_addr + (output_cnt << 2)
  // assign word_writeData and io.wdata, io.wstrb and io.wen
  
  // 將output_buffer的所有值累加到word_writeData內
  
  
  
 
 val word_index = RegInit(0.U(3.W))
 

 
 when((input_cnt === 2.U || input_cnt === 4.U) && test ===0.U){
	word_index := word_index + 1.U
	word_writeData(word_index) := List
    .range(0, cols)
    .map { index => output_buffer.io.output(index).bits << byte * (cols - 1 - index) }
    .reduce(_ + _)
	}
	
	
  val wdata_buffer1 = RegInit(0.U(64.W))
  val wdata_buffer2 = RegInit(0.U(64.W))
  
  
  when(word_writeData(1)>0.U){
  wdata_buffer1 := Cat(word_writeData(0),word_writeData(1))
  }
  
  when(word_writeData(3)>0.U){
  wdata_buffer2 := Cat(word_writeData(2),word_writeData(3))
  }

  
  //由於word_writeData只有data_widwh的一半，這裡##來做連接前或後半段
  
 
   
  
  val pre_wdata   = RegNext(io.wdata)
  
  val word_index2 = RegInit(0.U(3.W))
  when(io.wdata_valid){word_index2 := word_index2+1.U}
  
  io.wdata := Mux(
	io.wdata_valid ,
    Mux(
		output_cnt === 0.U || output_cnt === 1.U,
		wdata_buffer1,
		wdata_buffer2
		),
	pre_wdata
	)
  
  // Write data 的部分, 如果有少於data bus width 寬度的, 
  // 要用WSTRB 的設定來做masking
  // 前半段或後半段data
  io.wstrb := Mux(output_cnt(0) === 0.U, "b00001111".U, "b11110000".U)
  // 是否可寫入output
  //io.wen   := output_buffer.io.output(0).valid
  
  // assign io.mmio output signals
  // 全部完成時，將mmio內的enable_in設為0，status_in和wen設為1
  io.mmio.ENABLE_IN := !(stateReg === sFinish) // pull down ENABLE when operation is done
  io.mmio.STATUS_IN := stateReg === sFinish    // pull up STATUS when operation is done
  io.mmio.WEN       := stateReg === sFinish    // write MMIO regs when operation is done



  // * next state logic
  switch(stateReg) {
    // 初始
    is(sIdle) {
		when(io.mmio.ENABLE_OUT) {
		  stateReg := sPreload
		  io.raddr_prepare := true.B
		}
	}

	// 當weight載入完後，換stall_2
    is(sPreload) {
      when(io.mmio.ENABLE_OUT) {
        //stateReg := Mux(weight_cnt === 0.U && io.rdata_valid, sPropagate, sStall_1)
		when(test ===2.U){
			io.raddr_prepare := true.B
			}
		stateReg := Mux(weight_cnt === 0.U&& test ===1.U, sPropagate, sPreload)
	  }
    }
	
	// 全走完後到sCheck
    is(sPropagate) {
      when(io.mmio.ENABLE_OUT) {
		when(test ===2.U){
			io.raddr_prepare := true.B
			}
	    //io.raddr_prepare := true.B
        stateReg := Mux((input_cnt === cols.U && test ===1.U) , sCheck, sPropagate)
      }
    }
	// 確定output計數是否真的走完
    is(sCheck) {
      stateReg := Mux(output_cnt === (rows - 1).U && io.wdata_valid, sFinish, sCheck)
	  io.wen := true.B
    }
	
	// 重置
    is(sFinish) {
	  when(!io.mmio.ENABLE_OUT) {
		  stateReg := sIdle
		  io.mmio.STATUS_IN := true.B
	  }
    }
  }

  // * FSM output decoder
  

   when(stateReg === sPreload) {
	weight_cnt := weight_cnt - Mux( test ===1.U, 1.U, 0.U)
	//io.raddr_prepare := true.B
  // input計數
  }.elsewhen(stateReg === sPropagate) {
    input_cnt  := input_cnt  + Mux( test ===1.U, 1.U, 0.U)
  // output計數，且將output buffer的值陸續寫入
  }.elsewhen(stateReg === sCheck) {
    //output_cnt := output_cnt + Mux(output_buffer.io.output(0).valid, 1.U, 0.U)
    output_cnt := output_cnt + Mux( io.wresp_valid, 1.U, 0.U)
  // 完成後初始化，針對特定 MMIO registers 進行寫入
  }.elsewhen(stateReg === sFinish) {
    // reset counters
    weight_cnt := rows.U
    input_cnt  := 0.U
    output_cnt := 0.U
  }.otherwise {
    // DontCare
  }
}

object SATop extends App {
  (new chisel3.stage.ChiselStage).emitVerilog(
    new SA(4, 4, 32, 64, 32), // rows, cols, addr_width, data_width, reg_width
    Array("-td", "./generated/SA")
  )
}
