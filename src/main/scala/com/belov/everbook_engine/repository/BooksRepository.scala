package com.belov.everbook_engine.repository

import com.belov.everbook_engine.domain.Book
import com.belov.everbook_engine.use_cases.search.BooksSearch.{BookInput, SearchResult}

trait BooksRepository {
  def insert(books: List[Book]): Unit

  def search(input: List[BookInput]): List[SearchResult]
}


object BooksRepository {
  class InMemoryBooksRepository extends BooksRepository {
    private var books = List.empty[Book]

    override def search(input: List[BookInput]): List[SearchResult] =
      for {
        bookToSearch <- input
        booksStored = books.filter(
          b => bookToSearch.title.contains(b.title) || bookToSearch.isbn.exists(b.isbn.contains)
        )
      } yield SearchResult(bookToSearch, booksStored)

    override def insert(books: List[Book]): Unit = this.books = books
  }
}


