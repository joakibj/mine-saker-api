package no.nav.personbruker.minesaker.api.saf.queries

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.amshove.kluent.`should contain`
import org.junit.jupiter.api.Test

internal class HentSakerTest {

    private val objectMapper = jacksonObjectMapper()

    private val dummyIdent = "123"

    @Test
    fun `Skal bygge opp en korrekt sporring`() {
        val expectedSakstema = "FOR"
        val expectedFields = "tema { navn kode }"
        val identAsQueryVarible = "\$ident : String!"
        val expectedSakstemaAsInputVariable = """"temaetSomSkalHentes":"$expectedSakstema""""

        val request = HentKonkretSakstema.createRequest(dummyIdent, expectedSakstema)

        val requestAsJson = objectMapper.writeValueAsString(request)

        requestAsJson `should contain` expectedFields
        requestAsJson `should contain` identAsQueryVarible
        requestAsJson `should contain` expectedSakstemaAsInputVariable
    }

}
