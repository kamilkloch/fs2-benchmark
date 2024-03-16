package fs2.benchmarks

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import fs2.*
import org.openjdk.jmh.annotations.*

import java.util.concurrent.TimeUnit

@State(Scope.Thread)
//@Warmup(iterations = 1, time = 1, timeUnit = TimeUnit.SECONDS)
//@Measurement(iterations = 1, time = 1, timeUnit = TimeUnit.SECONDS)
//@Fork(value = 1, warmups = 1)
class Benchmarks {

  @Benchmark
  def streamParJoinUnbounded(): Int = {
    val each = Stream
      .range(0, 1000)
      .map(i => Stream.eval(IO.pure(i)))
    each.parJoin(Int.MaxValue).compile.last.unsafeRunSync().get
  }

  @Benchmark
  def listParJoinUnbounded(): Int = {
    val each = List
      .range(0, 1000)
      .map(i => Stream.eval(IO.pure(i)))
    each.parJoinUnbounded.compile.last.unsafeRunSync().get
  }

  @Benchmark
  def merge2(): Int = {
    val s1 = Stream.emits(List.fill(10)(0)).covary[IO].repeatN(100)
    val s2 = Stream.emits(List.fill(5)(0)).covary[IO].repeatN(200)

    s1.merge(s2).compile.last.unsafeRunSync().get
  }

  @Benchmark
  def listParJoin2(): Int = {
    val s1 = Stream.emits(List.fill(10)(0)).covary[IO].repeatN(100)
    val s2 = Stream.emits(List.fill(5)(0)).covary[IO].repeatN(200)

    List(s1, s2).parJoinUnbounded.compile.last.unsafeRunSync().get
  }

  @Benchmark
  def streamParJoin2(): Int = {
    val s1 = Stream.emits(List.fill(10)(0)).covary[IO].repeatN(100)
    val s2 = Stream.emits(List.fill(5)(0)).covary[IO].repeatN(200)

    Stream(s1, s2).parJoinUnbounded.compile.last.unsafeRunSync().get
  }

  @Benchmark
  def mergeWithEmpty(): Int = {
    val s1 = Stream.emits(List.fill(10)(0)).covary[IO].repeatN(1000)
    val s2 = Stream.empty

    s1.merge(s2).compile.last.unsafeRunSync().get
  }

  @Benchmark
  def streamParJoinWithEmpty(): Int = {
    val s1 = Stream.emits(List.fill(10)(0)).covary[IO].repeatN(1000)
    val s2 = Stream.empty

    Stream(s1, s2).parJoinUnbounded.compile.last.unsafeRunSync().get
  }

  @Benchmark
  def listParJoinWithEmpty(): Int = {
    val s1 = Stream.emits(List.fill(10)(0)).covary[IO].repeatN(1000)
    val s2 = Stream.empty

    List(s1, s2).parJoinUnbounded.compile.last.unsafeRunSync().get
  }

  @Benchmark
  def noopJoinWithEmpty(): Int = {
    val s1 = Stream.emits(List.fill(10)(0)).covary[IO].repeatN(1000)

    s1.compile.last.unsafeRunSync().get
  }
  
}
