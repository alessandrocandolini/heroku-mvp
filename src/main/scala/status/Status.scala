package status
import cats.effect.{IO, IOApp}
import cats.effect.std.Console
import cats.implicits.*
import cats.{Applicative, Functor, Show}
import com.monovore.decline.Opts
import fs2.*
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
import sttp.tapir.server.ServerEndpoint

case class StatusResponse(status: String) derives CanEqual, MyCodecAsObject

object StatusEndpoint:
  val ok: StatusResponse = StatusResponse("ok")

  val statusEndpoint: PublicEndpoint[Unit, Nothing, StatusResponse, Any] =
    infallibleEndpoint.get
      .in("status")
      .out(jsonBody[StatusResponse])

  def handler[F[_]: Applicative]: Unit => F[Either[Nothing, StatusResponse]] = _ => Applicative[F].pure(Right(ok))

  def statusServerEndpoint[F[_]: Applicative]: ServerEndpoint[Any, F] = statusEndpoint.serverLogic(handler)
