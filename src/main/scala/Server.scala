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
import cats.effect.*
import org.http4s.HttpRoutes
import sttp.tapir.server.http4s.Http4sServerInterpreter
import org.http4s.blaze.server.BlazeServerBuilder

import scala.language.unsafeNulls
import status.StatusEndpoint.*
import sttp.tapir.server.ServerEndpoint
import sttp.tapir.swagger.bundle.SwaggerInterpreter
import pureconfig.*
import config.*
import cats.{Applicative, Functor, Show}

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
