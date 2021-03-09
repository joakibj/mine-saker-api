package no.nav.personbruker.minesaker.api.saf

import com.expediagroup.graphql.types.GraphQLResponse
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.http.HttpHeaders.Authorization
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import no.nav.dokument.saf.selvbetjening.generated.dto.HentJournalposter
import no.nav.dokument.saf.selvbetjening.generated.dto.HentSakstemaer
import no.nav.personbruker.minesaker.api.common.exception.SafException
import no.nav.personbruker.minesaker.api.saf.domain.Fodselsnummer
import no.nav.personbruker.minesaker.api.saf.domain.Sakstema
import no.nav.personbruker.minesaker.api.saf.journalposter.JournalposterRequest
import no.nav.personbruker.minesaker.api.saf.sakstemaer.SakstemaerRequest
import java.net.URL

class SafConsumer(
    private val httpClient: HttpClient,
    private val safEndpoint: URL
) {

    suspend fun hentSakstemaer(request: SakstemaerRequest, accessToken: String): List<Sakstema> {
        val responseDto: GraphQLResponse<HentSakstemaer.Result> = sendQuery(request, accessToken)
        val data: HentSakstemaer.Result = responseDto.data ?: throw noDataWithContext(responseDto)
        return data.toInternal()
    }

    suspend fun hentJournalposter(innloggetBruker: Fodselsnummer, request: JournalposterRequest, accessToken: String): List<Sakstema> {
        val responseDto = sendQuery<GraphQLResponse<HentJournalposter.Result>>(request, accessToken)
        val data: HentJournalposter.Result = responseDto.data ?: throw noDataWithContext(responseDto)
        return data.toInternal(innloggetBruker)
    }

    private fun noDataWithContext(responseDto: GraphQLResponse<*>) =
        SafException("Ingen data i resultatet fra SAF.")
            .addContext("response", responseDto)

    private suspend inline fun <reified T> sendQuery(request: GraphQLRequest, accessToken: String): T {
        return try {
            withContext(Dispatchers.IO) {
                httpClient.post {
                    url("$safEndpoint/graphql")
                    method = HttpMethod.Post
                    header(Authorization, "Bearer $accessToken")
                    contentType(ContentType.Application.Json)
                    accept(ContentType.Application.Json)
                    body = request
                }
            }

        } catch (e: Exception) {
            val internalException = SafException("Klarte ikke å utføre spørring mot SAF", e)
            internalException.addContext("query", request.query)
            internalException.addContext("variables", request.variables)
            throw internalException
        }

    }

}
