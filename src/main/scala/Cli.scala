import cats.effect.{IO, IOApp}
import cats.effect.std.Console
import cats.Show
import cli.*
import com.monovore.decline.Opts
import fs2.*
import cats.Functor
import utils.*
import utils.simpleConsole
import cats.effect.*

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
