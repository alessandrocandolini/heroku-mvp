package config
import munit.FunSuite
import config.*
import pureconfig.*

class ConfigSpec extends FunSuite:

  test("fail to parse invalid input") {
    assert(ConfigSource.string("{}").load[Config].isLeft)
  }

  test("correctly parse valid input") {

    val s: String =
      """{
        |port = 1212
        |host = "localhost"
        |database {
        |    user = "dbuser"
        |    password = "password"
        |    host = "localhost"
        |    name = "server"
        |}
        |}
        """.stripMargin

    val expected: Config = Config(
      port = 1212,
      host = "localhost",
      database = DbConfig(
        user = "dbuser",
        password = "password",
        host = "localhost",
        name = "server"
      )
    )

    val actual = ConfigSource.string(s).load[Config]
    assertEquals(actual, Right(expected))

  }

  test("can parse actual application.conf successfully") {
    assert(Config.readDefaultConfig.isRight)
  }
