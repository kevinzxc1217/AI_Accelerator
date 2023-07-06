package aias_lab9.Memory

import chisel3._
import chisel3.util._
import chisel3.util.experimental.loadMemoryFromFile

import aias_lab9.AXILite._

class DataMem(size: Int, addrWidth: Int, dataWidth: Int, binaryFile: String) extends Module {
  val io = IO(new Bundle {
    val bus_slave = new AXILiteSlaveIF(addrWidth, dataWidth)
	val dm_addr = Input(UInt(addrWidth.W))
	val dm_data = Output(UInt(dataWidth.W))
  })
  


  /*
    state declaration
    1. sIdle
    2. sReadResp -> send readResp to the bus
    2. sWriteResp -> send writeResp to the bus
   */
  val sIdle :: sReadResp :: sWriteResp :: Nil = Enum(3)
  // state reg
  val stateReg = RegInit(sIdle)
  
  val memory = Mem((1 << (size)), UInt(8.W))

  // pre-load data into mem
  loadMemoryFromFile(memory, binaryFile)
  
  // memory read

  
  
  // Next state decoder
  switch(stateReg) {
    //判斷要轉什麼階段
    is(sIdle) {
	  //若readAddr.valid 則轉 sReadResp
      when(io.bus_slave.readAddr.valid) {
        stateReg := sReadResp
	  //若writeAddr.valid 則轉 sWriteResp
      }.elsewhen(io.bus_slave.writeAddr.valid & io.bus_slave.writeData.valid) {
        stateReg := sWriteResp
	  // 否則不轉
      }.otherwise {
        stateReg := sIdle
      }
    }
	// 當ready為1時回sIdle
    is(sReadResp) {
      stateReg := Mux((io.bus_slave.readData.ready), sIdle, sReadResp)
    }
	// 當ready為1時回sIdle
    is(sWriteResp) {
      stateReg := Mux((io.bus_slave.writeResp.ready), sIdle, sReadResp)
    }
  }

  // AXI slave interface output - ready / valid
  io.bus_slave.readAddr.ready  := false.B
  io.bus_slave.readData.valid  := false.B
  io.bus_slave.writeAddr.ready := false.B
  io.bus_slave.writeData.ready := false.B
  io.bus_slave.writeResp.valid := false.B

  switch(stateReg) {
    is(sIdle) {
      // Idle -> ready to accept request from master
      io.bus_slave.readAddr.ready  := true.B
      io.bus_slave.writeAddr.ready := true.B
      io.bus_slave.writeData.ready := true.B
    }
    is(sReadResp) {
      // read request done -> set io.bus_slave.readData.valid to HIGH
      io.bus_slave.readData.valid := true.B
    }
    is(sWriteResp) {
      // write request done -> set io.bus_slave.writeResp.valid to HIGH
      io.bus_slave.writeResp.valid := true.B
    }
  }

  // Handle request
  val addrReg = RegInit(0.U(addrWidth.W))
  

  
  
  // memory write
  switch(stateReg) {
    is(sIdle) {
	  //addrReg給值
      addrReg := Mux(io.bus_slave.readAddr.valid, io.bus_slave.readAddr.bits.addr - "h8000".U, addrReg)
      // writeAddr和 writeData valid
	  when(io.bus_slave.writeAddr.valid & io.bus_slave.writeData.valid) {
        // writeAddr給值
		for (i <- 0 until (dataWidth / 8)) {
          memory((io.bus_slave.writeAddr.bits.addr - "h8000".U) + i.U) := Mux(
            // strb當判斷
			(io.bus_slave.writeData.bits.strb(i) === 1.U),
            // slave找
			io.bus_slave.writeData.bits.data(8 * (i + 1) - 1, 8 * i),
            // memory找
			memory((io.bus_slave.writeAddr.bits.addr - "h8000".U) + i.U)
          )
        }
      }
    }
    is(sReadResp) {
      addrReg := addrReg
    }
    is(sWriteResp) {
      addrReg := addrReg
    }
  }
  //將2個數字一行組合起來成一個readData
  io.bus_slave.readData.bits.data := Cat(
    memory(addrReg + 7.U),
    memory(addrReg + 6.U),
    memory(addrReg + 5.U),
    memory(addrReg + 4.U),
    memory(addrReg + 3.U),
    memory(addrReg + 2.U),
    memory(addrReg + 1.U),
    memory(addrReg)
  )
  io.bus_slave.readData.bits.resp := 0.U
  io.bus_slave.writeResp.bits     := 0.U
  
  
    io.dm_data := Cat(
    memory(io.dm_addr + 7.U),
    memory(io.dm_addr + 6.U),
    memory(io.dm_addr + 5.U),
    memory(io.dm_addr + 4.U),
    memory(io.dm_addr + 3.U),
    memory(io.dm_addr + 2.U),
    memory(io.dm_addr + 1.U),
    memory(io.dm_addr)
  )
  
}
