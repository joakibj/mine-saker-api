package no.nav.personbruker.minesaker.api.common.exception

import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.`should contain`
import org.junit.jupiter.api.Test

internal class TransformationExceptionTest {

    @Test
    fun `ToString-metoden skal inneholde hvilken type feil det er`() {
        val expectedMessage = "Dummy feilmelding"
        val expectedErrorType = TransformationException.ErrorType.MISSING_FIELD

        val exception = TransformationException(expectedMessage, expectedErrorType)
        exception.addContext("dummyKey", "dummyValue")

        exception.type `should be equal to` expectedErrorType
        exception.message `should be equal to` expectedMessage
        println(exception.toString())
        exception.toString() `should contain` expectedErrorType.toString()
    }

    @Test
    fun `Hjelpemetoden for aa opprette TransformationException med feltnavn i konteksten`() {
        val expectedFieldName = "navnet på feltet er dette"

        val exception = TransformationException.withMissingFieldName(expectedFieldName)

        exception.type `should be equal to` TransformationException.ErrorType.MISSING_FIELD
        exception.context[TransformationException.feltnavnKey] `should be equal to` expectedFieldName
        exception.toString() `should contain` expectedFieldName
    }

}
