package com.pear2pear.car2car.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.Toast
import com.pear2pear.car2car.R
import com.pear2pear.car2car.activities.contract.UserProfileContract
import com.pear2pear.car2car.activities.presenter.UserProfilePresenter
import com.pear2pear.car2car.adapter.CarAdapter
import com.pear2pear.car2car.assets.Car
import com.pear2pear.car2car.cognito.CognitoSettings
import kotlinx.android.synthetic.main.activity_user_profile.*

class UserProfileActivity: AppCompatActivity(), UserProfileContract.View {

    var presenter: UserProfileContract.Presenter? = null

    var token = ""
    var profileUsername = ""

    lateinit var adapter: CarAdapter

    override fun initPresenter() {
        if (presenter == null) {
            presenter = UserProfilePresenter(this)
        }
        presenter?.attach(this)
    }

    override fun setUserCarsAdapter(carsList: ArrayList<Car>) {
        adapter = CarAdapter(this, carsList) { car ->
            if(car.username == CognitoSettings(this).getUserPool().currentUser.userId) {
                val myCarIntent = Intent(this, MyCarViewActivity::class.java)
                myCarIntent .putExtra("Id", car.carID)
                myCarIntent .putExtra("Username", car.username)
                myCarIntent .putExtra("token", token)
                startActivity(myCarIntent )
            }else if(car.using == false){
                val viewCarIntent = Intent(this, CarViewActivity::class.java)
                viewCarIntent.putExtra("ID", car.carID)
                viewCarIntent.putExtra("user",car.username)
                viewCarIntent.putExtra("token",token)
                startActivity(viewCarIntent)
            }else{
                Toast.makeText(this, "L'auto non è attualmente disponibile", Toast.LENGTH_SHORT).show()
            }
        }
        recycler_view_user_profile.adapter = adapter
        recycler_view_user_profile.layoutManager = LinearLayoutManager(this)
        recycler_view_user_profile.setHasFixedSize(true)
    }

    override fun hideProgressBar() {
        progress_bar_user_profile.visibility = View.INVISIBLE
    }

    override fun showUserCars() {
        presenter?.downloadUserCars(profileUsername) { list ->
            if(list.size > 0)
            presenter?.setUserCarsAdapter(list)
        }
    }

    override fun showUserInfo() {
        presenter?.downloadUserInfo(profileUsername) { user ->
            text_user_user_profile.text = user.user
            text_level_user_profile.text = user.getLevel().toString()
            if (user.avatar == "mouse") { icon_user_user_profile.setImageResource(R.drawable.avatar_mouse) }
            else if (user.avatar  == "cat") { icon_user_user_profile.setImageResource(R.drawable.avatar_cat) }
            else if (user.avatar  == "dog") { icon_user_user_profile.setImageResource(R.drawable.avatar_dog) }
            else if (user.avatar  == "bull") { icon_user_user_profile.setImageResource(R.drawable.avatar_bull) }
            else if (user.avatar  == "lion") { icon_user_user_profile.setImageResource(R.drawable.avatar_lion) }
            else if (user.avatar  == "giraffe") { icon_user_user_profile.setImageResource(R.drawable.avatar_giraffe) }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)
        if (intent.extras != null) {
            val bundle: Bundle = intent.extras
            token = bundle.getString("token")
            profileUsername = bundle.getString("username")
        }
        initPresenter()
        presenter!!.setCurrentToken(token)
        showUserInfo()
        showUserCars()
    }

    override fun onRestart() {
        super.onRestart()
        val refresh = Intent(this, UserProfileActivity::class.java)
        refresh.putExtra("token",token)
        refresh.putExtra("username",profileUsername)
        startActivity(refresh)
        this.finish()
    }

    override fun showError() {
        Toast.makeText(this,"Si è verificato un errore", Toast.LENGTH_SHORT).show()
    }
}
