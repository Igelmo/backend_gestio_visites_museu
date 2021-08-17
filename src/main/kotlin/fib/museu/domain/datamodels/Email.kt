package fib.museu.domain.datamodels

import org.apache.commons.mail.DefaultAuthenticator
import org.apache.commons.mail.SimpleEmail

class Email() {
    private val email = SimpleEmail()

    init {
        email.hostName = "smtp.gmail.com"
        email.setSmtpPort(587)
        email.isSSLOnConnect = true
    }

    fun sendEmail(requestedBooking: RequestedBookingObject, accept: Boolean) {
        val visitor = requestedBooking.visitor
        email.setAuthenticator(DefaultAuthenticator("gmailUsername", "gmailPassword"))
        email.setFrom(System.getProperty("gmailUsername"))
        email.addTo(visitor.email)

        if (accept) {
            email.subject = "Acceptació visita museu de la FIB"
            email.setMsg(
                "Hola " + visitor.name + ",\n \n" +
                        "S'ha acceptat la visita que havieu demanat pel dia " +
                        requestedBooking.requestedDateTime.dayOfMonth + " a les " +
                        requestedBooking.requestedDateTime.toLocalTime() + ".\n" +
                        "Recordem que l'aforament màxim és de 30 assistents per visita " +
                        "i que les visites solen durar aproximadament una hora. \n" +
                        "En cas de qualsevol dubte o problema no dubteu a contactar amb nosaltres " +
                        "al correu museu@fib.upc.edu o al telèfon 93 405 41 83. \n \n" +
                        "Finalment, en cas de voler cancel·lar la visita, pot entrar a l'enllaç següent: ENLLAÇ AQUÍ \n" +
                        "El codi per a cancel·lar la visita és el CODI CANCEL·LACIÓ.\n \n" +
                        "Moltes gràcies per tot i salutacions, \n \n \n" +
                        "Museu de la FIB"
            )
        } else {
            email.subject = "Denegació visita museu de la FIB"
            email.setMsg(
                "Hola " + visitor.name + ",\n \n" +
                        "S'ha denegat la visita que havieu demanat pel dia " +
                        requestedBooking.requestedDateTime.dayOfMonth + " a les " +
                        requestedBooking.requestedDateTime.toLocalTime() + ".\n \n" +
                        "Aixó pot ser degut a que la franja sol·licitada just es troba ocupada o que el centre " +
                        visitor.center + " es troba sancionat per algún motiu. \n" +
                        "Si tot i intentant-ho novament a una data diferent persisteix el problema, o no sap si " +
                        "el seu centre pot estar sancionat, no dubteu a contactar amb nosaltres " +
                        "al correu museu@fib.upc.edu o al telèfon 93 405 41 83 \n \n" +
                        "Moltes gràcies i disculpeu les molèsties, \n \n \n" +
                        "Museu de la FIB"
            )
        }
        email.send()
    }
}


