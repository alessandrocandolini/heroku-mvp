package config

import org.http4s.server.ServerBuilder

opaque type Port = Int

object Port:
  def apply(i: Int): Port = i

opaque type Host = String

object Host:
  def apply(s: String): Host = s

extension [F[_]](s: ServerBuilder[F])
  def bindPortAndHost(p: Port, h: Host): s.Self =
    s.bindHttp(p, h)

opaque type DbUser     = String
opaque type DbPassword = String
opaque type DbHost     = String
opaque type DbName     = String

object DbUser:
  def apply(s: String): DbUser = s

object DbPassword:
  def apply(s: String): DbPassword = s

object DbHost:
  def apply(s: String): DbHost = s

object DbName:
  def apply(s: String): DbName = s
