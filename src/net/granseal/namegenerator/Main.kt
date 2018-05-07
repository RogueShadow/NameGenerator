package net.granseal.namegenerator

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.layout.AnchorPane
import javafx.stage.Stage


class CharacterGenerator : Application() {
    override fun start(stage: Stage) {
        Data.start(stage)
    }


    override fun stop(){
    }
}
