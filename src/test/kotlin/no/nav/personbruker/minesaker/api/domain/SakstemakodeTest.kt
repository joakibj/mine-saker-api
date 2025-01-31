package no.nav.personbruker.minesaker.api.domain

import no.nav.dokument.saf.selvbetjening.generated.dto.HentJournalposter
import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.shouldBeEmpty
import org.amshove.kluent.shouldNotBeNull
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory

internal class SakstemakodeTest {

    private val log = LoggerFactory.getLogger(SakstemakodeTest::class.java)

    @Test
    fun `Skal inneholde alle kjente temakoder fra SAF`() {
        val externalTemaer = HentJournalposter.Tema.values()
        val internalSakstemaerForSAF = Sakstemakode.teamKoderFraSAF()

        val manglendeTemaer = mutableListOf<String>()
        externalTemaer.forEach { external ->

            runCatching {
                if (external.isTemaSomSkalRepresenteresInternt()) {
                    Sakstemakode.valueOf(external.toString())
                }

            }.onFailure {
                manglendeTemaer.add(external.toString())
            }
        }

        if (manglendeTemaer.isNotEmpty()) {
            log.info("manglendeTemaer: $manglendeTemaer")
        }
        manglendeTemaer.shouldBeEmpty()

        externalTemaer.size - 1 `should be equal to` internalSakstemaerForSAF.size
    }

    private fun HentJournalposter.Tema.isTemaSomSkalRepresenteresInternt() =
        this != HentJournalposter.Tema.__UNKNOWN_VALUE

    @Test
    fun `Skal inneholde sakstemakoden for DigiSos`() {
        val kodeForDigiSos = Sakstemakode.valueOf("KOM")
        kodeForDigiSos.shouldNotBeNull()
    }

}
