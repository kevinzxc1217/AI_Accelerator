package aias_lab5.Hw4

import chisel3._
import chisel3.iotesters.{Driver,PeekPokeTester}

import opcode_map._

class DecoderTest(dut:Decoder) extends PeekPokeTester(dut){
    
    def extend (bit:Int,extend:Int):Int = {
        val seq = Seq.fill(extend)(bit)
        seq.reduce(2*_+_) << (32 - extend)
    }

    var pass = 0
    
    for(i <- 0 until 20){
        var inst = rnd.nextInt(1<<24) << 8 
        //theoretically should left shift 7 but 8 can make inst[31] have more possibility to be 1
        var inst_31 = (inst >> 31) & 0x1
        var inst_30_25 = (inst >> 25) & 0x3f
        var inst_24_21 = (inst >> 21) & 0xf
        var inst_20 = (inst >> 20) & 0x1
        var inst_11_8 = (inst >> 8) & 0xf
        var inst_7 = (inst >> 7) & 0x1
        var inst_30_20 = (inst >> 20) & 0x7ff
        var inst_19_12 = (inst >> 12) & 0xff

        // println("inst:"+inst.toString)
        // println("inst_31:"+inst_31.toString)
        // println("inst_30_25:"+inst_30_25.toString)
        // println("inst_24_21:"+inst_24_21.toString)
        // println("inst_20:"+inst_20.toString)
        // println("inst_11_8:"+inst_11_8.toString)
        // println("inst_7:"+inst_7.toString)
        // println("inst_30_20:"+inst_30_20.toString)
        // println("inst_19_12:"+inst_19_12.toString)

        var I_imm = extend(inst_31,21) + (inst_30_25 << 5) + (inst_24_21 << 1) + inst_20
        var S_imm = extend(inst_31,21) + (inst_30_25 << 5) + (inst_11_8 << 1) + inst_7
        var B_imm = extend(inst_31,20) + (inst_7 << 11) + (inst_30_25 << 5) + (inst_11_8 << 1)
        var U_imm = (inst_31 << 31) + (inst_30_20 << 20) + (inst_19_12 << 12)
        var J_imm = extend(inst_31,12) + (inst_19_12 << 12) + (inst_20 << 11) + (inst_30_25 << 5) + (inst_24_21 << 1)

        // println("I_imm = " + I_imm.toString)
        // println("S_imm = " + S_imm.toString)
        // println("B_imm = " + B_imm.toString)
        // println("U_imm = " + U_imm.toString)
        // println("J_imm = " + J_imm.toString)
        
        
        poke(dut.io.inst, inst + LOAD.litValue())
        if(!expect(dut.io.imm,I_imm)){
            println("LOAD test failed!")
            pass+=1
        }
        step(1)
        
        poke(dut.io.inst, inst + STORE.litValue())
        if(!expect(dut.io.imm,S_imm)){
            println("STORE test failed!")
            pass+=1
        }
        step(1)
        
        poke(dut.io.inst, inst + BRANCH.litValue())
        if(!expect(dut.io.imm,B_imm)){
            println("BRANCH test failed!")
            pass+=1
        }
        step(1)
        
        poke(dut.io.inst, inst + JALR.litValue())
        if(!expect(dut.io.imm,I_imm)){
            println("JALR test failed!")
            pass+=1
        }
        step(1)
        
        poke(dut.io.inst, inst + JAL.litValue())
        if(!expect(dut.io.imm,J_imm)){
            println("JAL test failed!")
            pass+=1
        }
        step(1)
        
        poke(dut.io.inst, inst + OP_IMM.litValue())
        if(!expect(dut.io.imm,I_imm)){
            println("OP_IMM test failed!")
            pass+=1
        }
        step(1)
        
        poke(dut.io.inst, inst + OP.litValue())
        if(!expect(dut.io.imm,0)){
            println("OP test failed!")
            pass+=1
        }
        step(1)
        
        poke(dut.io.inst, inst + AUIPC.litValue())
        if(!expect(dut.io.imm,U_imm)){
            println("AUIPC test failed!")
            pass+=1
        }
        step(1)
        
        poke(dut.io.inst, inst + LUI.litValue())
        if(!expect(dut.io.imm,U_imm)){
            println("LUI test failed!")
            pass+=1
        }
        step(1)
    }

    if(pass == 0)
        println("Decoder test completed!!!!!")
    else
        println(s"Decoder test failed...you have ${pass} errors to fix")
}

object DecoderTest extends App{
    Driver.execute(args,()=>new Decoder()){
        c:Decoder => new DecoderTest(c)
    }
}