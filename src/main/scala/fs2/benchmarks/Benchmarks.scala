package fs2.benchmarks

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import fs2.*
import org.openjdk.jmh.annotations.*

import java.util.concurrent.TimeUnit

@State(Scope.Thread)
//@Warmup(iterations = 1, time = 10, timeUnit = TimeUnit.SECONDS)
//@Measurement(iterations = 1, time = 10, timeUnit = TimeUnit.SECONDS)
//@Fork(value = 1, warmups = 1)
class Benchmarks {

  @Benchmark
  def streamParJoinUnbounded(): Unit = {
    val list = List.fill(1024)(Stream.eval(IO.unit))
    Stream.emits(list).parJoinUnbounded.compile.last.unsafeRunSync().get
  }

  @Benchmark
  def listParJoinUnbounded(): Unit = {
    val list = List.fill(1024)(Stream.eval(IO.unit))
    list.parJoinUnbounded.compile.last.unsafeRunSync().get
  }

  @Benchmark
  def merge2(): Int = {
    val s = Stream.eval(IO.pure(0)).repeatN(1024)

    s.merge(s).compile.last.unsafeRunSync().get
  }

  @Benchmark
  def listParJoin2(): Int = {
    val s = Stream.eval(IO.pure(0)).repeatN(1024)

    List(s, s).parJoinUnbounded.compile.last.unsafeRunSync().get
  }

  @Benchmark
  def streamParJoin2(): Int = {
    val s = Stream.eval(IO.pure(0)).repeatN(1024)

    Stream(s, s).parJoinUnbounded.compile.last.unsafeRunSync().get
  }

  @Benchmark
  def mergeWithEmpty(): Int = {
    val s1 = Stream.eval(IO.pure(0)).repeatN(1024)
    val s2 = Stream.empty

    s1.merge(s2).compile.last.unsafeRunSync().get
  }

  @Benchmark
  def streamParJoinWithEmpty(): Int = {
    val s1 = Stream.eval(IO.pure(0)).repeatN(1024)
    val s2 = Stream.empty

    Stream(s1, s2).parJoinUnbounded.compile.last.unsafeRunSync().get
  }

  @Benchmark
  def listParJoinWithEmpty(): Int = {
    val s1 = Stream.eval(IO.pure(0)).repeatN(1024)
    val s2 = Stream.empty

    List(s1, s2).parJoinUnbounded.compile.last.unsafeRunSync().get
  }

  @Benchmark
  def noopJoinWithEmpty(): Int = {
    val s1 = Stream.eval(IO.pure(0)).repeatN(1024)

    s1.compile.last.unsafeRunSync().get
  }

}
