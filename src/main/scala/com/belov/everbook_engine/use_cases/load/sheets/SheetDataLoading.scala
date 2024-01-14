package com.belov.everbook_engine.use_cases.load.sheets

import cats.Monad
import cats.implicits._
import com.belov.everbook_engine.config.Config
import com.belov.everbook_engine.config.Config.SheetConfig
import com.belov.everbook_engine.domain.Book
import com.google.api.services.sheets.v4.Sheets
import org.typelevel.log4cats.{LoggerFactory, SelfAwareStructuredLogger}

case class SheetDataLoading[F[_]: Monad](config: Config, loggerFactory: LoggerFactory[F]) {
  val logger: SelfAwareStructuredLogger[F] = loggerFactory.getLogger

  def run(): F[Seq[Book]] =
    for {
      sheets <- Spreadsheets(config.sources.googleSheets.access).load().pure[F]
      sheetConfigs: Seq[Config.SheetConfig] = config.sources.googleSheets.list
      books <- sheetConfigs.traverse(loadBooks(_, sheets)).map(_.flatten)
    } yield books

  private def loadBooks(config: SheetConfig, sheets: Sheets#Spreadsheets): F[List[Book]] = {
    val data = SheetData(config, sheets).load()

    for {
      _ <- data.traverse(s => logger.info(s"Loaded ${s.booksNumber} books for source ${s.source}"))
      books = data.flatMap(SheetDataAdapter(_, config.defaultProtocol).adapt)
    } yield books
  }

}
