package no.nav.personbruker.minesaker.api.common.exception

open class CommunicationException(message: String, cause: Throwable?) : AbstractMineSakerException(message, cause) {

    constructor(message: String) : this(message, null)

}
