package com.belov.everbook_engine.config

import com.belov.everbook_engine.config.Config.{OutputConfig, SourcesConfig}
import pureconfig.ConfigSource
import pureconfig.generic.auto._

import scala.collection.mutable

case class Config(sources: SourcesConfig, output: OutputConfig)

object Config {
  def load(): Config = ConfigSource.default.loadOrThrow[Config]

  case class SourcesConfig(googleSheets: GoogleSheetsConfig, local: LocalSourcesConfig)

  case class LocalSourcesConfig(list: Seq[LocalSourceConfig])

  case class LocalSourceConfig(path: String, source: String, sheet: Int, defaultProtocol: Map[String, String])

  case class GoogleSheetsConfig(access: GoogleSheetsAccessConfig, list: Seq[SheetConfig])

  case class GoogleSheetsAccessConfig(credentialsFilePath: String, tokensDirPath: String)

  case class SheetConfig(id: String, sheets: Seq[SheetData], defaultProtocol: Map[String, String])

  case class SheetData(name: String, protocol: String)

  case class OutputConfig(dirPath: String)

  object Headers {
    val isbn = "isbn"
    val author = "author"
    val narrator = "narrator"
    val translator = "translator"
    val title = "title"
    val description = "description"
    val duration = "duration"
    val rightsExpirationDates = "rights_expiration_date"
    val price = "price"
    val genre = "genre"
  }

  import Headers._

  val outputHeaders = mutable.LinkedHashMap(
    isbn -> "ISBN",
    author -> "Автор",
    title -> "Название книги",
    narrator -> "Исполнитель",
    translator -> "Переводчик",
    description -> "Описание книги",
    duration -> "Продолжительность (ччч:мм:сс)",
    rightsExpirationDates -> "Дата окончания прав",
    price -> "Цена",
    genre -> "Жанр"
  )

  object SheetConfig {
    type Header = String
    type Protocol = Map[Header, String]
  }
}
