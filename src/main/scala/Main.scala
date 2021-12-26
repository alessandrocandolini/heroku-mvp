import cats.effect.{IO, IOApp}
import cats.effect.std.Console
import cats.Show
import cli.*
import com.monovore.decline.Opts
import fs2.*
import cats.Functor
import utils.*
import utils.simpleConsole

object Main extends CommandIOAppSimple(
      name = "heroku-mvp",
      header = "heroku-mvp",
      version = "0.1"
    ):

  override def run: Opts[IO[Unit]] = Args.readArgs.map(program)

  def source : Stream[Pure, String] = Stream("hello", "world")

  def pipeline[F[_] : SimpleConsole : Functor, A : Show] : Stream[F,A] => Stream[F,A] =
    _.evalTap(SimpleConsole[F].println)

  val program: Args => IO[Unit] = _ =>
    source.covary[IO]
      .through(pipeline)
      .compile
      .drain
