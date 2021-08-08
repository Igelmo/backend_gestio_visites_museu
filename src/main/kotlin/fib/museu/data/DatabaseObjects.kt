package fib.museu.data

import fib.museu.domain.datamodels.AssistantsType
import fib.museu.domain.datamodels.PieceType
import org.ktorm.schema.*

object Persons : Table<Person>("person") {
    val email = varchar("email").primaryKey().bindTo { it.email }
    val name = varchar("name").bindTo { it.name }
    val surname = varchar("surname").bindTo { it.surname }
    val phone = varchar("phone").bindTo { it.phone }
}

object Guides : Table<Guide>("guide") {
    val guideEmail = varchar("guideEmail").primaryKey().references(Persons) { it.person }
    val username = varchar("username").bindTo { it.username }
    val password = varchar("password").bindTo { it.password }
    val workedHours = int("workedHours").bindTo { it.workedHours }
}

object Visitors : Table<Visitor>("visitor") {
    val visitorEmail = varchar("visitorEmail").primaryKey().references(Persons) { it.person }
    val center = varchar("center").bindTo { it.center }
}

object RequestedBookings : Table<RequestedBooking>("booking") {
    val requestedDay = date("visitDay").primaryKey().bindTo { it.requestedDay }
    val requestedHour = time("visitHour").primaryKey().bindTo { it.requestedHour }
    val contactEmail = varchar("contactEmail").references(Visitors) { it.visitor }
    val assistants = int("assistants").bindTo { it.assistants }
    val typeAssistant = enum<AssistantsType>("typeAssistants").bindTo { it.typeAssistant }
    val comments = varchar("comments").bindTo { it.comments }
    val accepted = boolean("accepted").bindTo { it.accepted }
}

object Visits : Table<Visit>("visit") {
    val visitDay = date("visitDay").primaryKey().references(RequestedBookings) { it.requestedBooking }
    val visitHour = time("visitHour").primaryKey().references(RequestedBookings) { it.requestedBooking }
    val guideEmail = varchar("guideEmail").bindTo { it.guideEmail }
    val completed = boolean("completed").bindTo { it.completed }
}

object Surveys : Table<Survey>("survey") {
    val id = varchar("id").primaryKey().bindTo { it.id }
    val vDay = date("vDay").references(Visits) { it.visit }
    val vHour = time("vHour").references(Visits) { it.visit }
    val mark = int("mark").bindTo { it.mark }
    val comments = varchar("comments").bindTo { it.comments }
}

object Pieces : Table<Piece>("piece") {
    val id = varchar("id").primaryKey().bindTo { it.id }
    val donor = varchar("donor").primaryKey().bindTo { it.person.email }
    val name = varchar("name").bindTo { it.name }
    val brand = varchar("brand").bindTo { it.brand }
    val exposed = boolean("exposed").bindTo { it.exposed }
    val type = enum<PieceType>("type").bindTo { it.type }
    val imageURL = varchar("imageURL").bindTo { it.imageURL }
    val description = varchar("description").bindTo { it.description }
    val comments = varchar("comments").bindTo { it.comments }
}
