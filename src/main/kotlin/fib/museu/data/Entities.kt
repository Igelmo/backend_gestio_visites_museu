package fib.museu.data

import fib.museu.domain.datamodels.*
import org.ktorm.entity.Entity
import java.time.LocalDateTime

interface Person : Entity<Person> {
    companion object : Entity.Factory<Person>()

    var email: String
    var name: String
    var surname: String
    var phone: String
}

fun Person(personObject: PersonObject) = Person {
    email = personObject.email
    name = personObject.name
    surname = personObject.surname
    phone = personObject.phone
}

interface Guide : Entity<Guide> {
    companion object : Entity.Factory<Guide>()

    var person: Person
    var username: String
    var password: String
    var workedHours: Int

}

fun Guide(guide: GuideObject) = Guide {
    person = Person(guide.person)
    username = guide.username
    password = guide.password
    workedHours = guide.workedHours
}

interface Visitor : Entity<Visitor> {
    companion object : Entity.Factory<Visitor>()

    var person: Person
    var center: String
}

fun Visitor(visitor: VisitorObject) = Visitor {
    person = Person(visitor.person)
    center = visitor.center
}

interface RequestedBooking : Entity<RequestedBooking> {
    companion object : Entity.Factory<RequestedBooking>()

    var requestedDateTime: LocalDateTime
    var visitor: Visitor
    var assistants: Int
    var typeAssistant: AssistantsType
    var comments: String?
    var accepted: Boolean
}

fun RequestedBooking(requestedBookingObject: RequestedBookingObject) = RequestedBooking {
    visitor = Visitor(requestedBookingObject.visitor)
    requestedDateTime = requestedBookingObject.requestedDateTime
    assistants = requestedBookingObject.assistants
    typeAssistant = requestedBookingObject.assistantsType
    comments = requestedBookingObject.comments
    accepted = requestedBookingObject.accepted
}

interface Visit : Entity<Visit> {
    companion object : Entity.Factory<Visit>()

    var visitDateTime: LocalDateTime
    var requestedBooking: RequestedBooking
    var guideEmail: String
    var completed: Boolean
}

fun Visit(visit: VisitObject) = Visit {
    visitDateTime = visit.visitDateTime
    requestedBooking = RequestedBooking(visit.requestedBooking)
    guideEmail = visit.guideEmail
    completed = visit.completed
}

interface Survey : Entity<Survey> {
    companion object : Entity.Factory<Survey>()

    val id: String
    val visit: Visit
    val mark: Int
    val comments: String
}

interface Piece : Entity<Piece> {
    companion object : Entity.Factory<Piece>()

    val person: Person
    val id: String
    val name: String
    val brand: String
    val exposed: Boolean
    val type: PieceType
    val imageURL: String
    val description: String
    val comments: String
    val quantity: Int
}