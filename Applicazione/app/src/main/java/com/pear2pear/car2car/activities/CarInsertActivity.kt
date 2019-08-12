package com.pear2pear.car2car.activities

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.graphics.Color
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_insert_auto.*
import java.lang.Exception
import java.util.*
import android.location.Address
import android.location.Geocoder
import android.widget.EditText
import android.widget.Toast

import com.pear2pear.car2car.R
import com.google.android.gms.maps.model.LatLng
import com.pear2pear.car2car.activities.contract.CarInsertContract
import com.pear2pear.car2car.activities.presenter.CarInsertPresenter
import java.io.IOException

class CarInsertActivity : AppCompatActivity(),CarInsertContract.View {
    private var usertoinsert: String = ""

    var jwt:String=""


    var presenter:CarInsertContract.Presenter?=null


    override fun showError() {
        label_code_insert_car.text="Errore inserimento"
        label_code_insert_car.setTextColor(Color.RED)
    }

    override fun initPresenter(token:String) {
        if (presenter == null) {
            presenter = CarInsertPresenter(this)
        }
        presenter?.setCurrentToken(jwt)
        presenter?.attach(this)
    }

    fun searchLocation(): LatLng {
        val locationSearch: EditText = findViewById(R.id.edit_position_insert_car)
        lateinit var location: String
        location = locationSearch.text.toString()
        var addressList: List<Address>? = null
        var latLng: LatLng = LatLng(0.0, 0.0)

        if (location == null || location == "") {
            Toast.makeText(applicationContext, "provide location", Toast.LENGTH_SHORT).show()
        } else {
            val geoCoder = Geocoder(this)
            try {
                addressList = geoCoder.getFromLocationName(location, 1)

            } catch (e: IOException) {
                e.printStackTrace()
            }
            val address = addressList!![0]
            latLng = LatLng(address.latitude, address.longitude)
        }

        return latLng

    }

    override fun showFirstCar() {
        Toast.makeText(this,"Complimenti! hai inserito la tua prima auto, hai ricevuto 40pe",Toast.LENGTH_SHORT).show()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        usertoinsert = intent.getStringExtra("user")
        jwt=intent.getStringExtra("token")
        setContentView(R.layout.activity_insert_auto)
        initPresenter(jwt)
        bt_confirm_insert_car.setOnClickListener {

            val createdCar = com.pear2pear.car2car.assets.Car(
                edit_brand_insert_car.text.toString(),
                edit_model_insert_car.text.toString(),
                edit_plates_insert_car.text.toString(),
                "${searchLocation().latitude}-${searchLocation().longitude}",
                text_registration_date_insert_car.text.toString() + "T22:00:00.000+00:00",
                edit_price_insert_car.text.toString().toDouble(),
                edit_description_insert_car.text.toString(),
                usertoinsert,
                text_start_date_insert_car.text.toString(),
                text_end_date_insert_car.text.toString(),
                false
            )

            presenter!!.insert(createdCar)





        }
        bt_start_date_insert_car.setOnClickListener {
            showCalendar(1)
        }
        bt_end_date_insert_car.setOnClickListener() {
            showCalendar(2)
        }
        bt_registration_date_insert_car.setOnClickListener() {
            showCalendar(3)
        }

    }

    fun showCalendar(int: Int) {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH) + 1
        val day = c.get(Calendar.DAY_OF_MONTH)


        val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, y, m, d ->
            if (int == 1) {
                text_start_date_insert_car.setText(String.format("%s-%s-%s", y, m + 1, d))
            }
            if (int == 2) {
                text_end_date_insert_car.setText(String.format("%s-%s-%s", y, m + 1, d))
            }
            if (int == 3) {
                text_registration_date_insert_car.setText(String.format("%s-%s-%s", y, m + 1, d))
            }


        }, year, month, day)
        dpd.show()
    }

    override fun closeView(){
        this.finish()
    }

}
