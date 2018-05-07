package net.granseal.namegenerator


import javafx.beans.Observable
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableStringValue
import javafx.event.EventType
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.control.ChoiceBox
import javafx.scene.layout.AnchorPane
import javafx.stage.Stage
import net.granseal.namegenlib.NameGenDB
import java.net.URL

object Data {
    //val charGen = CharacterGenerator()
    val cDb = NameGenDB()
    // Male, Female, Random
    val genders = arrayOf("M","F","R")

    lateinit var generatorWindow: Stage
    var dataEditorWindow = Stage()

    val generatorRoot: AnchorPane = FXMLLoader.load<AnchorPane>(getResource("net/granseal/namegenerator/assets/MainWindow.fxml"))
    val generatorScene = Scene(generatorRoot, generatorRoot.prefWidth, generatorRoot.prefHeight)

    val dataEditorRoot: AnchorPane = FXMLLoader.load<AnchorPane>(getResource("net/granseal/namegenerator/assets/DataWindow.fxml"))
    val dataEditorScene = Scene(dataEditorRoot, dataEditorRoot.prefWidth, dataEditorRoot.prefHeight)

    fun start(stage: Stage){
        generatorWindow = stage
        generatorWindow.scene = generatorScene
        generatorWindow.title = "Name Generator"
        generatorWindow.show()
        dataEditorWindow.scene = dataEditorScene
        dataEditorWindow.title = "Data Editor"


    }

    fun getResource(res: String): URL {
        val url = this.javaClass.classLoader.getResource(res)
        if (url == null)println("Failed to find: $res")
        return url
    }

}

