package fib.museu.di

import fib.museu.data.BookingMySQLRepository
import fib.museu.data.PersonMySQLRepository
import fib.museu.domain.datamodels.EmailSender
import fib.museu.domain.repository.BookingRepository
import fib.museu.domain.repository.PersonRepository
import org.apache.commons.mail.DefaultAuthenticator
import org.apache.commons.mail.SimpleEmail
import org.koin.dsl.module
import org.ktorm.database.Database

val mainModule = module {
    single<BookingRepository> {
        BookingMySQLRepository(get(), get())
    }
    single<PersonRepository> {
        PersonMySQLRepository(get())
    }
    single {
        Database.connect(
            "jdbc:mysql://localhost:3306/mydb?useUnicode=true",
            user = getProperty("DBUsername"),
            password = getProperty("DBPassword"),
            driver = "com.mysql.cj.jdbc.Driver"
        )
    }
    single {
        EmailSender(get())
    }
    single {
        SimpleEmail().apply {
            hostName = "smtp.gmail.com"
            setSmtpPort(587)
            isSSLOnConnect = true
            setAuthenticator(DefaultAuthenticator(getProperty("EmailUsername"), getProperty("EmailPassword")))
            setFrom(getProperty("EmailUsername"))
        }
    }
}