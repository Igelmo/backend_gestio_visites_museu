package fib.museu.data

import fib.museu.domain.datamodels.GuideObject
import fib.museu.domain.datamodels.VisitorObject
import fib.museu.domain.repository.PersonRepository
import org.ktorm.database.Database
import org.ktorm.dsl.*

class PersonMySQLRepository(
    private val ktormDatabase: Database
) : PersonRepository {

    override fun getVisitor(email: String): VisitorObject = getPerson(email)
        .map { it.asVisitor() }
        .first()

    override fun getGuide(email: String): GuideObject = getPerson(email)
        .map { it.asGuide() }
        .first()

    private fun getPerson(email: String) = ktormDatabase.from(Persons).select()
        .where { Persons.email eq email }
        .limit(0, 1)

    override fun setVisitor(visitor: VisitorObject) {
        ktormDatabase.insert(Persons) {
            set(it.email, visitor.email)
            set(it.name, visitor.name)
            set(it.surname, visitor.surname)
            set(it.phone, visitor.phone)
        }
        ktormDatabase.insert(Visitors) {
            set(it.visitorEmail, visitor.email)
            set(it.center, visitor.center)
            set(it.numVisits, 0)
        }
    }

    override fun setGuide(guide: GuideObject) {
        ktormDatabase.insert(Persons) {
            set(it.email, guide.email)
            set(it.name, guide.name)
            set(it.surname, guide.surname)
            set(it.phone, guide.phone)
        }
        ktormDatabase.insert(Guides) {
            set(it.guideEmail, guide.email)
            set(it.username, guide.username)
            set(it.password, guide.password)
        }
    }

    private fun QueryRowSet.asVisitor(): VisitorObject {
        val email = get(Persons.email) ?: throw IllegalStateException("Missing Primary key email")
        return ktormDatabase.from(Visitors).select()
            .where { Visitors.visitorEmail eq email }
            .limit(0, 1)
            .map {
                VisitorObject(
                    email = email,
                    name = get(Persons.name) ?: throw IllegalStateException("Name has to be not null"),
                    surname = get(Persons.surname) ?: throw IllegalStateException("Surname has to be not null"),
                    phone = get(Persons.phone) ?: throw IllegalStateException("Phone has to be not null"),
                    center = get(Visitors.center) ?: throw IllegalStateException("Center has to be not null"),
                    numVisits = get(Visitors.numVisits) ?: throw IllegalStateException("NumVisits has to be not null"),
                )
            }
            .first()
    }

    private fun QueryRowSet.asGuide(): GuideObject {
        val email = get(Persons.email) ?: throw IllegalStateException("Missing Primary key email")
        return ktormDatabase.from(Guides).select()
            .where { Guides.guideEmail eq email }
            .limit(0, 1)
            .map {
                GuideObject(
                    email = email,
                    name = get(Persons.name) ?: throw IllegalStateException("Name has to be not null"),
                    surname = get(Persons.surname) ?: throw IllegalStateException("Surname has to be not null"),
                    phone = get(Persons.phone) ?: throw IllegalStateException("Phone has to be not null"),
                    username = get(Guides.username) ?: throw IllegalStateException("Name has to be not null"),
                    password = get(Guides.password) ?: throw IllegalStateException("Surname has to be not null"),
                    workedHours = get(Guides.workedHours) ?: throw IllegalStateException("Phone has to be not null"),
                )
            }
            .first()
    }
}

