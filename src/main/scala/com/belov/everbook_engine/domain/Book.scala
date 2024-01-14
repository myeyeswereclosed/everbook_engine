package com.belov.everbook_engine.domain

import java.util.UUID

import io.circe.generic.JsonCodec
import io.circe.generic.auto._

@JsonCodec
case class Book(
  id: UUID = UUID.randomUUID(),
  title: String,
  author: String,
  source: String,
  isbn: Option[String] = None,
  description: Option[String] = None,
  narrator: Option[String] = None,
  translator: Option[String] = None,
  duration: Option[String] = None,
  rightsExpirationDate: Option[String] = None,
  price: Option[String] = None,
  genre: Option[String] = None,
) {
  def valuesStringList: List[String] =
    List(
      isbn.getOrElse(""),
      author,
      title,
      narrator.getOrElse(""),
      translator.getOrElse(""),
      description.getOrElse(""),
      duration.getOrElse(""),
      rightsExpirationDate.getOrElse(""),
      price.getOrElse(""),
      genre.getOrElse("")
    )
}
