package no.nav.personbruker.minesaker.api.health

import io.ktor.application.*
import io.ktor.html.*
import io.ktor.http.*
import kotlinx.coroutines.coroutineScope
import kotlinx.html.*
import no.nav.personbruker.minesaker.api.config.Environment
import no.nav.personbruker.minesaker.api.config.HttpClientBuilder

suspend fun ApplicationCall.buildSelftestPage(healthService: HealthService) = coroutineScope {

    val healthChecks = healthService.getHealthChecks()
    val hasFailedChecks = healthChecks.any { healthStatus -> Status.ERROR == healthStatus.status }

    respondHtml(status =
    if(hasFailedChecks) {
        HttpStatusCode.ServiceUnavailable
    } else {
        HttpStatusCode.OK
    })
    {
        head {
            title { +"Selftest mine-saker-api" }
        }
        body {
            var text = if(hasFailedChecks) {
                "FEIL"
            } else {
                "Service-status: OK"
            }
            h1 {
                style = if(hasFailedChecks) {
                    "background: red;font-weight:bold"
                } else {
                    "background: green"
                }
                +text
            }
            table {
                thead {
                    tr { th { +"SELFTEST MINE-SAKER-API" } }
                }
                tbody {
                    healthChecks.map {
                        tr {
                            td { +it.serviceName }
                            td {
                                style = if(it.status == Status.OK) {
                                    "background: green"
                                } else {
                                    "background: red;font-weight:bold"
                                }
                                +it.status.toString()
                            }
                            td { +it.statusMessage }
                        }
                    }
                }
            }
        }
    }
}
