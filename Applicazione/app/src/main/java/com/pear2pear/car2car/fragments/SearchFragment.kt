package com.pear2pear.car2car.fragments

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.*
import kotlinx.android.synthetic.main.fragment_search.*

import com.google.android.gms.maps.model.*

import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession
import com.pear2pear.car2car.activities.CarViewActivity
import com.pear2pear.car2car.activities.HomeActivity
import com.pear2pear.car2car.R
import com.pear2pear.car2car.adapter.CarAdapter
import com.pear2pear.car2car.assets.Car
import com.pear2pear.car2car.fragments.contract.SearchContract
import com.pear2pear.car2car.fragments.presenter.SearchPresenter
import java.lang.Exception
import kotlin.collections.ArrayList

class SearchFragment : Fragment(), SearchContract.View, OnMapReadyCallback {


    var presenter: SearchContract.Presenter? = null

    var session: CognitoUserSession? = null

    var idcar = ArrayList<String>()
    var user=ArrayList<String>()

    lateinit var adapter: CarAdapter
    private var mMap: MapView? = null

    /* metodi maps */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mMap?.onSaveInstanceState(outState)
    }

    override fun setCurrentSession(newSession: CognitoUserSession?) {
        this.session = newSession
    }

    override fun initPresenter() {
        if (presenter == null) {
            presenter = SearchPresenter(context!!)
        }
        presenter?.attach(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initPresenter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_search, container, false)

        mMap = rootView?.findViewById(R.id.mapview_search) as MapView
        mMap?.onCreate(savedInstanceState)
        mMap?.getMapAsync(this)

        return rootView
    }

    override fun setSearchAdapter(searchList: ArrayList<Car>) {
        println("grafica mvp - trovate ${searchList.size} auto")
        adapter = CarAdapter(view!!.context, searchList) { car ->
            if (session != null) {
                val carIntent = Intent(view!!.context, CarViewActivity::class.java)
                carIntent.putExtra("ID", car.carID)
                carIntent.putExtra("user",car.username)
                carIntent.putExtra("token",session!!.accessToken.jwtToken)
                startActivity(carIntent)
            } else {
                Toast.makeText(activity, "Devi essere autenticato per poter prenotare un'auto", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        recycler_view_search.adapter = adapter
        recycler_view_search.layoutManager = LinearLayoutManager(view!!.context)
        recycler_view_search.setHasFixedSize(true)
    }

    override fun hideProgressBar() {
        search_progress_bar.visibility = View.INVISIBLE
    }

    override fun showLabelNoCars() {
        label_no_cars_search.visibility = View.VISIBLE
    }
    override fun showSearchList() {
        presenter?.downloadSearchList { list ->
            if(list.size > 0 ) {
                presenter?.setSearchAdapter(list)
            } else {
                hideProgressBar()
                showLabelNoCars()
            }

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showSearchList()
    }

    /* carlo altri metodi maps */
    override fun onResume() {
        super.onResume()
        mMap?.onResume()
    }

    override fun onPause() {
        super.onPause()
        mMap?.onPause()
    }

    override fun onStart() {
        super.onStart()
        mMap?.onStart()
    }

    override fun onStop() {
        super.onStop()
        mMap?.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mMap?.onDestroy()
        presenter?.detach()
        presenter = null
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mMap?.onLowMemory()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        presenter?.downloadSearchList { list ->
            if (list.size != 0) {
                for (i in 0 until list.size) {
                    idcar.add(list[i].carID)
                    user.add(list[i].username)
                    val title: String = list[i].brand + "/${i}"
                    var markerx: MarkerOptions? = MarkerOptions()
                        .position(
                            LatLng(
                                list[i].position.split("-")[0].toDouble(),
                                list[i].position.split("-")[1].toDouble()
                            )
                        )
                        .title(title)
                        .snippet(list[i].model)

                    googleMap.addMarker(markerx)
                }
                googleMap.setOnInfoWindowClickListener(infoWindowClickListener)

                googleMap.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(

                        HomeActivity.CurrentPosition.position
                        , 15f
                    )
                )
            }
        }
    }


    private val infoWindowClickListener = object : GoogleMap.OnInfoWindowClickListener {
        override fun onInfoWindowClick(p0: Marker?) {
            val i = p0!!.title.split("/")[1].toInt()
            println("OpenCar+${idcar[i]}+${user[i]}")
            openCarActivity(idcar[i],user[i])
        }
    }

    fun openCarActivity(idCar: String,user:String) {
        if (session != null) {
            val intent = Intent(activity, CarViewActivity::class.java)
            intent.putExtra("ID", idCar)
            intent.putExtra("user",user)
            intent.putExtra("token",session!!.accessToken.jwtToken)
            startActivity(intent)
        } else {
            Toast.makeText(activity, "Devi essere autenticato per poter prenotare un'auto", Toast.LENGTH_SHORT).show()
        }
    }

    override fun returnSession(): CognitoUserSession? {
        return session
    }

    override fun refresh(){
        val ft = getFragmentManager()!!.beginTransaction()
        if (Build.VERSION.SDK_INT >= 26) {
            ft.setReorderingAllowed(false)
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







