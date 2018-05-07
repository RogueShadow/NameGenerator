package net.granseal.namegenlib

import java.sql.DriverManager
import java.sql.PreparedStatement
import java.util.*

class NameGenDB(dbName: String = "jdbc:postgresql://rogue.granseal.net:5432/webapptut",
                dbUser: String = "webapptut",
                dbPass: String = "webapptutpass") {

    init {
        DriverManager.registerDriver(org.postgresql.Driver())
    }

    private val columnMap = mapOf(Pair("m_names","name"),Pair("f_names","name"),Pair("surnames","surname"),Pair("races","race"),
            Pair("jobs","job"),Pair("places","place"),Pair("worlds","world"))

    private var currentWorldID = 1

    var world: String = "default"
        set(value){
            val stmt = c.prepareStatement("select world, world_id from worlds where world = ? limit 1")
            stmt.setString(1, value)
            val rs = stmt.executeQuery()
            if (rs.next()){
                currentWorldID = rs.getInt("world_id")
                val worlddb = rs.getString("world")
                if (worlddb != null)field = worlddb
            }
        }

    private val c = DriverManager.getConnection(dbName,dbUser,dbPass)

    init {
        val stmt = c.createStatement()
        stmt.execute("SET search_path to chargenerator")
    }

    fun addWorld(world: String): Boolean {
        val checkstmt = c.prepareStatement("select world from worlds where world = ?")
        checkstmt.setString(1, world)
        val rs = checkstmt.executeQuery()
        return if (!rs.next()){
            val stmt = c.prepareStatement("insert into worlds(world) values(?)")
            stmt.setString(1, world)
            stmt.execute()
            true
        }else false
    }
    // Gender is "M" or "F" for Male and Female
    fun addName(name: String, gender: String) : Boolean{
        return when (gender) {
            "M" -> addNameM(name)
            "F" -> addNameF(name)
            else -> false
        }
    }
    private fun checkExists(table: String, value: String): Boolean {
        if (!columnMap.containsKey(table)){
            return false
        }
        val stmt = c.prepareStatement("select ${columnMap[table]} from $table where world_id = $currentWorldID and ${columnMap[table]} = ?")
        stmt.setString(1, value)
        val rs = stmt.executeQuery()
        return rs.next()
    }
    private fun addNameM(name: String): Boolean {
        val exists = checkExists("m_names",name)
        return if (!exists) {
            val stmt = c.prepareStatement("insert into m_names(name, world_id) values(?,?)")
            stmt.setString(1, name)
            stmt.setInt(2, currentWorldID)
            stmt.execute()
            true
        }else false

    }
    private fun addNameF(name: String): Boolean{
        val exists = checkExists("f_names",name)
        return if (!exists) {
            val stmt = c.prepareStatement("insert into f_names(name, world_id) values(?,?)")
            stmt.setString(1, name)
            stmt.setInt(2, currentWorldID)
            stmt.execute()
            true
        }else false

    }
    fun addSurName(surname: String): Boolean{
        val exists = checkExists("surnames",surname)
        return if (!exists) {
            val stmt = c.prepareStatement("insert into surnames(surname, world_id) values(?,?)")
            stmt.setString(1, surname)
            stmt.setInt(2, currentWorldID)
            stmt.execute()
            true
        } else false
    }
    fun addRace(race: String): Boolean {
        val exists = checkExists("races",race)
        return if (!exists) {
            val stmt = c.prepareStatement("insert into races(race, world_id) values(?,?)")
            stmt.setString(1, race)
            stmt.setInt(2, currentWorldID)
            stmt.execute()
            true
        }else false
    }
    fun addPlace(place: String): Boolean {
        val exists = checkExists("places",place)
        return if (!exists) {
            val stmt = c.prepareStatement("insert into places(place, world_id) values(?,?)")
            stmt.setString(1, place)
            stmt.setInt(2, currentWorldID)
            stmt.execute()
            true
        }else false
    }
    fun addJob(job: String): Boolean {
        val exists = checkExists("jobs",job)
        return if (!exists) {
            val stmt = c.prepareStatement("insert into jobs(job, world_id) values(?, ?)")
            stmt.setString(1, job)
            stmt.setInt(2, currentWorldID)
            stmt.execute()
            true
        }else false
    }

    fun removeWorld(world: String){
        val stmt = c.prepareStatement("delete from worlds where world = ?")
        stmt.setString(1, world)
        stmt.execute()
    }
    private fun removeNameM(name: String){
        val stmt = c.prepareStatement("delete from m_names where name = ?")
        stmt.setString(1, name)
        stmt.execute()
    }
    private fun removeNameF(name: String){
        val stmt = c.prepareStatement("delete from f_names where name = ?")
        stmt.setString(1, name)
        stmt.execute()
    }
    // gender is "M" or "F" for Male or Female.
    fun removeName(name: String, gender: String){
        when (gender){
            "M" -> removeNameM(name)
            "F" -> removeNameF(name)
            else -> return
        }
    }
    fun removeSurName(surname: String){
        val stmt = c.prepareStatement("delete from surnames where surname = ?")
        stmt.setString(1, surname)
        stmt.execute()
    }
    fun removeRace(race: String){
        val stmt = c.prepareStatement("delete from races where race = ?")
        stmt.setString(1, race)
        stmt.execute()
    }
    fun removePlace(place: String){
        val stmt = c.prepareStatement("delete from places where place = ?")
        stmt.setString(1, place)
        stmt.execute()
    }
    fun removeJob(job: String){
        val stmt = c.prepareStatement("delete from jobs where job = ?")
        stmt.setString(1, job)
        stmt.execute()
    }

    fun getWorlds(): List<String> {
        val stmt = c.prepareStatement("select world from worlds")
        val rs = stmt.executeQuery()
        val result = mutableListOf<String>()
        while (rs.next()) {
            result.add(rs.getString("world"))
        }
        return result
    }
    private fun getLimit(limit: Int?): String = if (limit == null) "" else "ORDER BY random() LIMIT $limit"
    private fun getWorldFilter(world_id: Int? = currentWorldID): String {
        return if (world_id == null) {
            ""
        } else {
            "where world_id = $world_id"
        }
    }

    fun getNamesM(world_id: Int? = currentWorldID, limit: Int? = null): List<String> {
        val stmt = c.prepareStatement("select name from m_names ${getWorldFilter(world_id)} ${getLimit(limit)}")
        return getCol(stmt, "name")
    }
    fun getNamesF(world_id: Int? = currentWorldID, limit: Int? = null): List<String> {
        val stmt = c.prepareStatement("select name from f_names ${getWorldFilter(world_id)} ${getLimit(limit)}")
        return getCol(stmt, "name")
    }
    fun getNames(world_id: Int? = currentWorldID, limit: Int? = null): List<String> {
        val mnames = getNamesM(world_id, limit)
        val fnames = getNamesF(world_id, limit)
        val names = mnames.toMutableSet()
        names.addAll(fnames)
        val s = names.size/2
        val r = Random(System.nanoTime())
        while (names.size > s){
            names.remove(names.elementAt(r.nextInt(names.size)))
        }
        return names.toList()
    }
    fun getSurNames(world_id: Int? = currentWorldID, limit: Int? = null): List<String> {
        val stmt = c.prepareStatement("select surname from surnames ${getWorldFilter(world_id)} ${getLimit(limit)}")
        return getCol(stmt, "surname")
    }
    fun getPlaces(world_id: Int? = currentWorldID, limit: Int? = null): List<String> {
        val stmt = c.prepareStatement("select place from places ${getWorldFilter(world_id)} ${getLimit(limit)}")
        return getCol(stmt, "place")
    }
    fun getRaces(world_id: Int? = currentWorldID, limit: Int? = null): List<String> {
        val stmt = c.prepareStatement("select race from races ${getWorldFilter(world_id)} ${getLimit(limit)}")
        return getCol(stmt,"race")
    }
    fun getJobs(world_id: Int? = currentWorldID, limit: Int? = null): List<String> {
        val stmt = c.prepareStatement("select job from jobs ${getWorldFilter(world_id)} ${getLimit(limit)}")
        return getCol(stmt,"job")
    }
    private fun getCol(stmt: PreparedStatement,col: String): List<String> {
        val rs = stmt.executeQuery()
        val result = mutableListOf<String>()
        while (rs.next()){
            result.add(rs.getString(col))
        }
        return result
    }
}