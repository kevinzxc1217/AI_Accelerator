package aias_lab9.SystolicArray

import chisel3._
import chisel3.util._
import chisel3.stage.ChiselStage

/** tile module
  *
  * in our lab or homework
  *
  * systolic array consists of only 1 tile
  */
class tile(rows: Int, cols: Int, bits: Int) extends Module {
  val io = IO(new Bundle {
    val input  = Input(Vec(rows, Valid(UInt(bits.W))))
    val weight = Input(Vec(cols, Valid(UInt(bits.W))))
    // input preload control signal will be connect to every PEs in the tile
    val preload = Input(Bool())
    val output  = Output(Vec(cols, Valid(UInt((2 * bits).W))))
  })

  // sa: systolic array (single tile, [rows x cols] PEs in a tile)
  val sa = Array.fill(rows, cols)(Module(new PE(bits)).io)

  for (i <- 0 until rows) {
    for (j <- 0 until cols) {
      // Wiring: preload (connect io.preload to every PEs in the tile)
      sa(i)(j).preload := io.preload

      // Wiring: input & PE.io.fwd_input
      if (j == 0) {
        // first column
        sa(i)(j).input <> io.input(i)
      } else {
        // not first column
		// 前一個sa的值往後傳
        sa(i)(j).input <> sa(i)(j - 1).fwd_input
      }

      // Wiring: weight (PE.io.fwd_weight) & partial sum (PE.io.fwd_ps)
      if (i == 0) {
        // first row
        sa(i)(j).weight <> io.weight(j)
		// 初始化partial sum
        sa(i)(j).ps := 0.U
      } else {
        // not first row
        sa(i)(j).weight <> sa(i - 1)(j).fwd_weight
        sa(i)(j).ps := sa(i - 1)(j).fwd_ps.bits
      }
    }
  }

  // connect io.output(x) to sa(rows - 1)(x).fwd_ps, the range of x is from 0 to (cols - 1)
  List.range(0, cols).map { x => io.output(x) <> sa(rows - 1)(x).fwd_ps }
}
