package fib.museu.data

import fib.museu.domain.datamodels.GuideObject
import fib.museu.domain.datamodels.PersonObject
import fib.museu.domain.datamodels.VisitorObject
import fib.museu.domain.repository.PersonRepository
import org.ktorm.database.Database
import org.ktorm.dsl.*

class PersonMySQLRepository(
    private val ktormDatabase: Database
) : PersonRepository {

    override fun getVisitor(email: String) = ktormDatabase.from(Visitors).select()
        .where { Visitors.visitorEmail eq email }
        .limit(0, 1)
        .map { it.asVisitor() }
        .first()

    override fun getGuide(email: String): GuideObject = ktormDatabase.from(Guides).select()
        .where { Guides.guideEmail eq email }
        .limit(0, 1)
        .map { it.asGuide() }
        .first()

    private fun getPerson(email: String) = ktormDatabase.from(Persons).select()
        .where { Persons.email eq email }
        .limit(0, 1)
        .map { it.asPerson() }
        .first()

    override fun setVisitor(visitor: VisitorObject) {
        ktormDatabase.insert(Persons) {
            set(it.email, visitor.visitorEmail)
            set(it.name, visitor.person.name)
            set(it.surname, visitor.person.surname)
            set(it.phone, visitor.person.phone)
        }
        ktormDatabase.insert(Visitors) {
            set(it.visitorEmail, visitor.visitorEmail)
            set(it.center, visitor.center)
        }
    }

    override fun setGuide(guide: GuideObject) {
        ktormDatabase.insert(Persons) {
            set(it.email, guide.guideEmail)
            set(it.name, guide.person.name)
            set(it.surname, guide.person.surname)
            set(it.phone, guide.person.phone)
        }
        ktormDatabase.insert(Guides) {
            set(it.guideEmail, guide.guideEmail)
            set(it.username, guide.username)
            set(it.password, guide.password)
        }
    }

    private fun QueryRowSet.asPerson(): PersonObject {
        return PersonObject(
                    email = get(Persons.email) ?: throw IllegalStateException("Missing Primary key email"),
                    name = get(Persons.name) ?: throw IllegalStateException("Name has to be not null"),
                    surname = get(Persons.surname) ?: throw IllegalStateException("Surname has to be not null"),
                    phone = get(Persons.phone) ?: throw IllegalStateException("Phone has to be not null"),
                )
    }

    private fun QueryRowSet.asVisitor(): VisitorObject {
        val email = get(Visitors.visitorEmail) ?: throw IllegalStateException("Missing Primary key email")

        val person = getPerson(email)

        return ktormDatabase.from(Visitors).select()
            .where { Visitors.visitorEmail eq email }
            .limit(0, 1)
            .map {
                VisitorObject(
                    visitorEmail = email,
                    person = person,
                    center = get(Visitors.center) ?: throw IllegalStateException("Center has to be not null"),
                )
            }
            .first()
    }

    private fun QueryRowSet.asGuide(): GuideObject {
        val email = get(Persons.email) ?: throw IllegalStateException("Missing Primary key email")

        val person = getPerson(email)

        return ktormDatabase.from(Guides).select()
            .where { Guides.guideEmail eq email }
            .limit(0, 1)
            .map {
                GuideObject(
                    guideEmail = email,
                    person = person,
                    username = get(Guides.username) ?: throw IllegalStateException("Name has to be not null"),
                    password = get(Guides.password) ?: throw IllegalStateException("Surname has to be not null"),
                    workedHours = get(Guides.workedHours) ?: throw IllegalStateException("Phone has to be not null"),
                )
            }
            .first()
    }
}

