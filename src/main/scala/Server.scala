import cats.effect.*
import cats.implicits.*
import cats.Applicative
import cli.Args
import config.Config
import org.http4s.HttpRoutes
import org.http4s.blaze.server.BlazeServerBuilder
import status.StatusEndpoint.*
import sttp.tapir.*
import sttp.tapir.generic.auto.*
import sttp.tapir.json.circe.*
import sttp.tapir.server.ServerEndpoint
import sttp.tapir.server.http4s.Http4sServerInterpreter
import sttp.tapir.swagger.bundle.SwaggerInterpreter

object Server:

  def allEndpoints[F[_]: Applicative]: List[ServerEndpoint[Any, F]] = List(statusServerEndpoint)

  def docsEndpoint[F[_]] = SwaggerInterpreter().fromEndpoints[F](List(statusEndpoint), "My App", "1.0")

  def routes[F[_]: Async]: HttpRoutes[F] =
    Http4sServerInterpreter[F]().toRoutes(allEndpoints ++ docsEndpoint)

  def program[F[_]: Async: Applicative]: Args => F[Unit] = args =>
    Config.readConfigOrThrow[F].flatMap { c =>
      BlazeServerBuilder[F]
        .bindHttp(c.port, c.host)
        .withHttpApp(routes.orNotFound)
        .serve
        .compile
        .drain
    }
