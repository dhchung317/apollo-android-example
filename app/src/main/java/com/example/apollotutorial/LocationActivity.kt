package com.example.apollotutorial

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import arrow.core.Option
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.example.apollotutorial.client.apolloClient

class LocationActivity : AppCompatActivity() {

    private lateinit var apolloClient: ApolloClient
    private lateinit var rv: RecyclerView
    private lateinit var adapter: LocationsViewAdapter

    private var list: MutableList<LocationGroupQuery.Location> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)

        apolloClient = apolloClient()

        rv = findViewById(R.id.list)
        rv.layoutManager = LinearLayoutManager(this)
        adapter = LocationsViewAdapter(list)
        rv.adapter = adapter

        queryAllLocations()
    }

    fun queryAllLocations() {
        apolloClient.query(LocationGroupQuery())
            .enqueue(object : ApolloCall.Callback<LocationGroupQuery.Data>() {
                override fun onFailure(e: ApolloException) {
                    Log.i("error", e.toString());
                }

                override fun onResponse(response: Response<LocationGroupQuery.Data>) {
                    Log.e("onresponse", response.data?.locationGroup.toString());

                    if(response.data?.locationGroup?.locations?.isNotEmpty()!!){
                        runOnUiThread {
                            adapter.update(response.data?.locationGroup?.locations as List<LocationGroupQuery.Location>)
                        }
                    }
                }
            })
    }
}