package no.nav.personbruker.minesaker.api.saf.journalposter.transformers

import no.nav.personbruker.minesaker.api.common.exception.TransformationException
import no.nav.personbruker.minesaker.api.domain.Fodselsnummer
import no.nav.personbruker.minesaker.api.saf.journalposter.objectmothers.JournalpostObjectMother
import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.`should be instance of`
import org.amshove.kluent.shouldNotBeEmpty
import org.amshove.kluent.shouldNotBeNull
import org.junit.jupiter.api.Test

internal class JournalpostTransformerTest {

    private val dummyIdent = Fodselsnummer("123")

    @Test
    fun `Skal transformere til intern type`() {
        val external = JournalpostObjectMother.giveMeOneInngaaendeDokument()

        val internal = external.toInternal(dummyIdent)

        internal.shouldNotBeNull()
        internal.tittel.value `should be equal to` external.tittel
        internal.journalpostId.value `should be equal to` external.journalpostId

        internal.dokumenter.shouldNotBeEmpty()
        internal.avsender.shouldNotBeNull()
        internal.mottaker.shouldNotBeNull()
        internal.sisteEndret.shouldNotBeNull()
        internal.journalposttype.shouldNotBeNull()
    }

    @Test
    fun `Skal takle at tittel ikke er tilgjengelig i SAF, return dummy tittel til sluttbruker`() {
        val external = JournalpostObjectMother.giveMeOneInngaaendeDokument(tittel = null)

        val result = external.toInternal(dummyIdent)

        result.tittel.value `should be equal to` "Uten tittel"
    }

    @Test
    fun `Skal kaste feil hvis dokumentlisten er null`() {
        val external = JournalpostObjectMother.giveMeOneInngaaendeDokument(dokumenter = null)

        val result = runCatching {
            external.toInternal(dummyIdent)
        }

        result.isFailure `should be equal to` true
        result.exceptionOrNull() `should be instance of` TransformationException::class
        val exception = result.exceptionOrNull() as TransformationException
        exception.context[TransformationException.feltnavnKey] `should be equal to` "dokumenter"
    }

}
