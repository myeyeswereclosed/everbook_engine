package com.belov.everbook_engine.use_cases.load

import com.belov.everbook_engine.config.Config.LocalSourceConfig
import com.belov.everbook_engine.use_cases.load.sheets.SheetData.SourceSheetData

case class LocalSourceData(config: LocalSourceConfig) {

  def load(): List[SourceSheetData] = {
    List.empty
  }

}
