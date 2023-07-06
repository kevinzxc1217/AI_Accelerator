package aias_lab9.SystolicArray

import chisel3._
import chisel3.iotesters.{Driver, PeekPokeTester}
import scala.util.Random
import aias_lab9.SystolicArray
import scala.language.implicitConversions

class tileTest(dut: tile) extends PeekPokeTester(dut) {
  implicit def bigint2bool(bi: BigInt) = if (bi != 0) true else false

  def mmul(a: Array[Array[Int]], b: Array[Array[Int]]): Array[Array[Int]] = {
    for (row_vec <- a) yield {
      for (col_vec <- b.transpose) yield {
        (row_vec zip col_vec).map { case (x, y) => x * y }.reduce(_ + _)
      }
    }
  }

  def matInit(n: Int, rseed: Int): Array[Array[Int]] = {
    val maxval = 5
    val rnd    = new scala.util.Random(rseed)
    // randomly generate the n*n matrix, the range of each element is 1~5
    Array.tabulate(n) { _ => Array.tabulate(n) { _ => rnd.nextInt(maxval) + 1 } }
  }

  def printmat(m: Array[Array[Int]]): Unit = {
    m.foreach { r =>
      r.foreach { v => print(f"$v%4d") }
      println()
    }
    println()
  }

  val mat_size = 4
  val rows     = 4
  val cols     = 4
  val a_mat    = matInit(mat_size, 0)
  val b_mat    = matInit(mat_size, 1)
  val c_mat    = mmul(a_mat, b_mat)

  print("==A_mat \n")
  printmat(a_mat)
  print("==B_mat \n")
  printmat(b_mat)
  print("==C_mat \n")
  printmat(c_mat)

  // tester=========================================================

  val check_answer = Array.fill(mat_size)(0)

  for (clk <- 0 until 4 * mat_size) {

    // weight and preload
    for (col <- 0 until cols) {
      if (clk >= 0 && clk < mat_size) {
        poke(dut.io.preload, true)
        poke(dut.io.weight(col).bits, b_mat(rows - clk - 1)(col))
        poke(dut.io.weight(col).valid, true)
      } else {
        poke(dut.io.preload, false)
        poke(dut.io.weight(col).bits, 0)
        poke(dut.io.weight(col).valid, false)
      }
    }

    // propagate the input
    if (clk >= mat_size) {
      val clk_p = clk - mat_size
      for (idx <- 0 until mat_size) {
        val diff_p = clk_p - idx
        if (diff_p >= 0 && diff_p < mat_size) {
          poke(dut.io.input(idx).bits, a_mat(clk_p - idx)(idx))
          poke(dut.io.input(idx).valid, true)
        } else {
          poke(dut.io.input(idx).bits, 0)
          poke(dut.io.input(idx).valid, false)
        }
      }
    }

    //     // // check_answer the correctness of output in the appropriate timing (calculated)
    //     // if(clk >= 2*mat_size){
    //     //     val clk_pp = clk - 2*mat_size
    //     //     for(idx <- 0 until mat_size){
    //     //         val diff_pp = clk_pp - idx
    //     //         if(diff_pp >= 0 && diff_pp < mat_size){
    //     //             expect(dut.io.output(idx).bits,c_mat(diff_pp)(idx))
    //     //             print("Output row: "+idx.toString+" gets "+peek(dut.io.output(idx).bits).toString+"\n")
    //     //         }else{
    //     //             expect(dut.io.output(idx).bits,0)
    //     //             print("Output row: "+idx.toString+" gets 0\n")
    //     //         }
    //     //     }
    //     //     print("============================================\n")
    //     // }

    val someone_valid = Array
      .tabulate(mat_size)(idx => idx)
      .map { idx =>
        peek(dut.io.output(idx).valid)
      }
      .reduce(_ | _)

    if (someone_valid) {
      for (idx <- 0 until mat_size) {
        if (peek(dut.io.output(idx).valid) == 1) {
          expect(dut.io.output(idx).bits, c_mat(check_answer(idx))(idx))
          print("Output row: " + idx.toString + " gets " + peek(dut.io.output(idx).bits).toString + "\n")
          check_answer(idx) = check_answer(idx) + 1
        } else {
          print("Output row: " + idx.toString + " gets 0\n")
        }
      }
      print("============================================\n")
    }
    step(1)
  }
  step(10)
}

object tileTest extends App {
  Driver.execute(args, () => new tile(4, 4, 8)) { c: tile =>
    new tileTest(c)
  }
}
