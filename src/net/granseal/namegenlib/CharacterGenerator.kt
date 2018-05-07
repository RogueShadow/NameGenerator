//package net.granseal.namegenlib
//
//import com.google.gson.Gson
//import com.google.gson.GsonBuilder
//import com.google.gson.reflect.TypeToken
//import java.io.*
//import java.util.*
//import java.util.zip.ZipEntry
//import java.util.zip.ZipInputStream
//import java.util.zip.ZipOutputStream
//
//class CharacterGenerator(private val rnd: Random = Random()) {
//    var nameData = NameDataFactory().empty()
//    var properties = Properties()
//
//    fun generate(gender: Sex = Sex.MALE): Character {
//        val name = when (gender){
//            Sex.MALE -> (nameData.names.filter { it.sex == Sex.MALE }).random(rnd).name
//            Sex.FEMALE -> (nameData.names.filter { it.sex == Sex.FEMALE }).random(rnd).name
//            Sex.RANDOM -> nameData.names.random(rnd).name
//        }
//        val surName = nameData.surnames.random(rnd).name
//        val job = nameData.jobs.random(rnd).job
//        val race = nameData.races.random(rnd).race
//        val city = nameData.cities.random(rnd).city
//        return Character(name, surName, race, city, job)
//    }
//
//    private fun jsonToNameData(json: String): NameData {
//        val gson = Gson()
//        return gson.fromJson<NameData>(json, object : TypeToken<NameData>() {}.type)
//    }
//
//    private fun nameDataToJson(nameData: NameData): String {
//        val gson = GsonBuilder().setPrettyPrinting().serializeNulls().create()
//        return gson.toJson(nameData)
//    }
//
//    private fun zipInputStreams(zip: File, inputStreams: Array<Pair<String, InputStream>>): File {
//        ZipOutputStream(BufferedOutputStream(FileOutputStream(zip))).use { out ->
//            for (stream in inputStreams) {
//                BufferedInputStream(stream.second).use { origin ->
//                    val entry = ZipEntry(stream.first)
//                    out.putNextEntry(entry)
//                    origin.copyTo(out, 1024)
//                }
//            }
//        }
//        return zip
//    }
//    private fun zipInputStreams(inputStreams: Array<Pair<String,InputStream>>): ByteArray {
//        val buffer = ByteArrayOutputStream()
//        ZipOutputStream(BufferedOutputStream(buffer)).use { out ->
//            for (stream in inputStreams) {
//                BufferedInputStream(stream.second).use { origin ->
//                    val entry = ZipEntry(stream.first)
//                    out.putNextEntry(entry)
//                    origin.copyTo(out, 1024)
//                }
//            }
//        }
//        return buffer.toByteArray()
//    }
//
//    fun getCharacterDataFile(): ByteArray {
//        val dataJson = nameDataToJson(nameData)
//        val properties = properties
//        val props = StringWriter()
//
//        properties.store(props,"")
//        return zipInputStreams(arrayOf(
//                Pair("data.json",dataJson.byteInputStream()),
//                Pair("properties.cfg",props.toString().byteInputStream())))
//    }
//
//    fun saveCharacterDataFile(file: File): File {
//        val dataJson = nameDataToJson(nameData)
//        val properties = properties
//        val props = StringWriter()
//
//        properties.store(props,"")
//        return zipInputStreams(file, arrayOf(
//                Pair("data.json",dataJson.byteInputStream()),
//                Pair("properties.cfg",props.toString().byteInputStream())))
//    }
//
//    private fun getCharacterDataFile(inputStream: InputStream): Pair<Properties,NameData> {
//        val zin = ZipInputStream(inputStream)
//        val props = Properties()
//        var json = String()
//        var entry: ZipEntry? = zin.nextEntry
//        while (entry != null){
//            if (entry.name == "properties.cfg"){
//                props.load(zin.reader())
//                zin.closeEntry()
//            }
//            if (entry.name == "data.json"){
//                json = String(zin.readAllBytes())
//
//                zin.closeEntry()
//            }
//            entry = zin.nextEntry
//        }
//        return Pair(props,jsonToNameData(json))
//    }
//
//    fun loadCharacterDataFile(file: File){
//        loadCharacterDataFile(file.inputStream())
//    }
//
//    fun loadCharacterDataFile(inputStream: InputStream){
//        val (props,data) = getCharacterDataFile(inputStream)
//        this.properties = props
//        this.nameData = data
//    }
//
//    private fun getCharacterDataFile(file: File): Pair<Properties,NameData> {
//        return getCharacterDataFile(file.inputStream())
//    }
//
//    fun importCharacterDataFile(file: File){
//        val (_,data) = getCharacterDataFile(file)
//        this.nameData.cities.addAll(data.cities)
//        this.nameData.jobs.addAll(data.jobs)
//        this.nameData.names.addAll(data.names)
//        this.nameData.races.addAll(data.races)
//        this.nameData.surnames.addAll(data.surnames)
//    }
//
//}
//
//fun Collection<Name>.random(random: Random) = if (this.isNotEmpty()) this.toTypedArray()[random.nextInt(this.size)] else Name(Sex.MALE, "")
//fun Collection<Job>.random(random: Random) =  if (this.isNotEmpty()) this.toTypedArray()[random.nextInt(this.size)] else Job("")
//fun Collection<City>.random(random: Random) =  if (this.isNotEmpty()) this.toTypedArray()[random.nextInt(this.size)] else City("")
//fun Collection<SurName>.random(random: Random) =  if (this.isNotEmpty()) this.toTypedArray()[random.nextInt(this.size)] else SurName("")
//fun Collection<Race>.random(random: Random) =  if (this.isNotEmpty()) this.toTypedArray()[random.nextInt(this.size)] else Race("")
//
//enum class Sex {MALE, FEMALE, RANDOM}
//
//data class Name(
//        val sex: Sex,
//        val name: String
//)
//data class SurName(
//        val name: String
//)
//data class Race(
//        val race: String
//)
//data class City(
//        val city: String
//)
//data class Job(
//        val job: String
//)
//data class NameData(
//        val names: MutableSet<Name>,
//        val surnames: MutableSet<SurName>,
//        val races: MutableSet<Race>,
//        val cities: MutableSet<City>,
//        val jobs: MutableSet<Job>
//)
//
//class NameDataFactory {
//    fun empty(): NameData {
//        return NameData(mutableSetOf(),
//                mutableSetOf(),
//                mutableSetOf(),
//                mutableSetOf(),
//                mutableSetOf())
//    }
//}
//
//data class Character(
//        val name: String,
//        val surname: String,
//        val race: String,
//        val place: String,
//        val job: String
//)
