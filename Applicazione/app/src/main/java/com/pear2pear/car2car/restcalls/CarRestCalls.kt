package com.pear2pear.car2car.restcalls
import com.pear2pear.car2car.assets.Car
import com.pear2pear.car2car.assets.parser.CarParser
import com.pear2pear.car2car.restcalls.resources.cars.ActiveDateCarsResource
import com.pear2pear.car2car.restcalls.resources.cars.SpecificCarResource
import com.pear2pear.car2car.restcalls.resources.cars.UserCarsResource
import org.json.*
import java.util.*
import kotlin.collections.ArrayList

class CarRestCalls(var token: String? = null) {

    fun insertCar(name:String,c: Car):Boolean{
        val parsed = CarParser(c).toJSON()
        return UserCarsResource(name).request("POST", parsed, token).getBoolean("executed")
    }

    fun deleteCar(name: String, c: Car): Boolean{
        return SpecificCarResource(name, c.carID).request("DELETE", null, token).getBoolean("executed")
    }

    fun getCarsByName(name:String):ArrayList<Car>{
        val jarray = UserCarsResource(name).request("GET", null, token).getJSONArray("data")
        val out = arrayListOf<Car>()
        for (i in 0 until jarray.length()){
            out.add(CarParser(jarray[i] as JSONObject).toCar())
        }
        return out
    }

    fun getCarById(name: String, carId: String):Car{
        val json = SpecificCarResource(name, carId).request("GET", null, token).getJSONObject("data")
        return CarParser(json).toCar()
    }

    fun getActiveByDateExcluding(date:Date,x:Double,y:Double,name:String=""):ArrayList<Car>{
        val jarray = ActiveDateCarsResource(date,x,y).request("GET", null, token).getJSONArray("data")
        val out = arrayListOf<Car>()
        for (i in 0 until jarray.length()){
            out.add(CarParser(jarray[i] as JSONObject).toCar())
        }
        return out.filter { car -> car.username!=name  } as ArrayList<Car>
    }
}