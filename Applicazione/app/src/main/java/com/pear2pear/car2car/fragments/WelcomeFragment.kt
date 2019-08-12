package com.pear2pear.car2car.fragments

import android.app.AlertDialog
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
import co.metalab.asyncawait.async
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession
import com.pear2pear.car2car.R
import com.pear2pear.car2car.activities.LeaderboardActivity
import com.pear2pear.car2car.adapter.MissionAdapter
import com.pear2pear.car2car.assets.Mission
import com.pear2pear.car2car.cognito.CognitoSettings
import com.pear2pear.car2car.fragments.contract.WelcomeContract
import com.pear2pear.car2car.fragments.presenter.WelcomePresenter
import com.pear2pear.car2car.restcalls.GameRestCalls
import kotlinx.android.synthetic.main.fragment_welcome.*
import java.lang.Exception

class WelcomeFragment : Fragment(), WelcomeContract.View {

    var presenter: WelcomeContract.Presenter? = null

    var session: CognitoUserSession? = null

    lateinit var adapter: MissionAdapter

    override fun setCurrentSession(session: CognitoUserSession) {
        this.session = session
    }

    override fun returnCurrentSession(): CognitoUserSession {
        return session!!
    }

    override fun initPresenter() {
        if (presenter == null) {
            presenter = WelcomePresenter(context!!)
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
        val rootView = inflater.inflate(R.layout.fragment_welcome, container, false)

        val btLeaderboard = rootView.findViewById(R.id.bt_leaderboard_welcome) as Button
        btLeaderboard.setOnClickListener {
            val intent = Intent(activity, LeaderboardActivity::class.java)
            intent.putExtra("token",session!!.accessToken.jwtToken)
            startActivity(intent)
        }

        val btReward = rootView.findViewById(R.id.bt_reward_welcome) as Button
        btReward.setOnClickListener {
            async {
                var reward =
                    await { GameRestCalls(session!!.accessToken.jwtToken).getReward(CognitoSettings(view!!.context).getUserPool().currentUser.userId) }

                if (reward >= 0) {
                    showGenericDialog("Congratulazioni!!","Complimenti hai ricevuto un bonus giornaliero di $reward pe")
                } else {
                    Toast.makeText(view!!.context,"Bonus giornaliero gia ottenuto, riprova domani",Toast.LENGTH_SHORT).show()
                }
            }
        }
        return rootView
    }

    fun showGenericDialog(title: String, message: String?) {
        val builder = AlertDialog.Builder(view!!.context)
        builder.setTitle(title)
        builder.setMessage(message)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    override fun setMissionAdapter(missionsList: ArrayList<Mission>) {
        adapter = MissionAdapter(view!!.context, missionsList.reversed()) { mission ->
            if(mission.imgreward == "tutorial" && mission.complete){
                Toast.makeText(view!!.context,"Non portarla in autostrada!\nGUIDA CON PRUDENZA!",Toast.LENGTH_SHORT).show()
            }
        }
        missions_recycle_view.adapter = adapter
        missions_recycle_view.layoutManager = LinearLayoutManager(view!!.context)
        missions_recycle_view.setHasFixedSize(true)
    }

    override fun hideProgressBar() {
        welcome_progress_bar.visibility = View.INVISIBLE
    }

    override fun showMissionsList(user: String) {
        presenter?.downloadMissionsList(user) { list ->
            presenter?.setMissionAdapter(list)
        }
    }

    override fun showWelcomeUsername(username: String) {
        text_user_welcome.text = username
    }

    override fun showWelcomeAvatar(avatar: String) {
        if (avatar == "mouse") { icon_user_welcome.setImageResource(R.drawable.avatar_mouse) }
        else if (avatar == "cat") { icon_user_welcome.setImageResource(R.drawable.avatar_cat) }
        else if (avatar == "dog") { icon_user_welcome.setImageResource(R.drawable.avatar_dog) }
        else if (avatar == "bull") { icon_user_welcome.setImageResource(R.drawable.avatar_bull) }
        else if (avatar == "lion") { icon_user_welcome.setImageResource(R.drawable.avatar_lion) }
        else if (avatar == "giraffe") { icon_user_welcome.setImageResource(R.drawable.avatar_giraffe) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter?.showWelcomeUser()
        presenter?.setCurrentToken(session!!.accessToken.jwtToken)
        showMissionsList(CognitoSettings(view.context).getUserPool().currentUser.userId)
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
