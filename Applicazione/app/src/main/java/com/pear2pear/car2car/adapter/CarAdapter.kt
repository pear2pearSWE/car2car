package com.pear2pear.car2car.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.pear2pear.car2car.R
import com.pear2pear.car2car.assets.Car

class CarAdapter(val context: Context, private val cars: List<Car>, private val carItemClick: (Car) -> Unit) :
    RecyclerView.Adapter<CarAdapter.CarHolder>() {

    override fun onBindViewHolder(holder: CarHolder, position: Int) {
        holder.bindCar(cars[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_single_car, parent, false)
        return CarHolder(view, carItemClick)
    }

    inner class CarHolder(itemView: View?, val itemClick: (Car) -> Unit) : RecyclerView.ViewHolder(itemView) {
        val cIcon = itemView?.findViewById<ImageView>(R.id.icon_single_car)
        val cBrand = itemView?.findViewById<TextView>(R.id.text_brand_single_car)
        val cModel = itemView?.findViewById<TextView>(R.id.text_model_single_car)
        val cUsername = itemView?.findViewById<TextView>(R.id.text_owner_single_car)
        val cPrice = itemView?.findViewById<TextView>(R.id.text_price_single_car)

        fun bindCar(car: Car) {
            if(car.price <= 5 ) { cIcon?.setImageResource(R.drawable.green_car_icon) }
            else if(car.price > 5 && car.price < 10) { cIcon?.setImageResource(R.drawable.orange_car_icon) }
            else { cIcon?.setImageResource(R.drawable.red_car_icon) }
            cBrand?.text = car.brand
            cModel?.text = car.model
            cUsername?.text = car.username
            cPrice?.text = (car.price).toString()
            itemView.setOnClickListener { itemClick(car) }
        }
    }

    override fun getItemCount(): Int {
        return cars.count()
    }

}