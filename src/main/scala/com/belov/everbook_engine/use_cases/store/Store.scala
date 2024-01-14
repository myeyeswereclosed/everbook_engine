package com.belov.everbook_engine.use_cases.store

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import cats.Applicative
import com.belov.everbook_engine.domain.Book
import spoiwo.model.{Row, Sheet}
import spoiwo.natures.xlsx.Model2XlsxConversions._
import com.belov.everbook_engine.config.Config.{OutputConfig, outputHeaders}

import scala.util.Try

trait Store {

  def run(books: List[Book]): Either[Throwable, Unit]

}

object Store {
  def excel(config: OutputConfig): Store = (books: List[Book]) => {
    (for {
      sheet <- Try {
        Sheet(
          name = "Sheet",
          row = Row().withCellValues(outputHeaders.values.toList),
          rows = books.map(book => Row().withCellValues(book.valuesStringList)): _*
        )
      }
      fileName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("Books_found_YYMMdd_HHmmss"))
      _ <- Try {
        sheet.saveAsXlsx(s"${config.dirPath}\\$fileName.xlsx")
      }
    } yield ()).toEither
  }
}


