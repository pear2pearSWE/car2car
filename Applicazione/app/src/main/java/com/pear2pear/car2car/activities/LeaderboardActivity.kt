package com.pear2pear.car2car.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.Toast
import com.pear2pear.car2car.R
import com.pear2pear.car2car.activities.contract.LeaderboardContract
import com.pear2pear.car2car.activities.presenter.LeaderboardPresenter
import com.pear2pear.car2car.adapter.UserAdapter
import com.pear2pear.car2car.assets.User
import kotlinx.android.synthetic.main.activity_leaderborard.*

class LeaderboardActivity : AppCompatActivity(), LeaderboardContract.View {

    var presenter: LeaderboardContract.Presenter? = null

    var token = ""

    lateinit var adapter: UserAdapter

    override fun initPresenter() {
        if (presenter == null) {
            presenter = LeaderboardPresenter(this)
        }
        presenter?.attach(this)
    }

    override fun setLeaderBoardAdapter(leaderboard: ArrayList<User>) {
        //fino a qua stampa
        adapter = UserAdapter(this, leaderboard) { user ->
            val userIntent = Intent(this, UserProfileActivity::class.java)
            userIntent.putExtra("username", user.user)
            userIntent.putExtra("token", token)
            startActivity(userIntent)
        }
        leaderboard_recycler_view.adapter = adapter
        leaderboard_recycler_view.layoutManager = LinearLayoutManager(this)
        leaderboard_recycler_view.setHasFixedSize(true)
    }

    override fun hideProgressBar() {
        leaderboard_progress_bar.visibility = View.INVISIBLE
    }

    override fun showLeaderBoard() {
        presenter?.downloadLeaderBoard { list ->
            presenter?.setLeaderBoardAdapter(list)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leaderborard)
        if (intent.extras != null) {
            val bundle: Bundle = intent.extras
            token = bundle.getString("token")
        }
        initPresenter()
        presenter!!.setCurrentToken(token)
        showLeaderBoard()
    }

    override fun onRestart() {
        super.onRestart()
        val refresh = Intent(this, LeaderboardActivity::class.java)
        refresh.putExtra("token",token)
        startActivity(refresh)
        this.finish()
    }

    override fun showError() {
        Toast.makeText(this,"Si è verificato un errore", Toast.LENGTH_SHORT).show()
    }
}
