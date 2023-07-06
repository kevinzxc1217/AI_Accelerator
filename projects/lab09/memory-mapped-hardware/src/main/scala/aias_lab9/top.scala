package aias_lab9.ontop

import chisel3._
import chisel3.util._

import aias_lab9.SystolicArray._
import aias_lab9.Memory._
import aias_lab9.AXILite._

object config {
  val addr_width     = 32
  val data_width     = 64
  val reg_width     = 32
  //addr????????????
  val addr_map       = List(("h8000".U, "h10000".U),("h100000".U, "h2FFFFF".U))
  val data_mem_size  = 16 // power of 2 in byte
  val data_hex_path  = "src/main/resource/SRAM.hex"
}

import config._

class top(addr_width:Int, data_width:Int) extends Module {
  val io = IO(new Bundle {
	val finish = Output(UInt(1.W))
	val dm_addr = Input(UInt(addr_width.W))
	val dm_data = Output(UInt(data_width.W))
    val ENABLE    = Input(Bool())
  })
  
  val topsa  = Module(new topSA(addr_width, data_width, reg_width))
  val dm  = Module(new DataMem(data_mem_size, addr_width, data_width, data_hex_path))
  val bus = Module(new AXILiteXBar(1, 2, addr_width, data_width, addr_map))
  
  topsa.io.ENABLE := io.ENABLE
  io.finish := topsa.io.finish
  dm.io.dm_addr := io.dm_addr
  io.dm_data := dm.io.dm_data
  
  
  
  // AXI Lite Bus
  
  bus.io.slaves(1) <> topsa.io.slave
  bus.io.masters(0) <> topsa.io.master
  bus.io.slaves(0) <> dm.io.bus_slave
}

