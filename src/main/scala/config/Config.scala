package config

import org.http4s.server.{ServerBuilder, defaults}

import pureconfig.*

case class Config(
  port: Port,
  host: Host,
  database: DbConfig
) derives CanEqual

object Config:

  given ConfigReader[Port] = ConfigReader[Int].map(Port.apply)

  given ConfigReader[Host] = ConfigReader[String].map(Host.apply)

  given ConfigReader[Config] =
    ConfigReader.forProduct3("port", "host", "database")(Config.apply)

case class DbConfig(
  user: DbUser,
  password: DbPassword,
  host: DbHost,
  name: DbName
) derives CanEqual

object DbConfig:
  given ConfigReader[DbUser]     = ConfigReader[String].map(DbUser.apply)
  given ConfigReader[DbPassword] = ConfigReader[String].map(DbPassword.apply)
  given ConfigReader[DbHost]     = ConfigReader[String].map(DbHost.apply)
  given ConfigReader[DbName]     = ConfigReader[String].map(DbName.apply)
  given ConfigReader[DbConfig]   = ConfigReader.forProduct4("user", "password", "host", "name")(DbConfig.apply)
