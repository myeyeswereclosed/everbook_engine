package com.belov.everbook_engine.use_cases.load.sheets

import com.belov.everbook_engine.config.Config.GoogleSheetsAccessConfig
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver
import com.google.api.client.googleapis.auth.oauth2.{GoogleAuthorizationCodeFlow, GoogleClientSecrets}
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.client.util.store.FileDataStoreFactory
import com.google.api.services.sheets.v4.{Sheets, SheetsScopes}

import java.io.{File, FileInputStream, InputStreamReader}
import java.util.Collections

case class Spreadsheets(config: GoogleSheetsAccessConfig) {
  def load(): Sheets#Spreadsheets = {

    val jsonFactory = JacksonFactory.getDefaultInstance
    val transport = GoogleNetHttpTransport.newTrustedTransport()

    val clientSecrets = GoogleClientSecrets.load(
      jsonFactory,
      new InputStreamReader(new FileInputStream(config.credentialsFilePath))
    )

    val flow =
      new GoogleAuthorizationCodeFlow.Builder(
        transport,
        jsonFactory,
        clientSecrets,
        Collections.singletonList(SheetsScopes.SPREADSHEETS_READONLY)
      ).setDataStoreFactory(
        new FileDataStoreFactory(new File(config.tokensDirPath))
      )
        .setAccessType("offline")
        .build

    val receiver = new LocalServerReceiver.Builder().setPort(8888).build

    new Sheets.Builder(
      transport,
      jsonFactory,
      new AuthorizationCodeInstalledApp(flow, receiver).authorize("user")
    ).setApplicationName("EverbookSheetsAccess")
      .build()
      .spreadsheets()
  }
}
