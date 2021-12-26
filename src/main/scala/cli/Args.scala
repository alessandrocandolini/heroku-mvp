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

case class Args(
  port: Int,
  verbose: Verbose,
  env: Env
) derives CanEqual

object Args:

  val defaultVerbose: Verbose = Verbose.Verbose

  val defaultEnv: Env = Env.Stage

  val defaultPort: Int = 8080

  val readArgs: Opts[Args] = (
    Opts
      .option[Int]("port", short = "p", help = "port where to run the server")
      .validate("port must be positive")(_ > 0)
      .withDefault(defaultPort),
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
