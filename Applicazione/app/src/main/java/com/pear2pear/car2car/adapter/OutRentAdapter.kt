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

class OutRentAdapter(
    val context: Context,
    private val notifications: List<Rent>,
    private val notificationItemClick: (Rent) -> Unit
) : RecyclerView.Adapter<OutRentAdapter.NotificationHolder>() {

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
        val nIcon = itemView?.findViewById<ImageView>(com.pear2pear.car2car.R.id.icon_single_rent)
        val nInfo = itemView?.findViewById<TextView>(com.pear2pear.car2car.R.id.text_single_rent)

        fun bindRent(rent: Rent, context: Context) {
            if (!rent.isAccepted) {
                nIcon?.setImageResource(R.drawable.icon_rent_sent)
                nInfo?.text =
                    "Hai inviato una richiesta a ${rent.toUser} in data  ${rent.date.date}/${rent.date.month + 1}/${rent.date.year + 1900}"
            } else if (rent.isEnded) {
                nIcon?.setImageResource(R.drawable.icon_rent_end)
                nInfo?.text = "Hai concluso il viaggio con successo"
            } else if (rent.isAccepted && !rent.hasKeys) {
                nIcon?.setImageResource(R.drawable.icon_rent_confirm)
                nInfo?.text =
                    "${rent.toUser} Ha confermato il noleggio in data  ${rent.date.date}/${rent.date.month + 1}/${rent.date.year + 1900}\n Conferma quando riceverai le chiavi"
            } else if (rent.isAccepted && rent.hasKeys) {
                nIcon?.setImageResource(R.drawable.icon_rent_recieved_key)
                nInfo?.text = "Hai ricevuto le chiavi da ${rent.toUser}"
            }
            if (rent.reason == "denied") {
                nIcon?.setImageResource(R.drawable.icon_rent_denied)
                nInfo?.text = "${rent.toUser} ha rifiutato la tua richiesta"
            }
            itemView.setOnClickListener { itemClick(rent) }
        }
    }
}