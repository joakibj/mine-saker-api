package no.nav.personbruker.minesaker.api.saf.journalposter.transformers

import no.nav.personbruker.minesaker.api.saf.journalposter.objectmothers.RelevantDatoObjectMother
import org.amshove.kluent.`should be equal to`
import org.junit.jupiter.api.Test

internal class RelevantDatoTransformerTest {

    @Test
    fun `Skal transformere til intern modell, og plukke ut den datoen som er nyest`() {
        val datoer = RelevantDatoObjectMother.giveMeOneOfEachEkspederRegistertAndOpprettet()

        val nyesteDato = datoer.toInternal()

        nyesteDato `should be equal to` RelevantDatoObjectMother.giveMeDatoForNotat().toInternal()
    }

}
