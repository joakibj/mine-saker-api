package no.nav.personbruker.minesaker.api.saf.journalposter

import no.nav.dokument.saf.selvbetjening.generated.dto.HENT_JOURNALPOSTER
import no.nav.personbruker.minesaker.api.saf.GraphQLRequest
import no.nav.personbruker.minesaker.api.saf.domain.Sakstemakode

class JournalposterRequest(override val variables: Map<String, Any>) : GraphQLRequest {

    override val query: String
        get() = HENT_JOURNALPOSTER.compactJson()

    companion object {
        fun create(ident: String, temaSomSkalHentes: Sakstemakode): JournalposterRequest {
            return JournalposterRequest(
                mapOf(
                    "ident" to ident,
                    "temaetSomSkalHentes" to temaSomSkalHentes.toString(),
                )
            )
        }
    }

}
