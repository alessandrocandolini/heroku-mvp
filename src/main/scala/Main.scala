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

object Main extends IOApp:

  val statusRoutes: HttpRoutes[IO] =
    Http4sServerInterpreter[IO]().toRoutes(fullEndpoint)

  val server = statusRoutes.orNotFound

  override def run(args: List[String]): IO[ExitCode] = {
    val port = 8080
    BlazeServerBuilder[IO]
      .bindHttp(port, "localhost")
      .withHttpApp(server)
      .serve
      .compile
      .drain
      .map(_ => ExitCode.Success)
  }
