package no.nav.personbruker.minesaker.api.saf

import no.nav.personbruker.minesaker.api.common.exception.TransformationException
import no.nav.personbruker.minesaker.api.domain.Fodselsnummer
import org.amshove.kluent.`should be instance of`
import org.amshove.kluent.`should not be null`
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail

internal class ResultTransformerTest {

    private val dummyInnloggetBruker = Fodselsnummer("123")

    @Test
    fun `Skal transformere et SAF-resultat for aa hente inn sakstemaer`() {
        val external = ResultObjectMother.giveMeHentSakstemaResult()

        val internal = external.toInternal()

        internal.`should not be null`()
    }

    @Test
    fun `Skal transformere et SAF-resultat for aa hente inn journalposter`() {
        val external = ResultObjectMother.giveMeHentJournalposterResult()

        val internal = external.toInternal(dummyInnloggetBruker)

        internal.`should not be null`()
    }

    @Test
    fun `Alle feil som skjer skal kastes videre ved henting av sakstemaer`() {
        val eksternalMedValideringsfeil = ResultObjectMother.giveMeHentSakstemaResultMedUfullstendigeData()

        runCatching {
            eksternalMedValideringsfeil.toInternal()

        }.onFailure { exception ->
            exception `should be instance of` TransformationException::class

        }.onSuccess {
            fail("Denne testen skal kaste en feil")
        }
    }

    @Test
    fun `Alle feil som skjer skal kastes videre ved henting av journalposter`() {
        val eksternalMedValideringsfeil = ResultObjectMother.giveMeHentJournalposterResultMedUfullstendigeData()

        runCatching {
            eksternalMedValideringsfeil.toInternal(dummyInnloggetBruker)

        }.onFailure { exception ->
            exception `should be instance of` TransformationException::class

        }.onSuccess {
            fail("Denne testen skal kaste en feil")
        }
    }

}
