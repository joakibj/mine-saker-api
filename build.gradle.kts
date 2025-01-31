import com.expediagroup.graphql.plugin.gradle.graphql
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    // Apply the Kotlin JVM plugin to add support for Kotlin on the JVM.
    kotlin("jvm").version(Kotlin.version)
    kotlin("plugin.allopen").version(Kotlin.version)

    id(GraphQL.pluginId) version GraphQL.version

    id(Shadow.pluginId) version (Shadow.version)
    // Apply the application plugin to add support for building a CLI application.
    application
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "13"
    kotlinOptions.freeCompilerArgs = listOf("-Xinline-classes")
}

repositories {
    // Use jcenter for resolving your dependencies.
    // You can declare any Maven/Ivy/file repository here.
    mavenCentral()
    maven("https://packages.confluent.io/maven")
    maven("https://jitpack.io")
    mavenLocal()
}

dependencies {
    implementation(GraphQL.client)
    implementation(Jackson.dataTypeJsr310)
    implementation(Kotlinx.coroutines)
    implementation(Kotlinx.htmlJvm)
    implementation(Ktor.auth)
    implementation(Ktor.authJwt)
    implementation(Ktor.clientApache)
    implementation(Ktor.clientJackson)
    implementation(Ktor.clientJson)
    implementation(Ktor.clientLogging)
    implementation(Ktor.clientLoggingJvm)
    implementation(Ktor.clientSerializationJvm)
    implementation(Ktor.htmlBuilder)
    implementation(Ktor.jackson)
    implementation(Ktor.serverNetty)
    implementation(Logback.classic)
    implementation(Logstash.logbackEncoder)
    implementation(Prometheus.common)
    implementation(Prometheus.hotspot)
    implementation(Prometheus.logback)
    implementation(Tms.KtorTokenSupport.idportenSidecar)
    implementation(Tms.KtorTokenSupport.tokendingsExchange)
    implementation(Tms.KtorTokenSupport.authenticationInstaller)
    implementation(Tms.KtorTokenSupport.tokenXValidation)

    testImplementation(Junit.api)
    testImplementation(Ktor.clientMock)
    testImplementation(Ktor.clientMockJvm)
    testImplementation(Kluent.kluent)
    testImplementation(Kotest.runnerJunit5)
    testImplementation(Kotest.assertionsCore)
    testImplementation(Kotest.extensions)
    testImplementation(Mockk.mockk)
    testImplementation(Jjwt.api)

    testRuntimeOnly(Bouncycastle.bcprovJdk15on)
    testRuntimeOnly(Jjwt.impl)
    testRuntimeOnly(Jjwt.jackson)
    testRuntimeOnly(Junit.engine)
}

application {
    mainClass.set("io.ktor.server.netty.EngineMain")
}

tasks {
    withType<Test> {
        useJUnitPlatform()
        testLogging {
            exceptionFormat = TestExceptionFormat.FULL
            events("passed", "skipped", "failed")
        }
    }

    register("runServer", JavaExec::class) {
        println("Setting default environment variables for running with DittNAV docker-compose")

        environment("SAF_API_URL", "http://localhost:8080/graphql")
        environment("CORS_ALLOWED_ORIGINS", "localhost:9002")

        environment("OIDC_ISSUER", "http://localhost:9000")
        environment("OIDC_DISCOVERY_URL", "http://localhost:9000/.well-known/openid-configuration")
        environment("OIDC_ACCEPTED_AUDIENCE", "stubOidcClient")
        environment("LOGINSERVICE_IDPORTEN_DISCOVERY_URL", "http://localhost:9000/.well-known/openid-configuration")
        environment("LOGINSERVICE_IDPORTEN_AUDIENCE", "stubOidcClient")
        environment("OIDC_CLAIM_CONTAINING_THE_IDENTITY", "pid")

        environment("NAIS_CLUSTER_NAME", "dev-sbs")
        environment("NAIS_NAMESPACE", "personbruker")
        environment("SENSU_HOST", "stub")
        environment("SENSU_PORT", "")

        mainClass.set(application.mainClass)
        classpath = sourceSets["main"].runtimeClasspath
    }
}

graphql {
    client {
        sdlEndpoint = "https://navikt.github.io/safselvbetjening/schema.graphqls"
        packageName = "no.nav.dokument.saf.selvbetjening.generated.dto"
    }
}

apply(plugin = Shadow.pluginId)
