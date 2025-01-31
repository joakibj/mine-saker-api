package no.nav.personbruker.minesaker.api.saf.journalposter.transformers

import no.nav.dokument.saf.selvbetjening.generated.dto.HentJournalposter
import no.nav.personbruker.minesaker.api.saf.common.transformers.finnSistEndret
import no.nav.personbruker.minesaker.api.saf.common.transformers.toInternal
import java.time.ZonedDateTime

fun List<HentJournalposter.RelevantDato?>.toInternal(): ZonedDateTime {
    val internal = filterNotNull().map { external -> external.toInternal() }
    return internal.finnSistEndret()
}

fun HentJournalposter.RelevantDato.toInternal() : ZonedDateTime = dato.toInternal()
