package org.legogroup.woof

import cats.Applicative
import cats.effect.kernel.Clock
import cats.effect.{IO, Ref}
import cats.syntax.all.*

import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.time.{Instant, ZoneId}
import java.util.TimeZone
import scala.concurrent.duration.*

class StringWriter(val ref: Ref[IO, String]) extends Output[IO]:
  def output(str: String): IO[Unit]      = ref.update(log => s"$log$str\n")
  def outputError(str: String): IO[Unit] = output(str)
  def get: IO[String]                    = ref.get

val newStringWriter: IO[StringWriter] =
  for ref <- Ref[IO].of("")
  yield StringWriter(ref)


val startTime = 549459420.seconds
val leetClock: Clock[IO] = new Clock[IO]:
  def applicative = Applicative[IO]
  def monotonic   = startTime.pure
  def realTime    = startTime.pure
