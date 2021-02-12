package no.nav.personbruker.minesaker.api.common.sak

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import no.nav.personbruker.minesaker.api.common.respondWithError
import org.slf4j.LoggerFactory

fun Route.sakApi(
    service: SakService
) {

    val log = LoggerFactory.getLogger(SakService::class.java)

    get("/sakstema") {
        try {
            val sakstemakode: String = call.request.queryParameters["kode"]
                ?: throw RuntimeException("Kallet kan ikke utføres uten at tema er valgt.")

            val result = service.hentSakstema(sakstemakode)
            call.respond(HttpStatusCode.OK, result)

        } catch (exception: Exception) {
            respondWithError(call, log, exception)
        }
    }

}
