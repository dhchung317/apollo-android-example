package com.example.apollotutorial

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.coroutines.toFlow
import com.apollographql.apollo.exception.ApolloException
import com.example.apollotutorial.client.apolloClient
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class LocationActivity : AppCompatActivity() {

    private lateinit var apolloClient: ApolloClient
    private lateinit var rv: RecyclerView
    private lateinit var adapter: LocationsViewAdapter

    private var list: MutableList<LocationGroupQuery.Location> = mutableListOf()

    @InternalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)

        apolloClient = apolloClient()

        rv = findViewById(R.id.list)
        rv.layoutManager = LinearLayoutManager(this)
        adapter = LocationsViewAdapter(list)
        rv.adapter = adapter

        queryAllLocations()
        subscribeToLocations()

    }

    fun queryAllLocations() {
        apolloClient.query(LocationGroupQuery())
            .enqueue(object : ApolloCall.Callback<LocationGroupQuery.Data>() {
                override fun onFailure(e: ApolloException) {
                    Log.i("error", e.toString());
                }

                override fun onResponse(response: Response<LocationGroupQuery.Data>) {
                    Log.e("onresponse", response.data?.locationGroup.toString());

                    if(!response.data?.locationGroup?.locations.isNullOrEmpty()) {
                        list.addAll(response.data?.locationGroup?.locations as List<LocationGroupQuery.Location>)
                        runOnUiThread {
                            adapter.update(response.data?.locationGroup?.locations as List<LocationGroupQuery.Location>)
                        }
                    }
                }
            })
    }

    @ExperimentalCoroutinesApi
    @InternalCoroutinesApi
    fun subscribeToLocations() {
        lifecycleScope.launch {
            apolloClient.subscribe(SubscribeLocationsSubscription()).toFlow().collect {
                runOnUiThread {
                    val location = LocationGroupQuery.Location(
                        "Location", it.data?.locations?.latitude!!, it.data?.locations?.longitude!!)
                        list.add(location)
                        adapter.update(list + location)
                }
            }

        }
    }
}