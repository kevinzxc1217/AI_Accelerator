package aias_lab9.SystolicArray

import chisel3._
import chisel3.util._
import chisel3.util.experimental.loadMemoryFromFile

/** LocalMem module
  *
  * includes internal memory and systolic array, also the control logics
  *
  * @param mem_size
  *   the size of internal mem
  * @param addr_width
  *   bit width of address
  * @param data_width
  *   bit width of data
  */
class LocalMem(mem_size: Int, addr_width: Int, data_width: Int) extends Module {
  val io = IO(new Bundle {
    // from SA(slave) or CPU(master)
    val raddr = Input(UInt(addr_width.W))
    val rdata = Output(UInt(data_width.W))

    val wen   = Input(Bool())
    val waddr = Input(UInt(addr_width.W))
    val wdata = Input(UInt(data_width.W))
    val wstrb = Input(UInt((data_width >> 3).W))
  })
  val byte = 8 // constant

  // use SyncReadMem module from chisel.util
  val localMem = SyncReadMem(mem_size, UInt(data_width.W))
  // preload data from hex file
  loadMemoryFromFile(localMem, "src/main/resource/SystolicArray/LocalMem.hex")

  // * when you execute aias_lab9.topSystolicArray.topTest, please comment line 28, which is about preload LocalMem

  // wires declaration
  // address -> truncate lower 3 bits to transfer byte addr to double-word address
  val raddr_aligned = WireDefault(io.raddr >> 3)
  val waddr_aligned = WireDefault(io.waddr >> 3)
  val wdata_mask    = Wire(Vec(data_width >> 3, UInt(byte.W))) // write data after masked

  wdata_mask := DontCare // avoid compilation error when signal is not fully initialized

  // memory read
  io.rdata := localMem.read(raddr_aligned)

  // memory write
  when(io.wen) {
    // do byte mask
    List.range(0, data_width >> 3).map { index =>
      wdata_mask(index) := Mux(
        io.wstrb(index) === 1.U,
        io.wdata(byte * (index + 1) - 1, byte * index),
        0.U
      )
    }
    // do write
    localMem.write(waddr_aligned, wdata_mask.asTypeOf(UInt(data_width.W)))
  }
}

object LocalMemTop extends App {
  (new chisel3.stage.ChiselStage).emitVerilog(
    new LocalMem(0x8000, 32, 64),
    Array("-td", "generated/LocalMem")
  )
}
