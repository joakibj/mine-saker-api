package no.nav.personbruker.minesaker.api.saf.journalposter.transformers

import no.nav.dokument.saf.selvbetjening.generated.dto.HentJournalposter
import no.nav.personbruker.minesaker.api.common.exception.MissingFieldException
import no.nav.personbruker.minesaker.api.saf.domain.AvsenderMottaker

fun HentJournalposter.AvsenderMottaker.toInternal() = AvsenderMottaker(
    id,
    type?.toInternal() ?: throw MissingFieldException("avsenderMottakerIdType")
)
