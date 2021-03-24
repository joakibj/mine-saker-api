package no.nav.personbruker.minesaker.api.saf.journalposter.transformers

import no.nav.dokument.saf.selvbetjening.generated.dto.HentJournalposter
import no.nav.personbruker.minesaker.api.common.exception.MissingFieldException
import no.nav.personbruker.minesaker.api.saf.domain.Dokumentinfo
import no.nav.personbruker.minesaker.api.saf.domain.DokumentInfoId
import no.nav.personbruker.minesaker.api.saf.domain.Tittel

object DokumentInfoTransformer {

    /**
     * Er kun interessert i den arkivert versjonen av dokumentet, da det er denne som er mest vanlig.
     */
    fun toInternal(externals: List<HentJournalposter.DokumentInfo?>?): List<Dokumentinfo> {
        if (externals == null) throw MissingFieldException("dokumenter")

        val internals = mutableListOf<Dokumentinfo>()
        externals
            .filterNotNull()
            .forEach { externalDokument ->
                val externalArkivertDokumentVariant = externalDokument.getEventuellArkivertVariant()
                if (externalArkivertDokumentVariant != null) {
                    val internal = toInternal(externalDokument, externalArkivertDokumentVariant)
                    internals.add(internal)
                }
            }
        return internals
    }

    private fun toInternal(
        external: HentJournalposter.DokumentInfo,
        externalVariant: HentJournalposter.Dokumentvariant
    ) = Dokumentinfo(
        Tittel(external.tittel ?: throw MissingFieldException("tittel")),
        DokumentInfoId(external.dokumentInfoId),
        externalVariant.brukerHarTilgang == true
    )

}

fun HentJournalposter.DokumentInfo.getEventuellArkivertVariant(): HentJournalposter.Dokumentvariant? {
    dokumentvarianter.forEach { variant ->
        if (variant?.hasArkivertVariant() == true) {
            return variant
        }
    }
    return null
}

fun HentJournalposter.Dokumentvariant.hasArkivertVariant(): Boolean {
    return variantformat === HentJournalposter.Variantformat.ARKIV
}
