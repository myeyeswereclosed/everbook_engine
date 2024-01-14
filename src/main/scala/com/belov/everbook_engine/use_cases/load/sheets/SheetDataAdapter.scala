package com.belov.everbook_engine.use_cases.load.sheets

import com.belov.everbook_engine.config.Config.Headers
import com.belov.everbook_engine.config.Config.SheetConfig.{Header, Protocol}
import com.belov.everbook_engine.domain.Book
import com.belov.everbook_engine.use_cases.load.sheets.SheetData.SourceSheetData

case class SheetDataAdapter(sourceSheetData: SourceSheetData, protocol: Protocol) {

  def adapt: List[Book] = {
    val headers = sheetHeaders()

    booksData
      .flatMap {
        bookMap =>
          for {
            title <- headers.get(Headers.title).flatMap(bookMap.get)
            author <- headers.get(Headers.author).flatMap(bookMap.get)
          } yield
            Book(
              title = title,
              author = author,
              source = sourceSheetData.source,
              narrator = headers.get(Headers.narrator).flatMap(bookMap.get),
              translator = headers.get(Headers.translator).flatMap(bookMap.get),
              isbn = headers.get(Headers.isbn).flatMap(bookMap.get),
              description = headers.get(Headers.description).flatMap(bookMap.get)
            )
      }
  }

  private def sheetHeaders(): Map[Header, Int] = {
    sourceSheetData.data.head
      .map(_.asInstanceOf[String].trim)
      .zipWithIndex
      .flatMap {
        case (header, index) =>
          protocol
            .find { case (_, protocolHeader) => protocolHeader == header }
            .map { case (header, _) => header -> index }
      }
      .toMap
  }

  private def booksData: List[Map[Int, Header]] =
    sourceSheetData.data.tail
      .map(_.map(_.asInstanceOf[String].trim).zipWithIndex.map {
        case (value, index) => index -> value
      }.toMap)
}
