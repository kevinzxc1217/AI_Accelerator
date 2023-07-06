package aias_lab9.Arbiter


import chisel3._
import scala.io.Source
import chisel3.iotesters.{PeekPokeTester, Driver}
import scala.language.implicitConversions
import java.io._


class MyArbiterTest(a : MyArbiter) extends PeekPokeTester(a){
  step(5)
  poke(a.io.in(0).bits, 5)
  poke(a.io.in(1).bits, 10)
  poke(a.io.in(2).bits, 15)
  poke(a.io.out.ready, true)

  poke(a.io.in(0).valid, false)
  poke(a.io.in(1).valid, false)
  poke(a.io.in(2).valid, true)
  step(1)

  poke(a.io.in(0).valid, false)
  poke(a.io.in(1).valid, true)
  poke(a.io.in(2).valid, false)
  step(1)

  poke(a.io.in(0).valid, true)
  poke(a.io.in(1).valid, false)
  poke(a.io.in(2).valid, false)
  step(1)

  poke(a.io.in(0).valid, false)
  poke(a.io.in(1).valid, true)
  poke(a.io.in(2).valid, true)
  step(1)

  poke(a.io.in(0).valid, true)
  poke(a.io.in(1).valid, false)
  poke(a.io.in(2).valid, true)
  step(1)

  poke(a.io.in(0).valid, true)
  poke(a.io.in(1).valid, true)
  poke(a.io.in(2).valid, false)
  step(1)

  poke(a.io.in(0).valid, true)
  poke(a.io.in(1).valid, true)
  poke(a.io.in(2).valid, true)
  step(1)

  step(20)

}

object MyArbiterTest extends App{
  Driver.execute(args,() => new MyArbiter()){
    c => new MyArbiterTest(c)
  }
}