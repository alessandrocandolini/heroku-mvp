import cats.effect.{IO, IOApp}
import cats.effect.std.Console
import cats.implicits.*
import cats.Show
import com.monovore.decline.Opts
import fs2.*
import cats.Functor
import utils.*
import cli.*
import utils.simpleConsole
import sttp.tapir.*
import sttp.tapir.json.circe.*
import sttp.tapir.generic.auto.*
import cats.effect.ExitCode
import org.http4s.HttpRoutes
import sttp.tapir.server.http4s.Http4sServerInterpreter
import org.http4s.blaze.server.BlazeServerBuilder
import scala.language.unsafeNulls
import status.StatusEndpoint.*

object Server:

  val statusRoutes: HttpRoutes[IO] =
    Http4sServerInterpreter[IO]().toRoutes(fullEndpoint)

  val routes = statusRoutes.orNotFound

  val program: Args => IO[Unit] = args =>
    BlazeServerBuilder[IO]
      .bindHttp(args.port, "localhost")
      .withHttpApp(routes)
      .serve
      .compile
      .drain
