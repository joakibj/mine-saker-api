package no.nav.personbruker.minesaker.api.saf.journalposter.transformers

import no.nav.dokument.saf.selvbetjening.generated.dto.HentJournalposter
import no.nav.personbruker.minesaker.api.common.exception.TransformationException
import no.nav.personbruker.minesaker.api.domain.Dokumentkilde
import no.nav.personbruker.minesaker.api.domain.Fodselsnummer

fun HentJournalposter.AvsenderMottaker.toInternal(innloggetBruker: Fodselsnummer) = Dokumentkilde(
    innloggetBrukerErAvsender(innloggetBruker),
    type?.toInternal() ?: throw TransformationException.withMissingFieldName("avsenderMottakerIdType")
)

fun HentJournalposter.AvsenderMottaker.innloggetBrukerErAvsender(innloggetBruker: Fodselsnummer): Boolean {
    return if (avsenderMottakerErEnPrivatperson()) {
        id.equals(innloggetBruker.value)
    } else {
        false
    }
}

private fun HentJournalposter.AvsenderMottaker.avsenderMottakerErEnPrivatperson() =
    type?.let { type -> type == HentJournalposter.AvsenderMottakerIdType.FNR } == true
