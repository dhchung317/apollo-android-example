package com.example.apollotutorial

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.example.apollotutorial.client.apolloClient
import java.time.Duration


class MainActivity : AppCompatActivity() {
    private lateinit var apolloClient: ApolloClient
    private lateinit var sendButton: Button
    private lateinit var latitude: EditText
    private lateinit var longitude: EditText
    private lateinit var goToListButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sendButton = findViewById(R.id.send)
        latitude = findViewById(R.id.latitude_editText)
        longitude = findViewById(R.id.longitude_editText)
        goToListButton = findViewById(R.id.goToList)

        if (Build.VERSION.SDK_INT > 9) {
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
            println("*** My thread is now configured to allow connection")
        }

        apolloClient = apolloClient()

        sendButton.setOnClickListener {
            if(latitude.text.isNotEmpty() && longitude.text.isNotEmpty()){
                addMutation(latitude.text.toString().toDouble(),longitude.text.toString().toDouble())
                latitude.text.clear()
                longitude.text.clear()
                Toast.makeText(this, "location sent!",Toast.LENGTH_SHORT)
            }
        }

        goToListButton.setOnClickListener {
            val intent = Intent(this, LocationActivity::class.java)
            startActivity(intent)
        }

    }

    fun addMutation(latitude: Double, longitude: Double){
        val mutation = AddLocationMutation(latitude, longitude)

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
    }
}