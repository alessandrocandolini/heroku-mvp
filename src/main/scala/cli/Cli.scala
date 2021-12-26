package cli
import com.monovore.decline.Opts
import Args.*
import com.monovore.decline.effect.CommandIOApp
import com.monovore.decline.Opts
import cats.effect.IO
import cats.effect.ExitCode
import cats.implicits.*

enum Verbose derives CanEqual:
  case Verbose
  case Quite

enum Env derives CanEqual:
  case Local
  case Stage
  case Prod

opaque type Limit = Int

object Limit:
  def apply(l: Int): Limit = l

case class Args(
  limit: Limit,
  path: String,
  verbose: Verbose,
  env: Env
) derives CanEqual

object Args:

  val defaultVerbose: Verbose = Verbose.Verbose

  val defaultEnv: Env = Env.Stage

  val readArgs: Opts[Args] = (
    Opts
      .option[Int]("limit", short = "n", help = "limit")
      .validate("Limit must be positive")(_ > 0)
      .map(Limit.apply),
    Opts.option[String]("path", short = "p", help = "file path"),
    Opts
      .flag("verbose", help = "Verbose output")
      .map(_ => Verbose.Verbose)
      .withDefault(defaultVerbose),
    (Opts
      .flag("stage", help = "Use stage env")
      .map(_ => Env.Stage) <+>
      Opts
        .flag("prod", help = "Use prod env")
        .map(_ => Env.Prod) <+>
      Opts
        .flag("local", help = "Use local env")
        .map(_ => Env.Local)).withDefault(defaultEnv)
  ).mapN(Args.apply)

abstract class CommandIOAppSimple(
  name: String,
  header: String,
  helpFlag: Boolean = true,
  version: String = ""
) extends CommandIOApp(name, header, helpFlag, version):
  def run: Opts[IO[Unit]]
  final def main: Opts[IO[ExitCode]] = run.map(_.as(ExitCode.Success))
