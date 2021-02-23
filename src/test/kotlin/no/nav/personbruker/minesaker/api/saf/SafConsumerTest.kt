package no.nav.personbruker.minesaker.api.saf

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import no.nav.personbruker.minesaker.api.common.exception.SafException
import no.nav.personbruker.minesaker.api.config.buildJsonSerializer
import no.nav.personbruker.minesaker.api.saf.dto.`in`.objectmother.HentJournalposterResultObjectMother
import no.nav.personbruker.minesaker.api.saf.dto.`in`.objectmother.HentSakstemaerObjectMother
import no.nav.personbruker.minesaker.api.saf.domain.MinimaltSakstema
import no.nav.personbruker.minesaker.api.saf.requests.JournalposterRequest
import no.nav.personbruker.minesaker.api.saf.requests.SakstemaerRequest
import org.amshove.kluent.*
import org.junit.jupiter.api.Test
import java.net.URL

internal class SafConsumerTest {

    private val objectMapper = jacksonObjectMapper()
    private val safDummyEndpoint = URL("https://www.dummy.no")
    private val dummyIdent = "123"

    @Test
    fun `Skal kunne hente alle sakstemaer, for en konkret bruker`() {
        val externalResponse = HentSakstemaerObjectMother.giveMeOneResult()
        val safResponseAsJson = objectMapper.writeValueAsString(externalResponse)
        val mockHttpClient = createMockHttpClient {
            respond(
                safResponseAsJson,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            )
        }
        val safConsumerWithResponse = SafConsumer(mockHttpClient, safEndpoint = safDummyEndpoint)

        val sakstemaRequest = SakstemaerRequest.create(dummyIdent)

        val internalSakstema = runBlocking {
            safConsumerWithResponse.hentSakstemaer(sakstemaRequest)
        }

        val externalSakstema = externalResponse.data!!.dokumentoversiktSelvbetjening.tema
        internalSakstema.size `should be equal to` externalSakstema.size
        internalSakstema[0] `should be instance of` MinimaltSakstema::class
        internalSakstema[0].navn `should be equal to` externalSakstema[0].navn
        internalSakstema[0].kode.`should be equal to`(externalSakstema[0].kode)
        internalSakstema `should not be equal to` externalSakstema
    }

    @Test
    fun `Skal kunne hente all info om et konkret sakstema, for en konkret bruker`() {
        val externalResponse = HentJournalposterResultObjectMother.giveMeOneResult()
        val safResponseAsJson = objectMapper.writeValueAsString(externalResponse)
        val mockHttpClient = createMockHttpClient {
            respond(
                safResponseAsJson,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            )
        }
        val safConsumerWithResponse = SafConsumer(mockHttpClient, safEndpoint = safDummyEndpoint)

        val sakstemaRequest = JournalposterRequest.create(dummyIdent, "FOR")

        val internalSakstema = runBlocking {
            safConsumerWithResponse.hentJournalposter(sakstemaRequest)
        }

        val externalSakstema = externalResponse.data!!.dokumentoversiktSelvbetjening.tema
        internalSakstema.size `should be equal to` externalSakstema.size
        internalSakstema[0] `should be instance of` MinimaltSakstema::class
        internalSakstema[0].navn `should be equal to` externalSakstema[0].navn
        internalSakstema[0].kode `should be equal to` externalSakstema[0].kode
        internalSakstema `should not be equal to` externalSakstema
    }

    @Test
    fun `Skal takle feil mot SAF, lagre kontekst og kaste intern feil videre`() {
        val mockHttpClient = createMockHttpClient {
            respondError(HttpStatusCode.BadRequest)
        }

        val safConsumerSomFeiler = SafConsumer(mockHttpClient, safEndpoint = safDummyEndpoint)

        val sakstemaRequest = JournalposterRequest.create(dummyIdent, "FOR")

        val result = runCatching {
            runBlocking {
                safConsumerSomFeiler.hentJournalposter(sakstemaRequest)
            }
        }

        result.isFailure `should be equal to` true
        val receivedException = result.exceptionOrNull()
        receivedException `should be instance of` SafException::class
        val se = receivedException as SafException
        se.context.shouldHaveKey("query")
        se.context.shouldHaveKey("variables")
        se.context["query"] `should be equal to` sakstemaRequest.query
        se.context["variables"] `should be equal to` sakstemaRequest.variables
    }

    @Test
    fun `Skal kaste intern feil videre ved tomt data-felt for ved henting av konkret sakstema`() {
        val externalErrorResponse = HentJournalposterResultObjectMother.giveMeResponseWithError()
        val safErrorResponseAsJson = objectMapper.writeValueAsString(externalErrorResponse)
        val mockHttpClient = createMockHttpClient {
            respond(
                safErrorResponseAsJson,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            )
        }
        val safConsumerWithResponse = SafConsumer(mockHttpClient, safEndpoint = safDummyEndpoint)

        val sakstemaRequest = SakstemaerRequest.create(dummyIdent)


        val result = runCatching {
            runBlocking {
                safConsumerWithResponse.hentSakstemaer(sakstemaRequest)
            }
        }

        result.isFailure `should be equal to` true
        result.exceptionOrNull() `should be instance of` SafException::class
        val safException = result.exceptionOrNull() as SafException
        val expectedKey = "response"
        safException.context shouldHaveKey expectedKey
        safException.context[expectedKey].shouldNotBeNull()
    }

    @Test
    fun `Skal kaste intern feil videre ved tomt data-felt for henting av alle sakstemaer`() {
        val externalErrorResponse = HentSakstemaerObjectMother.giveMeResponseWithError()
        val safErrorResponseAsJson = objectMapper.writeValueAsString(externalErrorResponse)
        val mockHttpClient = createMockHttpClient {
            respond(
                safErrorResponseAsJson,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            )
        }
        val safConsumerWithResponse = SafConsumer(mockHttpClient, safEndpoint = safDummyEndpoint)

        val sakstemaRequest = SakstemaerRequest.create(dummyIdent)


        val result = runCatching {
            runBlocking {
                safConsumerWithResponse.hentSakstemaer(sakstemaRequest)
            }
        }

        result.isFailure `should be equal to` true
        result.exceptionOrNull() `should be instance of` SafException::class
        val safException = result.exceptionOrNull() as SafException
        val expectedKey = "response"
        safException.context shouldHaveKey expectedKey
        safException.context[expectedKey].shouldNotBeNull()
    }

    private fun createMockHttpClient(respond: MockRequestHandleScope.() -> HttpResponseData): HttpClient {
        return HttpClient(MockEngine) {
            engine {
                addHandler {
                    respond()
                }
            }
            install(JsonFeature) {
                serializer = buildJsonSerializer()
            }
        }
    }

}
