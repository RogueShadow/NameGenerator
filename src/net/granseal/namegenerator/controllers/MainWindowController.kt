package net.granseal.namegenerator.controllers

import javafx.event.ActionEvent
import javafx.event.Event
import javafx.event.EventType
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.ChoiceBox
import javafx.scene.control.TextField
import javafx.scene.input.Clipboard
import javafx.scene.input.ClipboardContent
import javafx.scene.layout.AnchorPane
import javafx.stage.Stage
import javafx.stage.StageStyle
import net.granseal.namegenerator.Data


class MainWindowController {

    @FXML lateinit var closeButton: Button
    @FXML lateinit var gender: ChoiceBox<String>
    @FXML lateinit var name: TextField
    @FXML lateinit var worldChooser: ChoiceBox<String>


    private fun updateWorldChooser(){
        worldChooser.items.clear()
        Data.cDb.getWorlds().forEach {
            worldChooser.items.add(it)
        }
        worldChooser.value = Data.cDb.world
    }


    @FXML fun openNamesEditor(actionEvent: ActionEvent) {
        Data.dataEditorWindow.show()
        actionEvent.consume()
    }

    fun initialize() {
        gender.items.addAll(Data.genders)
        gender.value = gender.items.first()
        updateWorldChooser()
        worldChooser.selectionModel.selectedItemProperty().addListener({ out, old, new ->
            if (new == null) worldChooser.value = Data.cDb.world else {
                worldChooser.value = new
                Data.cDb.world = new
            }
        })
    }



    fun closeWindow(actionEvent: ActionEvent) {
        (closeButton.scene.window as Stage).close()
        actionEvent.consume()
    }

    fun generateName(actionEvent: ActionEvent) {
        val nameVal = when (gender.value) {
            Data.genders[0] -> Data.cDb.getNamesM(limit = 1).first()
            Data.genders[1] -> Data.cDb.getNamesF(limit = 1).first()
            else -> {
                Data.cDb.getNames(limit = 1).first()
            }
        }
        val surname = Data.cDb.getSurNames(limit = 1).first()
        val race = Data.cDb.getRaces(limit = 1).first()
        val job = Data.cDb.getJobs(limit = 1).first()
        val place = Data.cDb.getPlaces(limit = 1).first()

        name.text = "You are $nameVal $surname, a $race from $place, the very best $job there is."

        val clip = Clipboard.getSystemClipboard()
        val content = ClipboardContent()
        content.putString(name.text)
        clip.setContent(content)

        actionEvent.consume()
    }
}