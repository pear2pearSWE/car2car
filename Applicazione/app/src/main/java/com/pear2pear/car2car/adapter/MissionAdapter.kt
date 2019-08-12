package com.pear2pear.car2car.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.pear2pear.car2car.R
import com.pear2pear.car2car.assets.Mission

class MissionAdapter(val context: Context, private val missions: List<Mission>, private val missionItemClick: (Mission) -> Unit) :
    RecyclerView.Adapter<MissionAdapter.MissionsHolder>() {

    override fun onBindViewHolder(MissionHolder: MissionsHolder, position: Int) {
        MissionHolder.bindMission(missions[position], context)
    }

    override fun getItemCount(): Int {
        return missions.count()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MissionsHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_single_mission, parent, false)
        return MissionsHolder(view, missionItemClick)
    }

    inner class MissionsHolder(itemView: View?, val itemClick: (Mission) -> Unit) : RecyclerView.ViewHolder(itemView) {
        val mIcon = itemView?.findViewById<ImageView>(R.id.icon_single_mission)
        val mCover = itemView?.findViewById<ImageView>(R.id.cover_single_mission)
        val mTitle = itemView?.findViewById<TextView>(R.id.text_title_single_mission)
        val mDescription = itemView?.findViewById<TextView>(R.id.text_description_single_mission)
        val mStatus = itemView?.findViewById<TextView>(R.id.text_status_single_mission)
        val mProgress = itemView?.findViewById<ProgressBar>(R.id.mission_progressBar)

        fun bindMission(mission: Mission, context: Context) {
            mTitle?.text = mission.title
            mDescription?.text = mission.description
            mStatus?.text = mission.prize.toString()
            mProgress?.progress = mission.getStatus().toInt()

            if(mission.imgreward == "tutorial") { mIcon?.setImageResource(R.drawable.icon_mission_tutorial) }
            else if(mission.imgreward == "insertcar") { mIcon?.setImageResource(R.drawable.icon_mission_insertcar) }
            else if(mission.imgreward == "takeone") { mIcon?.setImageResource(R.drawable.icon_mission_takeone) }
            else if(mission.imgreward == "takefive") { mIcon?.setImageResource(R.drawable.icon_mission_takefive) }
            else if(mission.imgreward == "rentone") { mIcon?.setImageResource(R.drawable.icon_mission_rentone) }
            else if(mission.imgreward == "rentfive") { mIcon?.setImageResource(R.drawable.icon_mission_rentfive) }
            else if(mission.imgreward == "onehundredhours") { mIcon?.setImageResource(R.drawable.icon_mission_onehundredhours) }

            if( mission.complete) { mCover?.visibility = View.INVISIBLE }

            itemView.setOnClickListener { itemClick(mission) }
        }
    }
}