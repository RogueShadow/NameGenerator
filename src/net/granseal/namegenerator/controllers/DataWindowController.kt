package net.granseal.namegenerator.controllers

import javafx.application.Platform
import javafx.event.ActionEvent
import javafx.event.Event
import javafx.event.EventType
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.layout.AnchorPane
import net.granseal.namegenerator.Data
import java.util.*
import java.util.TimerTask




class DataWindowController {
    @FXML lateinit var dataWindow: AnchorPane
    @FXML lateinit var fNameList: ListView<String>
    @FXML lateinit var mNameList: ListView<String>
    @FXML lateinit var surNameList: ListView<String>
    @FXML lateinit var jobList: ListView<String>
    @FXML lateinit var raceList: ListView<String>
    @FXML lateinit var placeList: ListView<String>
    @FXML lateinit var worldList: ListView<String>
    @FXML lateinit var worldChooser: ChoiceBox<String>
    @FXML lateinit var genderChooser: ChoiceBox<String>
    @FXML lateinit var dataInput: TextField
    @FXML lateinit var dataTabs: TabPane

    private fun dataToLists(){
        mNameList.items.clear()
        fNameList.items.clear()
        surNameList.items.clear()
        raceList.items.clear()
        jobList.items.clear()
        placeList.items.clear()
        worldList.items.clear()

        Data.cDb.getNamesM().sortedBy{it}.forEach{ mNameList.items.add(it) }
        Data.cDb.getNamesF().sortedBy{it}.forEach{ fNameList.items.add(it) }
        Data.cDb.getSurNames().sortedBy{it}.forEach{ surNameList.items.add(it)}
        Data.cDb.getRaces().sortedBy{it}.forEach{ raceList.items.add(it)}
        Data.cDb.getJobs().sortedBy{it}.forEach{ jobList.items.add(it)}
        Data.cDb.getPlaces().sortedBy{it}.forEach{ placeList.items.add(it)}
        Data.cDb.getWorlds().sortedBy{it}.forEach { worldList.items.add(it) }
    }

    fun initialize(){
        Data.genders.forEach {
            genderChooser.items.add(it)
        }
        genderChooser.value = genderChooser.items[0]
        dataToLists()
        worldList.items.forEach { worldChooser.items.add(it) }
        worldChooser.value = Data.cDb.world
        dataTabs.selectionModel.selectedItemProperty().addListener({ _, _, newTab ->
            genderChooser.isVisible = (newTab.id == "nameTab")
            worldChooser.isVisible = (newTab.id != "worldTab")
            val timer = Timer()
            timer.schedule(object : TimerTask() {
                override fun run() {
                    Platform.runLater {
                        dataInput.requestFocus()
                        timer.cancel()
                        timer.purge()
                    }
                }
            }, 25)
        })
        worldChooser.selectionModel.selectedItemProperty().addListener({ _, _, new ->
            if (new == null) worldChooser.value = Data.cDb.world else {
                worldChooser.value = new
                Data.cDb.world = new
            }
            dataToLists()
        })
    }


    fun addData(actionEvent: ActionEvent) {
        when (dataTabs.selectionModel.selectedItem.id){
            "mNameTab" -> {
                if (Data.cDb.addName(dataInput.text,Data.genders[0])) {
                    mNameList.items.add(dataInput.text)
                }else println("Name already exists.")
            }
            "fNameTab" -> {
                if (Data.cDb.addName(dataInput.text,Data.genders[1])) {
                    fNameList.items.add(dataInput.text)
                }else println("Name already exists.")
            }
            "surNameTab" -> {
                if (Data.cDb.addSurName(dataInput.text)) {
                    surNameList.items.add(dataInput.text)
                }else println("Surname already exists.")
            }
            "raceTab" -> {
                if (Data.cDb.addRace(dataInput.text)) {
                    raceList.items.add(dataInput.text)
                }else println("Race already exists.")
            }
            "placeTab" -> {
                if (Data.cDb.addPlace(dataInput.text)) {
                    placeList.items.add(dataInput.text)
                }else println("Place already exists.")
            }
            "jobTab" -> {
                if (Data.cDb.addJob(dataInput.text)) {
                    jobList.items.add(dataInput.text)
                }else println("Job already exists.")
            }
            "worldTab" -> {
                if (Data.cDb.addWorld(dataInput.text)) {
                    worldList.items.add(dataInput.text)
                    worldChooser.items.add(dataInput.text)
                    dataToLists()
                }else println("World already exists.")
            }
            else -> println("Other Tab")
        }
        dataInput.text = ""

        actionEvent.consume()
    }

    fun deleteItem(actionEvent: ActionEvent) {
        when (dataTabs.selectionModel.selectedItem.id){
            "mNameTab" ->{
                Data.cDb.removeName(mNameList.selectionModel.selectedItem,"M")
                mNameList.items.remove(mNameList.selectionModel.selectedItem)
            }
            "fNameTab" ->{
                Data.cDb.removeName(fNameList.selectionModel.selectedItem,"F")
                fNameList.items.remove(fNameList.selectionModel.selectedItem)
            }
            "raceTab" -> {
                Data.cDb.removeRace(raceList.selectionModel.selectedItem)
                raceList.items.remove(raceList.selectionModel.selectedItem)
            }
            "placeTab" -> {
                Data.cDb.removePlace(placeList.selectionModel.selectedItem)
                placeList.items.remove(placeList.selectionModel.selectedItem)
            }
            "jobTab" -> {
                Data.cDb.removeJob(jobList.selectionModel.selectedItem)
                jobList.items.remove(jobList.selectionModel.selectedItem)
            }
            "surNameTab" -> {
                Data.cDb.removeSurName(surNameList.selectionModel.selectedItem)
                surNameList.items.remove(surNameList.selectionModel.selectedItem)
            }
            "worldTab" -> {
                val data = worldList.selectionModel.selectedItem
                Data.cDb.removeWorld(data)
                worldList.items.remove(data)
                worldChooser.items.remove(data)
                dataToLists()
            }
            else -> println("No tab by id: ${dataTabs.selectionModel.selectedItem.id}")
        }
        actionEvent.consume()
    }

    @FXML fun close(){
        dataWindow.scene.window.hide()
    }
}