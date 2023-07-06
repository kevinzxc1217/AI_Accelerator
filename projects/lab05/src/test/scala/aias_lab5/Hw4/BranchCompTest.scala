package aias_lab5.Hw4

import chisel3._
import chisel3.iotesters.{Driver,PeekPokeTester}

import condition._

class BranchCompTest(dut:BranchComp) extends PeekPokeTester(dut){
    
    def geu (src1:Int,src2:Int):Boolean = {
        val sign_1 = (src1 >> 31) & 1
        val sign_2 = (src2 >> 31) & 1

        if(sign_1 == sign_2){
            return (src1 >= src2)
        }else{
            if(sign_1 == 1){
                return true
            }else{
                return false
            }
        }
    }

    var pass = 0

    for(i <- 0 until 20){
        var src1 = rnd.nextInt(1<<30) << 2 
        var src2 = rnd.nextInt(1<<30) << 2 

        for (en <- Seq(true,false)){
            poke(dut.io.en,en)
            poke(dut.io.src1,src1)
            poke(dut.io.src2,src2)
            //======================

            if(en == false){
                if(!expect(dut.io.brtaken,false)){pass+=1}
                step(1)
            }else{
                poke(dut.io.funct3, EQ.litValue())
                if(!expect(dut.io.brtaken,if(src1==src2) true else false)){pass+=1}
                step(1)

                poke(dut.io.funct3, NE.litValue())
                if(!expect(dut.io.brtaken,if(src1!=src2) true else false)){pass+=1}
                step(1)

                poke(dut.io.funct3, LT.litValue())
                if(!expect(dut.io.brtaken,if(src1<src2) true else false)){pass+=1}
                step(1)

                poke(dut.io.funct3, GE.litValue())
                if(!expect(dut.io.brtaken,if(src1>=src2) true else false)){pass+=1}
                step(1)

                poke(dut.io.funct3, LTU.litValue())
                if(!expect(dut.io.brtaken,if(!geu(src1,src2)) true else false)){
                    pass+=1
                }
                step(1)

                poke(dut.io.funct3, GEU.litValue())
                if(!expect(dut.io.brtaken,if(geu(src1,src2)) true else false)){
                    pass+=1
                }
                step(1)
            }
        }
    }

    if(pass == 0)
        println("BranchComp test completed!!!!!")
    else
        println(s"BranchComp test failed...you have ${pass} errors to fix")
}

object BranchCompTest extends App{
    Driver.execute(args,()=>new BranchComp()){
        c:BranchComp => new BranchCompTest(c)
    }
}