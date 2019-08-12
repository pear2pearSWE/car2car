package com.pear2pear.car2car.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.pear2pear.car2car.R.layout
import com.pear2pear.car2car.R.drawable
import com.pear2pear.car2car.activities.contract.ChooseAvatarContract
import com.pear2pear.car2car.activities.presenter.ChooseAvatarPresenter
import kotlinx.android.synthetic.main.activity_choose_avatar.*
import android.content.Intent
import android.support.constraint.ConstraintLayout
import android.view.View
import android.widget.ImageView


class ChooseAvatarActivity: AppCompatActivity(), ChooseAvatarContract.View {
    var presenter: ChooseAvatarContract.Presenter? = null

    var token = ""
    var username = ""
    var level: Int = 0

    override fun initPresenter() {
        if (presenter == null) {
            presenter = ChooseAvatarPresenter(this)
        }
        presenter?.attach(this)
    }

    override fun showUserUsername(username: String) {
        text_username_choose_avatar.text = username
    }

    override fun showUserLevel(level : Int){
        this.level = level
        text_level_choose_avatar.text = level.toString()
    }

    override fun showUserAvatar(avatar: String){
        if (avatar == "mouse") { icon_your_choose_avatar.setImageResource(drawable.avatar_mouse) }
        else if (avatar == "cat") { icon_your_choose_avatar.setImageResource(drawable.avatar_cat) }
        else if (avatar == "dog") { icon_your_choose_avatar.setImageResource(drawable.avatar_dog) }
        else if (avatar == "bull") { icon_your_choose_avatar.setImageResource(drawable.avatar_bull) }
        else if (avatar == "lion") { icon_your_choose_avatar.setImageResource(drawable.avatar_lion) }
        else if (avatar == "giraffe") { icon_your_choose_avatar.setImageResource(drawable.avatar_giraffe) }
    }

    override fun setListener(layout: ConstraintLayout, unlock: Int, avatar: String) {
        layout.setOnClickListener {
            if (level < unlock) {
                Toast.makeText(this, "Il tuo livello è troppo basso", Toast.LENGTH_SHORT).show()
            } else {
                presenter?.changeAvatar(avatar)
                val refresh = Intent(this, ChooseAvatarActivity::class.java)
                refresh.putExtra("token",token)
                startActivity(refresh)
                this.finish()
            }
        }
    }

    override fun setAllListener() {
        setListener(layout_mouse_choose_avatar, 1, "mouse")
        setListener(layout_cat_choose_avatar, 2, "cat")
        setListener(layout_dog_choose_avatar, 3, "dog")
        setListener(layout_bull_choose_avatar, 5, "bull")
        setListener(layout_lion_choose_avatar, 7, "lion")
        setListener(layout_giraffe_choose_avatar, 10, "giraffe")
    }

    override fun hideCover(icon: ImageView, unlock: Int) {
        if(level >= unlock) { icon.visibility = View.INVISIBLE }
    }

    override fun showAllAvatar(){
        hideCover(cover_cat_choose_avatar,2)
        hideCover(cover_dog_choose_avatar,3)
        hideCover(cover_bull_choose_avatar,5)
        hideCover(cover_lion_choose_avatar,7)
        hideCover(cover_giraffe_choose_avatar,10)
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_choose_avatar)
        if (intent.extras != null) {
            val bundle: Bundle = intent.extras
            token = bundle.getString("token")
        }
        initPresenter()
        presenter!!.setCurrentToken(token)

        presenter?.downloadUserInfo()
        setAllListener()
    }

    override fun showError() {
        Toast.makeText(this,"Si è verificato un errore", Toast.LENGTH_SHORT).show()
    }
}