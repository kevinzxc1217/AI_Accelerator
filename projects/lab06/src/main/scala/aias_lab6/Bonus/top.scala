package aias_lab6.Bonus

import chisel3._
import chisel3.util._
import aias_lab6.Hw3.NumGuess

class top extends Module{
    val io  = IO(new Bundle{
        val gen = Input(Bool())
        val finish = Output(Bool())
    })

    io.finish := false.B
}