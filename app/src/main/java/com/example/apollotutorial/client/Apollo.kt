package com.example.apollotutorial.client

import android.os.Looper
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.subscription.WebSocketSubscriptionTransport
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

private var instance: ApolloClient? = null

private const val SERVER = "https://hyunki-apollo-server.herokuapp.com/"
private const val WS_URL = "wss://hyunki-apollo-server.herokuapp.com/graphql"


fun apolloClient(): ApolloClient {
    check(Looper.myLooper() == Looper.getMainLooper()) {
        "Only the main thread can get the apolloClient instance"
    }

    if (instance != null) {
        return instance!!
    }

    val okHttpClient = OkHttpClient.Builder()
        .pingInterval(1000, TimeUnit.SECONDS)
        .build()

    val subscriptionTransportFactory = WebSocketSubscriptionTransport.Factory(WS_URL, okHttpClient)


    instance = ApolloClient.builder()
        .serverUrl(SERVER + "graphql")
        .okHttpClient(okHttpClient)
        .subscriptionTransportFactory(subscriptionTransportFactory)
        .build()

    return instance!!
}

//download schema
//./gradlew :app:downloadApolloSchema -Pcom.apollographql.apollo.endpoint="https://hyunki-apollo-server.herokuapp.com/" -Pcom.apollographql.apollo.schema="src/main/graphql/com.example.apollotutorial/schema.json"
//
//fun createSubscriptionApolloClient(): ApolloClient {
//    val okHttpClient = OkHttpClient.Builder()
//        .pingInterval(1000, TimeUnit.SECONDS)
//        .build()
//
//    val subscriptionTransportFactory = WebSocketSubscriptionTransport.Factory(WS_URL, okHttpClient)
//
//    return ApolloClient.builder()
//        .serverUrl(SERVER)
//        .okHttpClient(okHttpClient)
//        .subscriptionTransportFactory(subscriptionTransportFactory)
//        .build()
//}

