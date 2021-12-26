package cli

import munit.FunSuite
import cli.*
import cli.Args.readArgs
import com.monovore.decline.*

class ArgsSpec extends FunSuite:

  val command = Command(
    name = "test",
    header = "test"
  ) {
    readArgs
  }

  test("opts can parse valid args with stage env") {
    val actual = command.parse(
      Seq(
        "-p",
        "5",
        "--stage",
        "--verbose"
      )
    )
    val expected = Args(
      port = 5,
      env = Env.Stage,
      verbose = Verbose.Verbose
    )

    assertEquals(actual, Right(expected))
  }

  test("opts can parse valid args with prod env") {
    val actual = command.parse(
      Seq(
        "-p",
        "5",
        "--prod"
      )
    )
    val expected = Args(
      port = 5,
      env = Env.Prod,
      verbose = Args.defaultVerbose
    )

    assertEquals(actual, Right(expected))
  }

  test("opts can parse valid args without stage/prod flag and port") {
    val actual = command.parse(
      Seq()
    )
    val expected = Args(
      port = Args.defaultPort,
      env = Args.defaultEnv,
      verbose = Args.defaultVerbose
    )

    assertEquals(actual, Right(expected))

  }
