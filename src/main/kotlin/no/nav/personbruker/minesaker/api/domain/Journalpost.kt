package no.nav.personbruker.minesaker.api.domain

import java.time.ZonedDateTime

data class Journalpost(
    val tittel: Tittel,
    val journalpostId: JournalpostId,
    val journalposttype: Journalposttype,
    val avsender: Dokumentkilde?,
    val mottaker: Dokumentkilde?,
    val sisteEndret: ZonedDateTime,
    val dokumenter: List<Dokumentinfo> = emptyList(),
    val harVedlegg: Boolean = dokumenter.size > 1
)
