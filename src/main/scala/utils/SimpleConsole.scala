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