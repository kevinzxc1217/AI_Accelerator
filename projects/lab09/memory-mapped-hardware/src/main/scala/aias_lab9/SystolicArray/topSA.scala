package aias_lab9.SystolicArray

import chisel3._
import chisel3.util._

import aias_lab9.AXILite._

/** topSA module
  *
  * includes one Memory_Mapped module and one SA module
  *
  * pure wiring between these two modules and I/O interface
  */
class topSA(addr_width: Int, data_width: Int, reg_width: Int) extends Module {
  val io = IO(new Bundle {
    // slave interface for connecting to AXI bus
    val slave = new AXILiteSlaveIF(addr_width, data_width)
	// add
	val master = new AXILiteMasterIF(addr_width, data_width)
	val finish = Output(Bool())
	val ENABLE    = Input(Bool())
  })

  // module declaration
  val sa = Module(new SA(4, 4, addr_width, data_width, reg_width))
  
  val mm = Module(new Memory_Mapped(0x8000, addr_width, data_width, reg_width))
  
  val sa_master  = Module(new SA_master(32,64))
  
  sa.io.ENABLE := io.ENABLE
  
  //新增
  io.finish := sa.io.finish
  sa_master.io.raddr_prepare := sa.io.raddr_prepare
  sa.io.wdata_valid := sa_master.io.wdata_valid
  sa.io.raddr_valid := sa_master.io.raddr_valid
  sa.io.rdata_valid := sa_master.io.rdata_valid
  sa.io.wresp_valid := sa_master.io.wresp_valid
  
   // module wiring
  io.slave <> mm.io.slave  
  mm.io.mmio <> sa.io.mmio

  io.master <> sa_master.io.bus_master
  
  sa_master.io.raddr := sa.io.raddr
  //sa_master.io.rdata := sa.io.rdata
  sa.io.rdata := sa_master.io.rdata
  sa_master.io.waddr := sa.io.waddr
  sa_master.io.wdata := sa.io.wdata
  sa_master.io.wstrb := sa.io.wstrb
  sa_master.io.wen := sa.io.wen  
  


  
  // for internal buffer (local mem) >>>>>
  //mm.io.raddr <> sa.io.raddr
  //mm.io.rdata <> sa.io.rdata
  //mm.io.waddr <> sa.io.waddr
  //mm.io.wdata <> sa.io.wdata
  //mm.io.wstrb <> sa.io.wstrb
  //mm.io.wen <> sa.io.wen
  
  
  
}
