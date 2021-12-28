package com.example.stepstobeersv1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var beerAdapter: BeerAdaptor

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

        val steps = getStepCount().total

        tvStepCount.text = steps.toString()




    }

}