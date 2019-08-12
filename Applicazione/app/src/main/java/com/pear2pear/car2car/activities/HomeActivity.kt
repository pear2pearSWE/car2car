package com.pear2pear.car2car.activities

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.*
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_home.*
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession
import com.pear2pear.car2car.fragments.*
import com.pear2pear.car2car.cognito.CognitoSettings
import com.google.android.gms.maps.model.LatLng
import com.pear2pear.car2car.R.id
import com.pear2pear.car2car.R.layout
import com.pear2pear.car2car.cognito.MyAccountActivity
import com.pear2pear.car2car.cognito.SignUpActivity
import com.pear2pear.car2car.cognito.contract.LoginContract
import com.pear2pear.car2car.cognito.contract.VerifySessionContract
import com.pear2pear.car2car.cognito.presenter.LoginPresenter
import com.pear2pear.car2car.cognito.presenter.VerifySessionPresenter
import com.pear2pear.car2car.gmaps.PositionPresenter
import kotlinx.android.synthetic.main.dialog_login.*
import kotlinx.android.synthetic.main.dialog_login.view.*
import co.metalab.asyncawait.async
import com.pear2pear.car2car.R
import com.pear2pear.car2car.restcalls.GameRestCalls
import kotlinx.android.synthetic.main.dialog_info_logout.*
import kotlinx.android.synthetic.main.dialog_info_logout.view.*
import kotlinx.android.synthetic.main.tutorial_end_layout.*
import kotlinx.android.synthetic.main.tutorial_my_car_layout.*
import kotlinx.android.synthetic.main.tutorial_notification_layout.*
import kotlinx.android.synthetic.main.tutorial_search_layout.*
import kotlinx.android.synthetic.main.tutorial_welcome_layout.*

class HomeActivity : AppCompatActivity(), VerifySessionContract.View, LoginContract.View {

    /*--------------session------------------------------*/
    var session: CognitoUserSession? = null

    /*--------------- fragments -------------------------*/
    private val welcomeFragment: WelcomeFragment = WelcomeFragment()
    private var searchFragment: SearchFragment = SearchFragment()
    private val myCarsFragment: MyCarsFragment = MyCarsFragment()
    private val notificationFragment: NotificationFragment = NotificationFragment()

    /*--------------- dialog -------------------------*/
    var dialog: AlertDialog? = null

    /*--------------presenter----------------------------*/
    var sessionPresenter = VerifySessionPresenter(this)
    var locationPresenter = PositionPresenter(this)

    /*---------------- variabili location ---------------*/
    object CurrentPosition {
        var position: LatLng = LatLng(0.0, 0.0)
    }

    /*-------------- gestione gui -----------------------*/
    fun changeFragment(newFragment: Fragment) {
        if(session!=null)welcomeFragment.setCurrentSession(session!!)
        if (!newFragment.isVisible) {
            val transaction = supportFragmentManager.beginTransaction()

            if ((session == null || !session!!.isValid) && newFragment != searchFragment) //non sono loggato e non guardo la ricerca quindi vado in notlogged
            {
                showLoginDialog()
            } else if ((session == null || !session!!.isValid) && newFragment == searchFragment) { //non sono loggato e vado in ricerca

                transaction.replace(id.fragment_container, newFragment)
            } else {   //sono loggato
                if (newFragment == searchFragment) {
                    searchFragment.setCurrentSession(session)
                }
                transaction.replace(id.fragment_container, newFragment)
            }
            transaction.commit()
        }
        return
    }

    override fun showLogged(session: CognitoUserSession?) {
        this.session = session
        changeFragment(welcomeFragment)
    }

    override fun showNotLogged() {
        changeFragment(searchFragment)
    }

