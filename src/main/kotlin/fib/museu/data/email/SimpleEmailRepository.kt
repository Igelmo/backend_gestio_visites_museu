package fib.museu.data.email

import fib.museu.domain.datamodels.RequestedBookingObject
import fib.museu.domain.repository.EmailRepository
import org.apache.commons.mail.SimpleEmail

class SimpleEmailRepository(private val email: SimpleEmail) : EmailRepository {

    override fun sendEmail(requestedBooking: RequestedBookingObject, type: Int) {
        val visitor = requestedBooking.visitor
        email.addTo(visitor.visitorEmail)

        when (type) {
            0 -> {
                email.subject = "Acceptació visita museu de la FIB"
                email.setMsg(
                    "Hola " + visitor.visitorEmail + ",\n \n" +
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
            }
            1 -> {
                email.subject = "Denegació visita museu de la FIB"
                email.setMsg(
                    "Hola " + visitor.person.name + ",\n \n" +
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
            else -> {
                email.subject = "Cancel·lació visita museu de la FIB"
                email.setMsg(
                    "Hola " + visitor.person.name + ",\n \n" +
                            "S'ha cancel·lat la visita que havieu demanat pel dia " +
                            requestedBooking.requestedDateTime.dayOfMonth + " a les " +
                            requestedBooking.requestedDateTime.toLocalTime() + ".\n \n" +
                            "Aixó pot ser degut a un imprevist d'última hora que no ens permet realitzar la visita. \n" +
                            "Pots tornar a demanar una visita a la pàgina web. \n" +
                            "En cas de qualsevol dubte, podeu contactar amb nosaltres al correu " +
                            "museu@fib.upc.edu o al telèfon 93 405 41 83 \n \n" +
                            "Moltes gràcies i disculpeu les molèsties, \n \n \n" +
                            "Museu de la FIB"
                )
            }
        }
        email.buildMimeMessage()
        email.sendMimeMessage()
    }
}