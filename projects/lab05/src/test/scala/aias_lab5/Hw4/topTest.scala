package aias_lab5.Hw4

import scala.io.Source
import chisel3.iotesters.{PeekPokeTester,Driver}

class topTest(dut:top) extends PeekPokeTester(dut){
    val filename = "./src/main/resource/InstMem_ass.txt"
    for(line <- Source.fromFile(filename).getLines){
        println("Inst:"+line)
        println("PC:" + peek(dut.io.pc_out).toString)
        println("ALU out:" + peek(dut.io.alu_out).toInt.toString)
        println("WBdata:" + peek(dut.io.rf_wdata_out).toInt.toString)
        println("Br taken:" + peek(dut.io.brtaken_out).toString)
        println("Jmp taken:" + peek(dut.io.jmptaken_out).toString)
        println("==============================")
        step(1)
    }
}

object topTest extends App{
    Driver.execute(args,()=>new top){
        c:top => new topTest(c)
    }
}