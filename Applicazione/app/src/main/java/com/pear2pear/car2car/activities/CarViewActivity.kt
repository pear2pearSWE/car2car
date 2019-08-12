package com.pear2pear.car2car.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_car_view.*
import com.pear2pear.car2car.R
import com.pear2pear.car2car.activities.contract.CarViewContract
import com.pear2pear.car2car.activities.presenter.CarViewPresenter
import com.pear2pear.car2car.assets.Car
import com.pear2pear.car2car.assets.Rent
import com.pear2pear.car2car.assets.User
import com.pear2pear.car2car.cognito.CognitoSettings
import kotlinx.android.synthetic.main.activity_car_view.bt_rent_request_car_view
import kotlinx.android.synthetic.main.activity_car_view.calendar_car_view
import kotlinx.android.synthetic.main.activity_car_view.icon_car_car_view
import kotlinx.android.synthetic.main.activity_car_view.text_brand_car_view
import kotlinx.android.synthetic.main.activity_car_view.text_level_car_view
import kotlinx.android.synthetic.main.activity_car_view.text_owner_car_view
import java.util.*

class CarViewActivity : AppCompatActivity(), CarViewContract.View {

    var presenter: CarViewContract.Presenter? = null

    var id = ""
    var user = ""

    var car = Car("", "", "", " ", "", 0.0, "")
    var year = 0
    var month = 0
    var day = 0

    var token: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_car_view)
        initPresenter()

        if (intent.extras != null) {
            val bundle: Bundle = intent.extras
            id = bundle.getString("ID")
            user = bundle.getString("user")
            token = bundle.getString("token")
            presenter!!.setCurrentToken(token)
            presenter!!.getCar(id)
            presenter!!.getUser(user)
        }

        calendar_car_view.setOnDateChangeListener { _, y, m, d ->
            year = y
            month = m
            day = d
        }

        bt_rent_request_car_view.setOnClickListener {
            presenter!!.insertRentRequest(
                Rent(
                    CognitoSettings(this).getUserPool().currentUser.userId,
                    user,
                    id
                )
            )
        }
    }

    override fun showCarData(car: Car) {
        if (car.price <= 5) {
            icon_car_car_view.setImageResource(R.drawable.green_car_icon)
        } else if (car.price > 5 && car.price < 10) {
            icon_car_car_view.setImageResource(R.drawable.orange_car_icon)
        } else {
            icon_car_car_view.setImageResource(R.drawable.red_car_icon)
        }
        //--------------------Text View dati auto-----------------
        text_brand_car_view.text = car.brand
        text_model_car_view.text = car.model
        text_date_car_view.text = car.registrationDate
        text_description_car_view.text = car.description
        text_plates_car_view.text = car.plates
        text_price_car_view.text = car.price.toString()
        text_start_sharing_car_view.text = car.sharingPeriodStart
        text_end_sharing_car_view.text = car.sharingPeriodEnd

        //----------------------dati calendario
        calendar_car_view.minDate = Date(
            car.sharingPeriodStart!!.split("-")[0].toInt() - 1900,
            car.sharingPeriodStart!!.split("-")[1].toInt() - 1,
            car.sharingPeriodStart!!.split("-")[2].toInt()
        ).time
        calendar_car_view.maxDate = Date(
            car.sharingPeriodEnd!!.split("-")[0].toInt() - 1900,
            car.sharingPeriodEnd!!.split("-")[1].toInt() - 1,
            car.sharingPeriodEnd!!.split("-")[2].toInt()
        ).time
    }

    override fun showUserData(user: User) {
        if (user.avatar == "mouse") {
            icon_user_car_view.setImageResource(R.drawable.avatar_mouse)
        } else if (user.avatar == "cat") {
            icon_user_car_view.setImageResource(R.drawable.avatar_cat)
        } else if (user.avatar == "dog") {
            icon_user_car_view.setImageResource(R.drawable.avatar_dog)
        } else if (user.avatar == "bull") {
            icon_user_car_view.setImageResource(R.drawable.avatar_bull)
        } else if (user.avatar == "lion") {
            icon_user_car_view.setImageResource(R.drawable.avatar_lion)
        } else if (user.avatar == "giraffe") {
            icon_user_car_view.setImageResource(R.drawable.avatar_giraffe)
        }
        text_owner_car_view.text = user.user
        text_level_car_view.text = user.getLevel().toString()
        hideProgressBar()
    }

    override fun hideProgressBar() {
        progress_bar_car_view.visibility = View.INVISIBLE
    }

    override fun initPresenter() {
        if (presenter == null) {
            presenter = CarViewPresenter(this)
        }
        presenter?.attach(this)
    }

    override fun returntoken(): String {
        return token
    }

    override fun setViewCar(car: Car) {
        this.car = car
    }

    override fun returnUser(): String {
        return user
    }

    override fun closeView() {
        this.finish()
    }


}
