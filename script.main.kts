#!/usr/bin/env kotlin

@file:Repository("https://repo.maven.apache.org/maven2/")
@file:DependsOn("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.3")

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import kotlin.system.exitProcess

if (args.isEmpty()) {
    println("please enter pokedex number")
    exitProcess(1)
}

val number = args[0]

// cf. https://pokeapi.co/
val pokeUrl = "https://pokeapi.co/api/v2/pokemon/$number"

val client: HttpClient = HttpClient.newBuilder().build()
val request: HttpRequest = HttpRequest.newBuilder()
    .uri(URI.create(pokeUrl))
    .build()
val response: HttpResponse<String> = client.send(request, HttpResponse.BodyHandlers.ofString())

val mapper = jacksonObjectMapper()
val pokemon: Pokemon = mapper.readValue(response.body())

println("id: ${pokemon.id}")
println("name: ${pokemon.name}")
println("image: ${pokemon.sprites.frontDefault}")

@JsonIgnoreProperties(ignoreUnknown = true)
data class Pokemon(
    val id: Int,
    val name: String,
    val sprites: Sprites,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Sprites(
    @JsonProperty("front_default")
    val frontDefault: String,
)
