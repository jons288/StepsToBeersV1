package com.example.stepstobeersv1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.ceil

class BeerAdaptor(
    private var beers: MutableList<Int>
) :RecyclerView.Adapter<BeerAdaptor.BeerViewHolder>(){

    class BeerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BeerViewHolder {
        return BeerViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.beerview,
                parent,
                false
            )
        )
    }

    fun convertSteps(steps: Int){
        val beersEarned = ceil(steps.toFloat()/1000.0).toInt()
        if(beersEarned >= 1){
            for(i in 1..beersEarned){
                beers.add(1)
            }
        }
        notifyItemInserted(beers.size - 1)
    }

    fun drinkBeer(){
        beers.removeLastOrNull()
        notifyDataSetChanged()
    }
    override fun onBindViewHolder(holder: BeerViewHolder, position: Int) {
        val curBeer = beers[position]
        holder.itemView.apply{
        }
    }

    override fun getItemCount(): Int {
        return beers.size
    }
}