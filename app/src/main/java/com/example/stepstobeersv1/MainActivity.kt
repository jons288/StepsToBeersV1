package com.example.stepstobeersv1

import android.content.ContentValues.TAG
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.FitnessOptions
import com.google.android.gms.fitness.data.*
import com.google.android.gms.fitness.request.DataReadRequest
import kotlinx.android.synthetic.main.activity_main.*
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.concurrent.TimeUnit
import java.util.jar.Manifest

class MainActivity : AppCompatActivity() {

    private lateinit var beerAdapter: BeerAdaptor

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        beerAdapter = BeerAdaptor(mutableListOf())

        rvBeers.adapter = beerAdapter
        rvBeers.layoutManager = LinearLayoutManager(this)
        rvBeers.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL ,false)

        btnDrink.setOnClickListener{
            beerAdapter.drinkBeer()
        }

        btnEarn.setOnClickListener{
            val mySteps = etSteps.text.toString()

            if(mySteps.isNotEmpty()){
                val myStepNum = mySteps.toInt()
                beerAdapter.convertSteps(myStepNum)
                etSteps.text.clear()
            }
        }

        if(ContextCompat.checkSelfPermission(this@MainActivity,android.Manifest.permission.ACTIVITY_RECOGNITION)
              != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this@MainActivity,
                arrayOf(android.Manifest.permission.ACTIVITY_RECOGNITION),
                1)


        }
        val FITNESS_OPTIONS: FitnessOptions = FitnessOptions.builder()
            .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
            .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
            .build()

        Fitness.getHistoryClient(this, GoogleSignIn.getAccountForExtension(this, FITNESS_OPTIONS))
            .readDailyTotal(DataType.TYPE_STEP_COUNT_DELTA)
            .addOnSuccessListener { result ->
                val totalSteps =
                    result.dataPoints.firstOrNull()?.getValue(Field.FIELD_STEPS)?.asInt() ?: 0
                // Do something with totalSteps
            }
            .addOnFailureListener { e ->
                Log.i(TAG, "There was a problem getting steps.", e)
            }



    }

}