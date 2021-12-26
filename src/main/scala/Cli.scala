import cats.{Functor, Show}
import cats.effect.Concurrent
import cli.Args
import fs2.*
import utils.*

object Cli:

  def source: Stream[Pure, String] = Stream("hello", "world")

  def pipeline[F[_]: SimpleConsole: Functor, A: Show]: Stream[F, A] => Stream[F, A] =
    _.evalTap(SimpleConsole[F].println)

  def program[F[_]: SimpleConsole: Functor: Concurrent]: Args => F[Unit] = _ =>
    source
      .covary[F]
      .through(pipeline)
      .compile
      .drain
