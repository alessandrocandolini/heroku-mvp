package status
import cats.{Applicative, Monad}
import cats.implicits.*
import sttp.tapir.*
import sttp.tapir.generic.auto.*
import sttp.tapir.json.circe.*
import sttp.tapir.server.ServerEndpoint
import utils.MyCodecAsObject
import org.legogroup.woof.Logger

case class StatusResponse(status: String) derives CanEqual, MyCodecAsObject

object StatusEndpoint:
  val ok: StatusResponse = StatusResponse("ok")

  val statusEndpoint: PublicEndpoint[Unit, Nothing, StatusResponse, Any] = {
    import scala.language.unsafeNulls
    infallibleEndpoint.get
      .in("status")
      .out(jsonBody[StatusResponse])
  }

  def handler[F[_]: Monad: Logger]: Unit => F[Either[Nothing, StatusResponse]] = _ => Logger[F].debug("status invoked") *> Applicative[F].pure(Right(ok))

  def statusServerEndpoint[F[_]: Monad: Logger]: ServerEndpoint[Any, F] = statusEndpoint.serverLogic(handler)
