import munit.FunSuite
import cli.*
import cli.Args.readArgs
import com.monovore.decline.*

class OptsSpec extends FunSuite:

  val command = Command(
    name = "test",
    header = "test"
  ) {
    readArgs
  }

  test("opts can parse valid args with stage env") {
    val actual   = command.parse(
      Seq(
        "-n",
        "5",
        "--path",
        "file://file.txt",
        "--stage",
        "--verbose"
      )
    )
    val expected = Args(
      limit = Limit.apply(5),
      path = "file://file.txt",
      env = Env.Stage,
      verbose = Verbose.Verbose
    )

    assertEquals(actual, Right(expected))
  }

  test("opts can parse valid args with prod env") {
    val actual   = command.parse(
      Seq(
        "-n",
        "5",
        "--path",
        "file://file.txt",
        "--prod"
      )
    )
    val expected = Args(
      limit = Limit.apply(5),
      path = "file://file.txt",
      env = Env.Prod,
      verbose = Args.defaultVerbose
    )

    assertEquals(actual, Right(expected))
  }

  test("opts can parse valid args without stage/prod flag") {
    val actual   = command.parse(
      Seq(
        "-n",
        "5",
        "--path",
        "file://file.txt"
      )
    )
    val expected = Args(
      limit = Limit.apply(5),
      path = "file://file.txt",
      env = Args.defaultEnv,
      verbose = Args.defaultVerbose
    )

    assertEquals(actual, Right(expected))

  }
