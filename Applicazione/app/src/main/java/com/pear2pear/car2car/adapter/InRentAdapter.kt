package com.pear2pear.car2car.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.pear2pear.car2car.R
import com.pear2pear.car2car.assets.Rent

class InRentAdapter(
    val context: Context,
    private val notifications: List<Rent>,
    private val notificationItemClick: (Rent) -> Unit
) :
    RecyclerView.Adapter<InRentAdapter.NotificationHolder>() {

    override fun onBindViewHolder(RentHolder: NotificationHolder, position: Int) {
        RentHolder.bindRent(notifications[position], context)
    }

    override fun getItemCount(): Int {
        return notifications.count()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_single_rent, parent, false)
        return NotificationHolder(view, notificationItemClick)
    }

    inner class NotificationHolder(itemView: View?, val itemClick: (Rent) -> Unit) : RecyclerView.ViewHolder(itemView) {
        val nIcon = itemView?.findViewById<ImageView>(R.id.icon_single_rent)
        val nInfo = itemView?.findViewById<TextView>(R.id.text_single_rent)

        fun bindRent(rent: Rent, context: Context) {
            println("aaaab${rent.isAccepted}+${rent.hasKeys}+${rent.isEnded}")
            if (!rent.isAccepted && rent.reason != "denied") {
                nIcon?.setImageResource(R.drawable.icon_rent_recieved)
                nInfo?.text =
                    "Hai una richiesta da parte di ${rent.user} in data ${rent.date.date}/${rent.date.month + 1}/${rent.date.year + 1900} \nClicca per confermare "
            } else if (rent.isEnded && rent.reason != "denied") {
                nIcon?.setImageResource(R.drawable.icon_rent_recieved_key)
                nInfo?.text = "${rent.user} ha riconsegnato le chiavi \nViaggio concluso"
            } else if (rent.isAccepted && !rent.hasKeys && rent.reason != "denied") {
                nIcon?.setImageResource(R.drawable.icon_rent_confirm)
                nInfo?.text =
                    "Hai confermato la richiesta di ${rent.user} in data  ${rent.date.date}/${rent.date.month + 1}/${rent.date.year + 1900}\nIn attesa di scambio chiavi..."
            } else if (rent.isAccepted && rent.hasKeys && rent.reason != "denied") {
                nIcon?.setImageResource(R.drawable.icon_rent_sent_key)
                nInfo?.text = "Hai ceduto le chiavi a ${rent.user}\nQuando saranno riconsegnate clicca qui"
            } else if (rent.reason == "denied") {
                nIcon?.setImageResource(R.drawable.icon_rent_denied)
                nInfo?.text = "Hai rifiutato la richiesta di ${rent.user}"
            }
            itemView.setOnClickListener { itemClick(rent) }
        }
    }

}