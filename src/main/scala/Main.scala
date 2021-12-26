import cats.effect.IO
import cats.implicits.*
import com.monovore.decline.Opts
import utils.{simpleConsole, CommandIOAppSimple}
import cli.Args
import org.legogroup.woof.*

object Main
    extends CommandIOAppSimple(
      name = "heroku-mvp",
      header = "heroku-mvp",
      version = "0.1"
    ):

  override def run: Opts[IO[Unit]] = Args.readArgs.map(program)

  val consoleOutput: Output[IO] = new Output[IO]:
    def output(str: String)      = IO.delay(println(str))
    def outputError(str: String) = output(str)

  given Filter  = Filter.everything
  given Printer = NoColorPrinter()

  val program: Args => IO[Unit] = args =>
    for {
      given Logger[IO] <- Logger.makeIoLogger(consoleOutput)
      _                <- Cli.program[IO](args)
      _                <- Server.program[IO](args)
    } yield ()
