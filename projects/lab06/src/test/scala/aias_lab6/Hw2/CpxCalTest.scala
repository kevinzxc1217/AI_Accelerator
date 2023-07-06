package aias_lab6.Hw2

import chisel3.iotesters.{Driver,PeekPokeTester}
import scala.language.implicitConversions

class CpxCalTest(dut:CpxCal) extends PeekPokeTester(dut){

    val dict = Map(
      '0' -> 0,
      '1' -> 1,
      '2' -> 2,
      '3' -> 3,
      '4' -> 4,
      '5' -> 5,
      '6' -> 6,
      '7' -> 7,
      '8' -> 8,
      '9' -> 9,
      '+' -> 10,
      '-' -> 11,
      '*' -> 12,
      '(' -> 13,
      ')' -> 14,
      '=' -> 15
    )
    
    val golden = Seq(("5+3*4=",17),
                     ("30+40=",70),
                     ("30-40=",-10),
                     ("20*20=",400),
                     ("(-123)=",-123),
                     ("(-10)+11+12-(-13)+(-14)=",12),
                     ("((-15)+(-10))*12-(34+66)*(-4)=",100))
                     //you can add your own formular for testing
    
    golden.zipWithIndex.foreach{ case((input,output),index)=>
        input.foreach{ ch =>
            poke(dut.io.key_in,dict(ch))
            step(1)
        }
        while(peek(dut.io.value.valid) == 0){
            step(1)
        }
        // assert(peek(dut.io.value.bits).toInt == output)
        println("Question"+(index+1).toString+": "+input)
        println("the output of module is :" + peek(dut.io.value.bits).toInt)
        println("the correct answer is :" + output)
        println(if(peek(dut.io.value.bits).toInt==output) "Correct" else "Wrong")
        println("==========================================")
        step(1)
    }
}

object CpxCalTest extends App{
    Driver.execute(args,()=>new CpxCal){
        c:CpxCal => new CpxCalTest(c)
    }
}