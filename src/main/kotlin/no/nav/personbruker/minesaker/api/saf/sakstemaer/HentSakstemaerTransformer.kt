package no.nav.personbruker.minesaker.api.saf.sakstemaer

import no.nav.dokument.saf.selvbetjening.generated.dto.HentSakstemaer
import no.nav.personbruker.minesaker.api.common.exception.MissingFieldException
import no.nav.personbruker.minesaker.api.saf.domain.Sakstema

object HentSakstemaerTransformer {

    fun toInternal(externalTemaer: List<HentSakstemaer.Sakstema>): List<Sakstema> {
        return externalTemaer.map { external ->
            toInternal(external)
        }
    }

    fun toInternal(external: HentSakstemaer.Sakstema): Sakstema {
        return Sakstema(
            external.navn ?: throw MissingFieldException("navn"),
            external.kode ?: throw MissingFieldException("kode")
        )
    }

}
