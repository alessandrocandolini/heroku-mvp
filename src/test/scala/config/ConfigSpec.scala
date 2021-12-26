package config
import munit.FunSuite
import config.*
import pureconfig.*

class ConfigSpec extends FunSuite:

  test("fail to parse invalid input") {
    ConfigSource.string("{}").load[Config].isRight
  }

  test("correctly parse valid input") {

    val s : String =
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

    val expected : Config = Config(
        port = Port.apply(1212),
        host = Host.apply("localhost"),
        database = DbConfig(
          user = DbUser.apply("dbuser"),
          password = DbPassword.apply("password"),
          host = DbHost.apply("localhost"),
          name = DbName.apply("server")
        )
      )

    val actual = ConfigSource.string(s).load[Config]
    assertEquals(actual, Right(expected))

  }
