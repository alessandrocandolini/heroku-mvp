package status
import cats.Applicative
import sttp.tapir.*
import sttp.tapir.generic.auto.*
import sttp.tapir.json.circe.*
import sttp.tapir.server.ServerEndpoint
import utils.MyCodecAsObject

case class StatusResponse(status: String) derives CanEqual, MyCodecAsObject

object StatusEndpoint:
  val ok: StatusResponse = StatusResponse("ok")

  val statusEndpoint: PublicEndpoint[Unit, Nothing, StatusResponse, Any] = {
    import scala.language.unsafeNulls
    infallibleEndpoint.get
      .in("status")
      .out(jsonBody[StatusResponse])
  }

  def handler[F[_]: Applicative]: Unit => F[Either[Nothing, StatusResponse]] = _ => Applicative[F].pure(Right(ok))

  def statusServerEndpoint[F[_]: Applicative]: ServerEndpoint[Any, F] = statusEndpoint.serverLogic(handler)
