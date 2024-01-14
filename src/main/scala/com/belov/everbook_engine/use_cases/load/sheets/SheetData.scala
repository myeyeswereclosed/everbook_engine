package com.belov.everbook_engine.use_cases.load.sheets

import com.belov.everbook_engine.config.Config.SheetConfig
import com.belov.everbook_engine.use_cases.load.sheets.SheetData.SourceSheetData
import com.google.api.services.sheets.v4.Sheets

import java.util
import scala.jdk.CollectionConverters._

case class SheetData(config: SheetConfig, spreadsheets: Sheets#Spreadsheets) {

  def load(): List[SourceSheetData] = {
    val storageSheetsNames =
      toScala(
        spreadsheets
          .get(config.id)
          .execute()
          .getSheets
      )
        .filter(s => config.sheets.map(_.name).contains(s.getProperties.getTitle))
        .map(_.getProperties.getTitle)

      storageSheetsNames.map(
        worksheetName =>
          SourceSheetData(
            source = worksheetName,
            data =
              toScala(
                spreadsheets
                  .values()
                  .get(config.id, s"$worksheetName")
                  .execute()
                  .getValues
              )
                .map(toScala)
          )
      )
  }

  private def toScala[T](list: util.List[T]): List[T] = list.asScala.toList
}

object SheetData {
  type Data = List[List[AnyRef]]

  case class SourceSheetData(source: String, data: Data) {
    def booksNumber: Int = data.headOption.fold(0)(_ => data.tail.size)
  }
}
