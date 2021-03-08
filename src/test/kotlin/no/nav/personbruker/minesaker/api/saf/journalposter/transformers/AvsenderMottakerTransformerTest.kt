package no.nav.personbruker.minesaker.api.saf.journalposter.transformers

import no.nav.personbruker.minesaker.api.common.exception.MissingFieldException
import no.nav.personbruker.minesaker.api.saf.journalposter.objectmothers.AvsenderMottakerObjectMother
import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.`should be instance of`
import org.amshove.kluent.shouldBeNull
import org.amshove.kluent.shouldNotBeNull
import org.junit.jupiter.api.Test

internal class AvsenderMottakerTransformerTest {

    @Test
    fun `Skal transformere alle gyldige verdier, fra ekstern til intern verdi`() {
        val external = AvsenderMottakerObjectMother.giveMePersonSomAvsender("123")

        val internal = external.toInternal()

        internal.id?.value `should be equal to` external.id
        internal.type.shouldNotBeNull()
    }

    @Test
    fun `Skal kaste feil hvis idType ikke er satt`() {
        val externalUtenTypeSatt = AvsenderMottakerObjectMother.giveMePersonSomAvsender(idType = null)

        val result = runCatching {
            externalUtenTypeSatt.toInternal()
        }

        result.isFailure `should be equal to` true
        result.exceptionOrNull() `should be instance of` MissingFieldException::class
        val mfe = result.exceptionOrNull() as MissingFieldException
        mfe.context["feltnavn"] `should be equal to` "avsenderMottakerIdType"
    }

    @Test
    fun `Skal kaste feil hvis id ikke er satt`() {
        val externalUtenTypeSatt = AvsenderMottakerObjectMother.giveMePersonSomAvsender(ident = null)

        val result = runCatching {
            externalUtenTypeSatt.toInternal()
        }

        result.isSuccess `should be equal to` true
        result.getOrNull()?.id.shouldBeNull()
        result.getOrNull()?.type.shouldNotBeNull()
    }

}
