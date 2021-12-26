package utils;
import cats.effect.{ExitCode, IO}
import cats.implicits.*
import com.monovore.decline.Opts
import com.monovore.decline.effect.CommandIOApp

abstract class CommandIOAppSimple(
  name: String,
  header: String,
  helpFlag: Boolean = true,
  version: String = ""
) extends CommandIOApp(name, header, helpFlag, version):
  def run: Opts[IO[Unit]]
  final def main: Opts[IO[ExitCode]] = run.map(_.as(ExitCode.Success))
