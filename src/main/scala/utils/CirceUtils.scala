package utils;

import io.circe.{Codec, Decoder, HCursor, JsonObject}
import scala.deriving.Mirror

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
