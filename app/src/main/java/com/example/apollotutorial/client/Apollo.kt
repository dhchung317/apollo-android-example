package com.example.apollotutorial.client

import android.os.Looper
import com.apollographql.apollo.ApolloClient

private var instance: ApolloClient? = null
private const val SERVER = "https://hyunki-apollo-server.herokuapp.com/"

fun apolloClient(): ApolloClient {
    check(Looper.myLooper() == Looper.getMainLooper()) {
        "Only the main thread can get the apolloClient instance"
    }

    if (instance != null) {
        return instance!!
    }

    instance = ApolloClient.builder()
        .serverUrl(SERVER)
        .build()

    return instance!!
}

//download schema
//./gradlew :app:downloadApolloSchema -Pcom.apollographql.apollo.endpoint="https://hyunki-apollo-server.herokuapp.com/" -Pcom.apollographql.apollo.schema="src/main/graphql/com.example.apollotutorial/schema.json"

