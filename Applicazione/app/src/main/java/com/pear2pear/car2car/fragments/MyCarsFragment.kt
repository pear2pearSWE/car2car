package com.pear2pear.car2car.fragments

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession
import com.pear2pear.car2car.activities.MyCarViewActivity
import com.pear2pear.car2car.activities.CarInsertActivity
import com.pear2pear.car2car.adapter.CarAdapter
import com.pear2pear.car2car.R
import com.pear2pear.car2car.assets.Car
import com.pear2pear.car2car.cognito.CognitoSettings
import com.pear2pear.car2car.fragments.contract.MyCarsContract
import com.pear2pear.car2car.fragments.presenter.MyCarsPresenter
import kotlinx.android.synthetic.main.fragment_my_cars.*
import java.lang.Exception

class MyCarsFragment : Fragment(), MyCarsContract.View {

    var presenter: MyCarsContract.Presenter? = null

    var session: CognitoUserSession? = null

    private var level = 0
    private var numberCars = 0

    lateinit var adapter: CarAdapter

    override fun setCurrentSession(session: CognitoUserSession) {
        this.session = session
    }

    override fun returnCurrentSession(): CognitoUserSession {
        return session!!
    }

    override fun initPresenter() {
        if (presenter == null) {
            presenter = MyCarsPresenter(context!!)
        }
        presenter?.attach(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initPresenter()

    }

    override fun clickedBtAddCar() {
        if( level < 1) {
            Toast.makeText(view!!.context, "Devi salire al livello 1 per inserire la prima auto\nCompleta il tutorial!", Toast.LENGTH_SHORT).show()
        }
        else if( level < 3 && numberCars == 1 ) {
            Toast.makeText(view!!.context, "Devi salire al livello 3 per aggiungere la seconda auto", Toast.LENGTH_SHORT).show()
        }
        else if (level < 5 && numberCars == 2 ) {
            Toast.makeText(view!!.context, "Devi salire al livello 5 per aggiungere tutte le auto che vuoi!", Toast.LENGTH_SHORT).show()
        } else {
            val intent = Intent(activity, CarInsertActivity::class.java)
            intent.putExtra("user", CognitoSettings(view!!.context).getUserPool().currentUser.userId)
            intent.putExtra("token", session!!.accessToken.jwtToken)
            startActivity(intent)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_my_cars, container, false)

        val btAddCar = rootView.findViewById(R.id.bt_add_car_my_cars) as Button
        btAddCar.setOnClickListener {
            clickedBtAddCar()
        }
        return rootView
    }

    override fun setMyCarsAdapter(myCarsList: ArrayList<Car>) {
        adapter = CarAdapter(view!!.context, myCarsList) { car ->
            val carIntent = Intent(view!!.context, MyCarViewActivity::class.java)
            carIntent.putExtra("Id", car.carID)
            carIntent.putExtra("Username", car.username)
            carIntent.putExtra("token",session!!.accessToken.jwtToken)
            startActivity(carIntent)
        }
        recycler_view_my_cars.adapter = adapter
        recycler_view_my_cars.layoutManager = LinearLayoutManager(view!!.context)
        recycler_view_my_cars.setHasFixedSize(true)
    }

    override fun hideProgressBar() {
        my_cars_progress_bar.visibility = View.INVISIBLE
    }

    override fun showLabelNoCars() {
        label_no_cars_my_cars.visibility = View.VISIBLE
    }

    override fun showMyCarsList() {
        presenter?.downloadMyCarsList { list ->
            if (presenter != null) {
                if(list.size > 0) {
                    numberCars = list.size
                    presenter?.setMyCarsAdapter(list)
                }else{
                    hideProgressBar()
                    showLabelNoCars()
                }
            }
        }
    }

    override fun setUserLevel() {
        presenter?.downloadUser { user ->
            level = user.getLevel()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUserLevel()
        presenter?.setCurrentToken(session!!.accessToken.jwtToken)
        showMyCarsList()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter?.detach()
        presenter = null
    }

    override fun returnSession(): CognitoUserSession? {
        return session
    }

    override fun refresh(){
        val ft = getFragmentManager()!!.beginTransaction();
        if (Build.VERSION.SDK_INT >= 26) {
            ft.setReorderingAllowed(false);
        }
        try {
            ft.detach(this).attach(this).commit()
        }catch (e : Exception) {
            e.printStackTrace()
        }
    }

    override fun showError() {
        Toast.makeText(view!!.context,"Si è verificato un errore", Toast.LENGTH_SHORT).show()
    }

}
