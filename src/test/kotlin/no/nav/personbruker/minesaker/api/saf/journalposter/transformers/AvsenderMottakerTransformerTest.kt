package no.nav.personbruker.minesaker.api.saf.journalposter.transformers

import no.nav.personbruker.minesaker.api.common.exception.MissingFieldException
import no.nav.personbruker.minesaker.api.saf.domain.ID
import no.nav.personbruker.minesaker.api.saf.journalposter.objectmothers.AvsenderMottakerObjectMother
import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.`should be instance of`
import org.amshove.kluent.shouldNotBeNull
import org.junit.jupiter.api.Test

internal class AvsenderMottakerTransformerTest {

    private val dummyIdent = ID("123")

    @Test
    fun `Skal transformere fra ekstern til intern verdi, i tilfeller hvor innlogget bruker er avsender`() {
        val expextedIdent = "123"
        val innloggetBruker = ID(expextedIdent)
        val external = AvsenderMottakerObjectMother.giveMePersonSomAvsender(expextedIdent)

        val internal = external.toInternal(innloggetBruker)

        internal.erSelvAvsender `should be equal to` true
        internal.type.shouldNotBeNull()
    }

    @Test
    fun `Skal transformere fra ekstern til intern verdi, i tilfeller hvor innlogget bruker IKKE er avsender`() {
        val innloggetBruker = ID("456")
        val external = AvsenderMottakerObjectMother.giveMePersonSomAvsender("123")

        val internal = external.toInternal(innloggetBruker)

        internal.erSelvAvsender `should be equal to` false
        internal.type.shouldNotBeNull()
    }

    @Test
    fun `Skal kaste feil hvis idType ikke er satt`() {
        val externalUtenTypeSatt = AvsenderMottakerObjectMother.giveMePersonSomAvsender(idType = null)

        val result = runCatching {
            externalUtenTypeSatt.toInternal(dummyIdent)
        }

        result.isFailure `should be equal to` true
        result.exceptionOrNull() `should be instance of` MissingFieldException::class
        val mfe = result.exceptionOrNull() as MissingFieldException
        mfe.context["feltnavn"] `should be equal to` "avsenderMottakerIdType"
    }

    @Test
    fun `Hvis ID ikke er satt, saa skal erSelvAvsender settes til false`() {
        val externalUtenTypeSatt = AvsenderMottakerObjectMother.giveMePersonSomAvsender(ident = null)

        val internal = externalUtenTypeSatt.toInternal(dummyIdent)

        internal.erSelvAvsender `should be equal to` false
        internal.type.shouldNotBeNull()
    }

}
