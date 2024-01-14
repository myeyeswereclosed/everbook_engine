package com.belov.everbook_engine

import java.io.{ByteArrayOutputStream, FileOutputStream}
import cats.effect.{ExitCode, IO, IOApp}
import com.belov.everbook_engine.config.Config
import com.belov.everbook_engine.router.HttpRouter
import org.http4s.ember.server.EmberServerBuilder
import org.typelevel.log4cats.LoggerFactory
import org.typelevel.log4cats.slf4j.Slf4jFactory
import cats.implicits._
import com.belov.everbook_engine.dependencies.Dependencies
import com.belov.everbook_engine.use_cases.load.sheets
import com.belov.everbook_engine.use_cases.load.sheets.{SheetData, SheetDataAdapter, SheetDataLoading, Spreadsheets}
import com.google.api.services.drive.model.File
import org.apache.poi.hssf.usermodel.HSSFWorkbook

object Main extends IOApp.Simple {

  implicit val logging: LoggerFactory[IO] = Slf4jFactory.create[IO]

  private val logger = logging.getLogger

  override def run: IO[Unit] = {
    val config = Config.load()
    val dependencies = Dependencies(config)

    for {
      app <- HttpRouter(dependencies).app
      _ <- logger.info(s"Config $config")
      books <- SheetDataLoading(config, logging).run()
      _ = dependencies.repository.insert(books.toList)
      _ <- logger.info(s"Loaded ${books.size} books of ${books.map(_.source).distinct.size} sources")
      _ <- EmberServerBuilder
        .default[IO]
        .withHttpApp(app)
        .build
        .useForever
        .as(ExitCode.Success)
    } yield ()
  }
}
