package fib.museu.di

import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.check.checkModules

class CheckModulesTest : KoinTest {

    @Test
    fun checkAllModules() = checkModules {
        modules(mainModule)
        properties(
            mapOf(
                "DBUsername" to "dummy",
                "DBPassword" to "#DummyDummy1",
                "EmailUsername" to "fake@email.com",
                "EmailPassword" to "fakepassword",
            )
        )
    }
}