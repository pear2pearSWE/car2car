package com.pear2pear.car2car.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.pear2pear.car2car.R
import com.pear2pear.car2car.assets.User

class UserAdapter(val context: Context, private val leaderboard: ArrayList<User>, private val userItemClick: (User) -> Unit) :
    RecyclerView.Adapter<UserAdapter.LeaderboardHolder>() {

    override fun onBindViewHolder(userHolder: LeaderboardHolder, position: Int) {
        userHolder.bindUser(leaderboard[position], position+1, context)
    }

    override fun getItemCount(): Int {
        return leaderboard.count()

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeaderboardHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_single_user, parent, false)
        return LeaderboardHolder(view, userItemClick)
    }

    inner class LeaderboardHolder(itemView: View?, val itemClick: (User) -> Unit) : RecyclerView.ViewHolder(itemView) {
        val uIcon = itemView?.findViewById<ImageView>(R.id.icon_user_leaderboard)
        val uPosition = itemView?.findViewById<TextView>(R.id.text_position_leaderboard)
        val uName = itemView?.findViewById<TextView>(R.id.text_user_leaderboard)
        val uLevel = itemView?.findViewById<TextView>(R.id.text_level_leaderboard)
        val uPe = itemView?.findViewById<TextView>(R.id.text_pe__leaderboard)

        fun bindUser(user: User, position: Int, context: Context) {
            if (user.avatar == "mouse") { uIcon?.setImageResource(R.drawable.avatar_mouse) }
            else if (user.avatar  == "cat") { uIcon?.setImageResource(R.drawable.avatar_cat) }
            else if (user.avatar  == "dog") { uIcon?.setImageResource(R.drawable.avatar_dog) }
            else if (user.avatar  == "bull") { uIcon?.setImageResource(R.drawable.avatar_bull) }
            else if (user.avatar  == "lion") { uIcon?.setImageResource(R.drawable.avatar_lion) }
            else if (user.avatar  == "giraffe") { uIcon?.setImageResource(R.drawable.avatar_giraffe) }
            uPosition?.text = position.toString()
            uName?.text = user.user
            uLevel?.text = user.getLevel().toString()
            uPe?.text = user.exp.toString()
            itemView.setOnClickListener { itemClick(user) }
        }
    }

}