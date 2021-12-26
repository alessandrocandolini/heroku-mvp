import org.scalacheck.Properties
import org.scalacheck.Arbitrary
import org.scalacheck.Prop.forAll
import munit.{CatsEffectSuite, ScalaCheckEffectSuite}
import org.scalacheck.effect.PropF.*
import cats.effect.*
import fs2.*
import org.scalacheck.Gen
import cats.data.State
import cats.data.StateT
import cats.Show
import utils.*
import munit.ScalaCheckSuite

class PrintLeftPropertiesEffectful extends ScalaCheckEffectSuite:

  val initialState = MockConsole(List.empty)

  test(
    "printLeft should not print anything to the console when the stream is right"
  ) {
    forAllF { (s: Stream[Pure, Either[Int, Int]]) =>

      val program = s
        .filter(_.isRight)
        .covary[StateConsole]
        .through(printLeft)
        .compile
        .drain

      program.run(initialState).map { case (MockConsole(out), _) =>
        assertEquals(out, List.empty)
      }

    }
  }

  test(
    "printLeft should print left events"
  ) {
    forAllF { (s: Stream[Pure, Either[Int, Int]]) =>

      val program = s
        .covary[StateConsole]
        .through(printLeft)
        .compile
        .drain

      val expected =
        s.collect { case Left(i) => i }.map(Show[Int].show).compile.toList

      program.run(initialState).map { case (MockConsole(out), _) =>
        assertEquals(out, expected)
      }

    }
  }

// the stream Gen is adapted from fs2 tests, let's see if we can import the original one in fs2

extension [A](genA: Gen[A])
  def toFinitePureStreamGen: Gen[Stream[Pure, A]] = {
    def smallLists[T]: Gen[T] => Gen[List[T]] = t => Gen.posNum[Int].map(_ % 20).flatMap(Gen.listOfN(_, t))

    Gen.frequency(
      1 -> Gen.const(Stream.empty),
      5 -> smallLists(genA).map(Stream.emits),
      5 -> smallLists(genA).map(as => Stream.emits(as).chunkLimit(1).unchunks),
      5 -> smallLists(smallLists(genA))
        .map(
          _.foldLeft(Stream.empty.covaryOutput[A])((acc, as) => acc ++ Stream.emits(as))
        ),
      5 -> smallLists(smallLists(genA))
        .map(
          _.foldRight(Stream.empty.covaryOutput[A])((as, acc) => Stream.emits(as) ++ acc)
        )
    )
  }

given [A](using
  a: Arbitrary[A]
): Arbitrary[Stream[Pure, A]] = Arbitrary {
  a.arbitrary.toFinitePureStreamGen
}

case class MockConsole(out: List[String]) derives CanEqual

type StateConsole[A] = StateT[IO, MockConsole, A]

given c: SimpleConsole[StateConsole] with
  def println[A: Show](a: A): StateConsole[Unit] =
    StateT.modify[IO, MockConsole] { c =>
      c.copy(out = c.out :+ (Show[A].show(a)))
    }
