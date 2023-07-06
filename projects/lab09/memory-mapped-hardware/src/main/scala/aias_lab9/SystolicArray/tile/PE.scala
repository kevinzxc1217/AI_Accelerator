package aias_lab9.SystolicArray

import chisel3._
import chisel3.util._

/** PE module
  *
  * building block for tile
  *
  * @param bits
  *   data width in PE (default by 1 byte)
  */
class PE(val bits: Int = 8) extends Module {
  val io = IO(new Bundle {
    // input propagation (fwd: forwarded)
    val input     = Input(Valid(UInt(bits.W)))
    val fwd_input = Output(Valid(UInt(bits.W)))

    // weight propagation
    val weight     = Input(Valid(UInt(bits.W)))
    val fwd_weight = Output(Valid(UInt(bits.W)))

    val preload = Input(Bool())

    // partial sum propagation (ps is short for partial sum)
    val ps     = Input(UInt((bits * 2).W))
    val fwd_ps = Output(Valid(UInt((bits * 2).W)))
  })

  // weightReg includes 2 parts: bits and valid
  val weightReg = RegInit({
    val init = Wire(Valid(UInt(bits.W)))
    init := DontCare
    init // return
  })

  // internal weight register (bits and valid)
  weightReg.bits  := Mux(io.preload, io.weight.bits, weightReg.bits)
  weightReg.valid := Mux(io.preload, io.weight.valid, weightReg.valid)

  // output
  io.fwd_weight.bits  := weightReg.bits
  io.fwd_weight.valid := weightReg.valid
  
  io.fwd_input <> RegNext(io.input)

  io.fwd_ps.valid := RegNext(io.input.valid)
  // fwd_ps乘積
  io.fwd_ps.bits  := RegNext(io.ps + weightReg.bits * io.input.bits)
}

object PETop extends App {
  (new chisel3.stage.ChiselStage).emitVerilog(
    new PE(8),
    Array("-td", "generated/PE")
  )
}