    override fun showProgressBar() {
        progress_bar_home_activity.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        progress_bar_home_activity.visibility = View.INVISIBLE
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(com.pear2pear.car2car.R.menu.menu_top_bar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item.itemId
        when (item.itemId) {
            R.id.my_profile -> {
                if (session != null) {
                    val intent = Intent(this, MyAccountActivity::class.java)
                    intent.putExtra("Username", this.session!!.username)
                    intent.putExtra("token", session!!.accessToken.jwtToken)
                    startActivity(intent)
                }
                return true
            }
            R.id.tutorial -> {
                showWelcomeTutorialDialog()
                return true
            }
            R.id.help -> {
                showInfoBoxDialog()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }

    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            id.navigation_home -> {
                if (session != null) {
                    welcomeFragment.setCurrentSession(session!!)
                } else {
                    println("sessionnonvalida")
                }
                changeFragment(welcomeFragment)
                return@OnNavigationItemSelectedListener true
            }
            id.navigation_dashboard -> {
                if (session != null) {
                    searchFragment.setCurrentSession(session)
                } else {
                    println("sessionnonvalida")
                }
                changeFragment(searchFragment)
                return@OnNavigationItemSelectedListener true
            }
            id.navigation_mycar -> {
                if (session != null) {
                    myCarsFragment.setCurrentSession(session!!)
                } else {
                    println("sessionnonvalida")
                }
                changeFragment(myCarsFragment)
                return@OnNavigationItemSelectedListener true
            }
            id.navigation_notifications -> {
                if (session != null) {
                    notificationFragment.setCurrentSession(session!!)
                } else {
                    println("sessionnonvalida")
                }
                changeFragment(notificationFragment)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(layout.activity_home)
        if (NetworkUtils.isNetworkAvailable(this)) {
            sessionPresenter.verify()
            locationPresenter.start()
            navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        } else {
            showConnectionDialog()
        }

    }

    override fun showInfoBoxDialog() {
        val builder = AlertDialog.Builder(this)
        val infoLogoutDialog = LayoutInflater.from(this).inflate(R.layout.dialog_info_logout, null)
        builder.setView(infoLogoutDialog)
        val dialog: AlertDialog = builder.create()
        if (session != null && session!!.isValid) {
        infoLogoutDialog.bt_logout_account.setOnClickListener(){
                dialog.dismiss()

                val thisUser: CognitoUser = CognitoSettings(this).getUserPool().getUser(this.session!!.username)
                thisUser.signOut()
                session = null
                searchFragment = SearchFragment()
                searchFragment.setCurrentSession(session)
                changeFragment(searchFragment)
            }
        }
        else{
            infoLogoutDialog.bt_logout_account.visibility=View.INVISIBLE
        }
        dialog.show()
    }



    override fun showWelcomeTutorialDialog() {
        val alertDialog = Dialog(this)
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        alertDialog.setContentView(layout.tutorial_welcome_layout)
        alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.bt_next_tutorial_welcome.setOnClickListener(){
            alertDialog.dismiss()
            changeFragment(myCarsFragment)
            myCarsFragment.setCurrentSession(session!!)
            showMyCarTutorialDialog()
        }
        alertDialog.show()
    }

    override fun showSearchTutorialDialog() {
        val alertDialog = Dialog(this)
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        alertDialog.setContentView(layout.tutorial_search_layout)
        alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.bt_next_tutorial_search.setOnClickListener(){
            alertDialog.dismiss()
        }
        alertDialog.show()
    }

    override fun showMyCarTutorialDialog(){
        val alertDialog = Dialog(this)
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        alertDialog.setContentView(layout.tutorial_my_car_layout)
        alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.bt_next_tutorial_my_cars.setOnClickListener(){
            alertDialog.dismiss()
            changeFragment(notificationFragment)
            showNotificationTutorialDialog()
        }
        alertDialog.show()
    }

    override fun showNotificationTutorialDialog(){
        val alertDialog = Dialog(this)
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        alertDialog.setContentView(layout.tutorial_notification_layout)
        alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.bt_next_tutorial_notifications.setOnClickListener(){
            alertDialog.dismiss()
            changeFragment(welcomeFragment)
            showEndTutorialDialog()
        }
        alertDialog.show()
    }

    override fun showEndTutorialDialog(){
        val alertDialog = Dialog(this)
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        alertDialog.setContentView(layout.tutorial_end_layout)
        alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.bt_next_tutorial_end.setOnClickListener(){
            var a=-1
            async{
                await{a=GameRestCalls(session!!.accessToken.jwtToken).completeTutorial(session!!.username)}
                if(a>=0){
                    showTutorialSuccess()
                }
                else{
                    showTutorialError()
                }
            }
            alertDialog.dismiss()

        }
        alertDialog.show()
    }

    fun showTutorialSuccess(){
        welcomeFragment.presenter?.refresh()
        Toast.makeText(this,"Complimenti hai completato il tutorial, hai ricevuto 40pe",Toast.LENGTH_SHORT).show()
    }

    fun showTutorialError(){
        Toast.makeText(this,"Tutorial gia completato, non ti sono stati accreditati i pe ",Toast.LENGTH_SHORT).show()
    }


    fun showConnectionDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("ERRORE")
        builder.setMessage("Errore connessione, assicurati di avere una connessione dati attiva")
        dialog = builder.create()
        dialog?.show()
    }

    fun showLoginDialog() {
        var presenter = LoginPresenter(this)

        val builder = AlertDialog.Builder(this)

        val LoginDialog = LayoutInflater.from(this).inflate(layout.dialog_login, null)

        builder.setView(LoginDialog)

        dialog = builder.create()

        LoginDialog.bt_sign_in_login.setOnClickListener() {
            presenter.verifyLogin(LoginDialog.username_edit.text.toString(), LoginDialog.password_edit.text.toString())
        }

        LoginDialog.bt_sign_up_login.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
        dialog?.show()
    }

    override fun hideDialog(session: CognitoUserSession?) {
        this.session = session
        dialog!!.dismiss()
        changeFragment(welcomeFragment)
    }

    override fun showError() {
        dialog!!.label_error_login.setTextColor(Color.RED)
    }

    override fun onRestart() {
        super.onRestart()
        if(welcomeFragment.presenter != null )
            welcomeFragment.presenter?.refresh()
        if(searchFragment.presenter != null )
            searchFragment.presenter?.refresh()
        if(myCarsFragment.presenter != null )
            myCarsFragment.presenter?.refresh()
        if(notificationFragment.presenter != null )
            notificationFragment.presenter?.refresh()
    }
}