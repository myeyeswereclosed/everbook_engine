package com.belov.everbook_engine.router

import java.nio.charset.Charset

import cats.effect.IO
import com.belov.everbook_engine.dependencies.Dependencies
import com.belov.everbook_engine.domain.Book
import org.http4s
import org.http4s.HttpRoutes
import org.http4s.dsl.io.{->, /, BadRequest, Ok, POST, Root, _}
import org.http4s.multipart.Multipart
import org.http4s.server.Router
import org.http4s.server.middleware.CORS
import org.http4s._
import org.http4s.circe._
import org.http4s.implicits._
import org.typelevel.log4cats.LoggerFactory
import io.circe.syntax._

case class HttpRouter(dependencies: Dependencies)(implicit val loggerFactory: LoggerFactory[IO]) {

  private val logger = loggerFactory.getLogger

  private val charset = Charset.forName("cp1251")

  implicit val decoder: EntityDecoder[IO, Book] = jsonOf[IO, Book]

  private val route: HttpRoutes[IO] = HttpRoutes.of[IO] {

    case req @ POST -> Root / "store" =>
      for {
        booksToStore <- req.decodeJson[List[Book]]
        _ <- logger.info(s"Books to store $booksToStore")
        _ <- logger.info(s"Requested ${booksToStore.size} books to store")
        storeResult = dependencies.store.run(booksToStore)
        _ <- logger.info(s"Result is $storeResult")
        res <- Ok()
      } yield res

    case req @ POST -> Root / "search" =>
      req.decode[IO, Multipart[IO]] { m =>
        {
          m.parts.find(_.name.contains("file")) match {
            case None => BadRequest(s"Not a file")
            case Some(part) =>
              for {
                contents <- part.bodyText.map(_.getBytes(charset)).compile.toList
                _ <- logger.info("Uploaded file with books to search")
                _ <- logger.info(s"File contents:\r\n${new String(contents.head, charset)}")
                response <- Ok(dependencies.booksSearch.search(new String(contents.head, charset)).asJson)
              } yield response
          }
        }
      }
  }

  val app: IO[http4s.Http[IO, IO]] = CORS.policy.apply(Router("/" -> route).orNotFound)
}
