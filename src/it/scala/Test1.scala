import org.scalacheck.Properties
import org.scalacheck.Prop.forAll
import scala.language.adhocExtensions

class Property1 extends Properties("hello"):

  property("addition of Int should be commutative") = forAll { (x: Int, y: Int) =>
    x + y == y + x
  }
