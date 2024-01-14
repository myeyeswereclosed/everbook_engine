package com.belov.everbook_engine.use_cases.search

import com.belov.everbook_engine.domain.Book
import com.belov.everbook_engine.repository.BooksRepository
import com.belov.everbook_engine.use_cases.search.BooksSearch.{BookInput, SearchResult}
import io.circe.generic.JsonCodec
import io.circe.generic.auto._

import scala.util.Try

case class BooksSearch(repository: BooksRepository) {
  def search(input: String): List[SearchResult] = {
    val inputData =
      for {
        bookInputString <- input.split(System.lineSeparator()).tail
        data = bookInputString.split(",")
      } yield BookInput(isbn = bookInput(data, 0), title = bookInput(data, 1))

    repository.search(inputData.toList)
  }

  private def bookInput(data: Array[String], index: Int): Option[String] =
    Try(data(index)).toOption.flatMap(s => Option(s).filter(_.trim.nonEmpty))
}

object BooksSearch {

  case class BookInput(isbn: Option[String], title: Option[String]) {
    require(List(title, isbn).flatten.nonEmpty, "Either title or isbn should be provided")
  }

  @JsonCodec
  case class SearchResult(input: BookInput, books: List[Book] = List.empty)

}
