package utils;

import cats.effect.std.Console
import cats.Show
import cats.Applicative
import fs2.Stream
import io.circe.{Codec, Decoder, HCursor, JsonObject}
import scala.deriving.Mirror

trait SimpleConsole[F[_]]:
  def println[A: Show](a: A): F[Unit]

object SimpleConsole:
  def apply[F[_]](using c: SimpleConsole[F]): c.type = c

given simpleConsole[F[_]: Console]: SimpleConsole[F] with
  def println[A: Show](a: A): F[Unit] = Console[F].println(a)

def printLeft[F[_]: Applicative: SimpleConsole, E: Show, A]: Stream[F, Either[E, A]] => Stream[F, Either[E, A]] = _.evalTap {
  case Left(e)  => SimpleConsole[F].println(e)
  case Right(_) => Applicative[F].unit
}

trait MyCodecAsObject[A] extends Codec.AsObject[A]

object MyCodecAsObject:

  inline def derived[A: Mirror.Of]: MyCodecAsObject[A] =
    new MyCodecAsObject[A] {
      private val derived                      = {
        import scala.language.unsafeNulls
        Codec.AsObject.derived[A]
      }
      def encodeObject(a: A): JsonObject       = derived.encodeObject(a)
      def apply(c: HCursor): Decoder.Result[A] =
        derived.apply(c)
    }
