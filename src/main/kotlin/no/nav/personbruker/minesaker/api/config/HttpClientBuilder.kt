package no.nav.personbruker.minesaker.api.config

import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.HttpTimeout

object HttpClientBuilder {

    fun build(): HttpClient {
        return config()
    }

    fun config() = HttpClient(Apache) {
        install(JsonFeature) {
            serializer = buildJsonSerializer()
        }
        install(HttpTimeout)
    }

}
