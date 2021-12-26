import cats.effect.IO
import cats.implicits.*
import com.monovore.decline.Opts
import utils.{simpleConsole, CommandIOAppSimple}
import cli.Args

object Main
    extends CommandIOAppSimple(
      name = "heroku-mvp",
      header = "heroku-mvp",
      version = "0.1"
    ):

  override def run: Opts[IO[Unit]] = Args.readArgs.map(program)

  val program: Args => IO[Unit] = args => Cli.program[IO](args) *> Server.program[IO](args)
