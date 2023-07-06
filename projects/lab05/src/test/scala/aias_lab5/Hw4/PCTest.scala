package aias_lab5.Hw4

import chisel3._
import chisel3.iotesters.{Driver,PeekPokeTester}

class PCTest(dut:PC) extends PeekPokeTester(dut){
    var pass = 0
    val x = rnd.nextInt(1<<5)
    step(x)
    assert(peek(dut.io.pc)==4*x)
    
    val lst = Seq(true,false)
    
    for(brtaken <- lst){
        for(jmptaken <- lst){
            poke(dut.io.brtaken,brtaken)
            poke(dut.io.jmptaken,jmptaken)
            poke(dut.io.offset,33)
            val pc_now = peek(dut.io.pc).toInt
            //println("pc_now = " + pc_now.toString)
            step(1)
            if(!expect(dut.io.pc, if(brtaken||jmptaken) 32 else (pc_now+4))){
                pass = pass + 1
            }
        }
    }

    if(pass == 0)
        println("PC test completed!!!!!")
    else
        println(s"PC test failed...you have ${pass} errors to fix")
}

object PCTest extends App{
    Driver.execute(args,()=>new PC()){
        c:PC => new PCTest(c)
    }
}