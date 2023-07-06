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

class SA_master(addr_width: Int, data_width: Int) extends Module {
  val io = IO(new Bundle {
	
	val bus_master = new AXILiteMasterIF(32, 64)
	
    val raddr = Input(UInt(addr_width.W))
    val rdata = Output(UInt(data_width.W))
    val wen   = Input(Bool())
    val waddr = Input(UInt(addr_width.W))
    val wdata = Input(UInt(data_width.W))
    val wstrb = Input(UInt((data_width >> 3).W))
	
	val finish= Output(Bool())
	val raddr_valid = Output(Bool())
    val rdata_valid = Output(Bool())
	val raddr_prepare = Input(Bool())
	val wdata_valid = Output(Bool())
	val wresp_valid = Output(Bool())
  })
 
  // 需改
  io.finish := false.B
  
  //新增
  io.bus_master.readAddr.valid      := false.B
  io.rdata := io.bus_master.readData.bits.data
  io.raddr_valid := io.bus_master.readAddr.valid 
  io.rdata_valid := io.bus_master.readData.valid 
  io.wdata_valid := io.bus_master.writeData.valid
  io.wresp_valid := io.bus_master.writeResp.valid
  
  //io.bus_master.readAddr.bits.addr  := io.raddr+"h8000".U
  io.bus_master.readAddr.bits.addr  := io.raddr
  io.bus_master.readData.ready      := false.B
  io.bus_master.writeAddr.valid     := false.B
  //io.bus_master.writeAddr.bits.addr := io.waddr+"h8000".U
  io.bus_master.writeAddr.bits.addr := io.waddr
  io.bus_master.writeData.valid     := false.B
  io.bus_master.writeData.bits.data := io.wdata
  io.bus_master.writeData.bits.strb := io.wstrb
  io.bus_master.writeResp.ready     := false.B
  
  
  
  val sNormal :: sAXIReadSend :: sAXIReadWait :: sAXIWriteSend :: sAXIWriteWait :: Nil = Enum(5)
  
  val DataMemAccessState = RegInit(sNormal)
  val isDataLoad  = Wire(Bool())
  val isDataStore = Wire(Bool())
  
  // change ???????????????????
  isDataLoad  := io.raddr_prepare
  isDataStore := io.wen
  
  
  //轉state
  switch(DataMemAccessState) {
    is(sNormal) {
	  // sAXIReadWait or sAXIReadSend
      when(isDataLoad) {
        DataMemAccessState := Mux(io.bus_master.readAddr.ready, sAXIReadWait, sAXIReadSend)
      // sAXIWriteWait or sAXIWriteSend 
	  }.elsewhen(isDataStore) {
        DataMemAccessState := Mux((io.bus_master.writeAddr.ready & io.bus_master.writeData.ready), sAXIWriteWait, sAXIWriteSend)
      // sNormal
	  }.otherwise {
        DataMemAccessState := sNormal
      }
    }
	// sAXIReadWait or sAXIReadSend
    is(sAXIReadSend) {
      DataMemAccessState := Mux(io.bus_master.readAddr.ready, sAXIReadWait, sAXIReadSend)
    }
	// sNormal or sAXIReadWait
    is(sAXIReadWait) {
      DataMemAccessState := Mux(io.bus_master.readData.valid, sNormal, sAXIReadWait)
	}
	// sAXIWriteWait or sAXIWriteSend
    is(sAXIWriteSend) {
      DataMemAccessState := Mux((io.bus_master.writeAddr.ready & io.bus_master.writeData.ready), sAXIWriteWait, sAXIWriteSend)
    }
	// sNormal or sAXIWriteWait
    is(sAXIWriteWait) {
      DataMemAccessState := Mux(io.bus_master.writeResp.valid, sNormal, sAXIWriteWait)
    }
  }
  
  

  switch(DataMemAccessState) {
    //初始化訊號
    is(sNormal) {
      io.bus_master.readAddr.valid  := isDataLoad
      io.bus_master.writeAddr.valid := (isDataStore & io.bus_master.writeAddr.ready & io.bus_master.writeData.ready)
      io.bus_master.writeData.valid := (isDataStore & io.bus_master.writeAddr.ready & io.bus_master.writeData.ready)
    }
	// 先ready後valid
    is(sAXIReadSend) {
      io.bus_master.readAddr.valid := Mux(io.bus_master.readAddr.ready, true.B, false.B)
    }
	// 先wait後ready
    is(sAXIReadWait) {
      io.bus_master.readData.ready := true.B
    }
	// 同read
    is(sAXIWriteSend) {
      io.bus_master.writeAddr.valid := (isDataStore & io.bus_master.writeAddr.ready & io.bus_master.writeData.ready)
      io.bus_master.writeData.valid := (isDataStore & io.bus_master.writeAddr.ready & io.bus_master.writeData.ready)
    }
	// 同read
    is(sAXIWriteWait) {
      io.bus_master.writeResp.ready := true.B
    }
  }	

  

}