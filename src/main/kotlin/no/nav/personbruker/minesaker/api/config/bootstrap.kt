package no.nav.personbruker.minesaker.api.config

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.client.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.jackson.*
import io.ktor.routing.*
import io.ktor.util.*
import io.ktor.util.pipeline.*
import io.prometheus.client.hotspot.DefaultExports
import no.nav.personbruker.minesaker.api.common.sak.sakApi
import no.nav.personbruker.minesaker.api.health.healthApi
import no.nav.tms.token.support.idporten.installIdPortenAuth
import no.nav.tms.token.support.idporten.user.IdportenUserFactory

@KtorExperimentalAPI
fun Application.mainModule(appContext: ApplicationContext = ApplicationContext()) {

    DefaultExports.initialize()

    install(DefaultHeaders)

    install(CORS) {
        host(appContext.environment.corsAllowedOrigins)
        allowCredentials = true
        header(HttpHeaders.ContentType)
    }

    installIdPortenAuth {
        tokenCookieName = "mine_saker_api_token"
        setAsDefault = true
    }

    install(ContentNegotiation) {
        jackson {
            enableMineSakerJsonConfig()
        }
    }

    routing {
        healthApi(appContext.healthService)

        authenticate {
            sakApi(appContext.sakService)
        }

        configureShutdownHook(appContext.httpClient)
    }
}

private fun Application.configureShutdownHook(httpClient: HttpClient) {
    environment.monitor.subscribe(ApplicationStopping) {
        httpClient.close()
    }
}

val PipelineContext<*, ApplicationCall>.idportenUser get() = IdportenUserFactory.createIdportenUser(call)
