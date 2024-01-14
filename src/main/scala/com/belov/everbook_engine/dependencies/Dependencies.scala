package com.belov.everbook_engine.dependencies

import com.belov.everbook_engine.config.Config
import com.belov.everbook_engine.repository.BooksRepository.InMemoryBooksRepository
import com.belov.everbook_engine.use_cases.search.BooksSearch
import com.belov.everbook_engine.use_cases.store.Store

case class Dependencies(config: Config) {
  val repository = new InMemoryBooksRepository()

  val booksSearch: BooksSearch = BooksSearch(repository)

  val store: Store = Store.excel(config.output)
}
