package com.example.apollotutorial

import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.example.apollotutorial.client.apolloClient


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT > 9) {
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
            println("*** My thread is now configured to allow connection")
        }

        val apolloClient = apolloClient(this)

        val mutation = AddLocationMutation(0.91, 10.62)

        apolloClient
            .mutate(mutation)
            .enqueue(object : ApolloCall.Callback<AddLocationMutation.Data>() {
                override fun onResponse(response: Response<AddLocationMutation.Data>) {
                    Log.i("onresponse", response.toString());
                }

                override fun onFailure(e: ApolloException) {
                    Log.e("error", e.message, e);
                }
            })

        apolloClient.query(LocationGroupQuery())
            .enqueue(object : ApolloCall.Callback<LocationGroupQuery.Data>() {
                override fun onFailure(e: ApolloException) {
                    Log.i("error", e.toString());
                }

                override fun onResponse(response: Response<LocationGroupQuery.Data>) {
                    Log.e("onresponse", response.data?.locationGroup.toString());
                }
            });
    }
}