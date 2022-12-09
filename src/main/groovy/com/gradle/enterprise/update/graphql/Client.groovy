package com.gradle.enterprise.update.graphql

import groovy.json.JsonSlurper

import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

class Client {
    String token

    Client(String token) {
        this.token = token
    }

    Object request(Query query) {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(new URI("https://api.public.moderne.io/graphql"))
            .header('Content-Type', 'application/json')
            .header('Authorization', "Bearer ${token}")
            .POST(HttpRequest.BodyPublishers.ofString(query.toJson()))
            .build()

        HttpClient httpClient = HttpClient.newHttpClient()
        def response = httpClient.send(request, HttpResponse.BodyHandlers.ofString())

        assert response.statusCode() == 200
        def json = new JsonSlurper().parseText(response.body())
        checkErrors(json)
        return json
    }

    private static void checkErrors(Object json) {
        if (json?.errors) {
            throw new RuntimeException("Errors returned by GraphQL: ${json.errors}")
        }
    }

}
