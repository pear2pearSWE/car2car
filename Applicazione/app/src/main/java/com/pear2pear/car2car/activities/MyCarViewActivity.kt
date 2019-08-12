package com.pear2pear.car2car.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import com.pear2pear.car2car.R
import com.pear2pear.car2car.activities.contract.MyCarViewContract
import com.pear2pear.car2car.activities.presenter.MyCarViewPresenter
import com.pear2pear.car2car.assets.Car
import kotlinx.android.synthetic.main.activity_my_car_view.*
import kotlinx.android.synthetic.main.dialog_delete_account.view.*

class MyCarViewActivity : AppCompatActivity(),MyCarViewContract.View {

    lateinit var id : String
    lateinit var username : String
    var token: String = ""

    var presenter: MyCarViewContract.Presenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_car_view)
        initPresenter()
        if( intent.extras != null ) {
            val bundle: Bundle = intent.extras
            id = bundle.getString("Id")
            username = bundle.getString("Username")
            token = bundle.getString("token")
            presenter!!.getCar(id)

        }
        bt_delete_car.setOnClickListener(){
            presenter!!.initDelete()
        }
    }

    override fun showData(car: Car) {
        if(car.price <= 5){ icon_car_my_car_view.setImageResource(R.drawable.green_car_icon) }
        else if(car.price > 5 && car.price < 10 ) { icon_car_my_car_view.setImageResource(R.drawable.orange_car_icon) }
        else { icon_car_my_car_view.setImageResource(R.drawable.red_car_icon) }
        text_brand_my_car_view.text = car.brand
        text_date_my_car_view.text = car.registrationDate
        text_description_my_car_view.text = car.description
        text_model_my_car_view.text=car.model
        text_plates_my_car_view.text=car.plates
        text_price_my_car_view.text=car.price.toString()
        text_start_sharing_my_car_view.text=car.sharingPeriodStart
        text_end_sharing_my_car_view.text=car.sharingPeriodEnd
        if(car.using!!){
            text_in_use_my_car.text="Si"
        }
        else{
            text_in_use_my_car.text="No"
        }
        hideProgressBar()
    }

    override fun hideProgressBar() {
        progress_bar_my_car.visibility = View.INVISIBLE
    }

    override fun initPresenter() {
        if (presenter == null) {
            presenter = MyCarViewPresenter(this)
        }
        presenter?.attach(this)
    }

    override fun returntoken():String {
        return token
    }

    override fun returnUser():String{
        return username
    }

    override fun closeView() {
        this.finish()
    }

    override fun showDeleteCarDialog(car:Car) {
        val builder = AlertDialog.Builder(this)
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.dialog_delete_account, null)
        builder.setTitle("Eliminazione auto")
        builder.setMessage("Sicuro di voler eliminare la tua ${car.brand} ${car.model}")
        builder.setView(mDialogView)
        val dialog: AlertDialog = builder.create()
        mDialogView.bt_confirm_delete_account.setOnClickListener() {
            presenter!!.deleteCar()
            dialog.dismiss()
            this.finish()
        }
        mDialogView.bt_cancel_delete_account.setOnClickListener() {
            mDialogView.destroyDrawingCache()
            dialog.dismiss()
        }
        dialog.show()
    }
}
