package com.pear2pear.car2car.fragments

import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import co.metalab.asyncawait.async
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession
import com.pear2pear.car2car.R
import com.pear2pear.car2car.adapter.InRentAdapter
import com.pear2pear.car2car.adapter.OutRentAdapter
import com.pear2pear.car2car.assets.Rent
import com.pear2pear.car2car.cognito.CognitoSettings
import com.pear2pear.car2car.fragments.contract.NotificationContract
import com.pear2pear.car2car.fragments.presenter.NotificationPresenter
import com.pear2pear.car2car.restcalls.RentRestCalls
import kotlinx.android.synthetic.main.dialog_confirm_rent.view.*
import kotlinx.android.synthetic.main.fragment_notification.*
import java.lang.Exception

class NotificationFragment : Fragment(), NotificationContract.View {

    var presenter: NotificationContract.Presenter? = null

    var session: CognitoUserSession? = null

    lateinit var inRentsAdapterIn: InRentAdapter
    lateinit var outRentsAdapterIn: OutRentAdapter

    override fun setCurrentSession(session: CognitoUserSession) {
        this.session = session
    }

    override fun returnCurrentSession(): CognitoUserSession {
        return session!!
    }

    override fun initPresenter() {
        if (presenter == null) {
            presenter = NotificationPresenter(context!!)
        }
        presenter?.attach(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initPresenter()
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_notification, container, false)
        return rootView
    }

    override fun showConfirmRentDialog(id: String) {

        val builder = AlertDialog.Builder(view!!.context)
        val confirmRentDialog = LayoutInflater.from(view!!.context).inflate(R.layout.dialog_confirm_rent, null)
        builder.setTitle("Richiesta noleggio")
        builder.setMessage("Vuoi confermare la richiesta ?")
        builder.setView(confirmRentDialog)
        val dialog: AlertDialog = builder.create()
        confirmRentDialog.bt_confirm_rent_dialog.setOnClickListener() {
            async {
                await {
                    RentRestCalls(session!!.accessToken.jwtToken).acceptRent(
                        id,
                        CognitoSettings(view!!.context).getUserPool().currentUser.userId
                    )
                }
            }
            dialog.dismiss()
            presenter?.refresh()
        }
        confirmRentDialog.bt_deny_rent_dialog.setOnClickListener() {
            async {
                await {
                    RentRestCalls(session!!.accessToken.jwtToken).denyRent(
                        id,
                        CognitoSettings(view!!.context).getUserPool().currentUser.userId
                    )
                }
            }
            dialog.dismiss()
            presenter?.refresh()
        }
        dialog.show()
    }

    override fun showConfirmKeyDialog(id: String) {

        val builder = AlertDialog.Builder(view!!.context)
        val confirmRentDialog = LayoutInflater.from(view!!.context).inflate(R.layout.dialog_confirm_rent, null)
        builder.setTitle("Richiesta chiavi")
        builder.setMessage("Vuoi confermare la ricezione delle chiavi ?")
        builder.setView(confirmRentDialog)
        val dialog: AlertDialog = builder.create()
        confirmRentDialog.bt_confirm_rent_dialog.setOnClickListener() {
            async {
                await {
                    RentRestCalls(session!!.accessToken.jwtToken).confirmKeys(
                        id,
                        CognitoSettings(view!!.context).getUserPool().currentUser.userId
                    )
                }
            }
            dialog.dismiss()
            presenter?.refresh()
        }
        confirmRentDialog.bt_deny_rent_dialog.setOnClickListener() {
            dialog.dismiss()
        }
        dialog.show()
    }

    override fun showConfirmEndDialog(id: String) {

        val builder = AlertDialog.Builder(view!!.context)
        val confirmRentDialog = LayoutInflater.from(view!!.context).inflate(R.layout.dialog_confirm_rent, null)
        builder.setTitle("Restituzione chiavi")
        builder.setMessage("Vuoi confermare la restituzione delle chiavi ?")
        builder.setView(confirmRentDialog)
        val dialog: AlertDialog = builder.create()
        confirmRentDialog.bt_confirm_rent_dialog.setOnClickListener() {
            async {
                await {
                    RentRestCalls(session!!.accessToken.jwtToken).closeRent(
                        id,
                        CognitoSettings(view!!.context).getUserPool().currentUser.userId
                    )
                }
            }
            dialog.dismiss()
            presenter?.refresh()
        }
        confirmRentDialog.bt_deny_rent_dialog.setOnClickListener() {
            dialog.dismiss()
        }
        dialog.show()
    }

    override fun showGenericDialog(title: String, message: String?) {
        val builder = AlertDialog.Builder(view!!.context)
        builder.setTitle(title)
        builder.setMessage(message)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    override fun setRentsInListAdapter(inList: ArrayList<Rent>) {
        inRentsAdapterIn = InRentAdapter(view!!.context, inList.reversed()) { rent ->

            if (rent.reason == "denied") {
                showGenericDialog("Richiesta rifiutata", "Hai rifiutato la richiesta di ${rent.user}")
            } else if (rent.isEnded) {
                showGenericDialog("Noleggio concluso", "${rent.user} ha riconsegnato le chiavi")
            } else if (!rent.isAccepted) { // richiesta ricevuta - ConfirmRentDialog (tasto conferma/annulla)
                showConfirmRentDialog(rent.id)
            } else if (rent.isAccepted && rent.hasKeys) {
                showConfirmEndDialog(rent.id)


            }
        }
        in_rents_recycler_view.adapter = inRentsAdapterIn
        in_rents_recycler_view.layoutManager = LinearLayoutManager(view!!.context)
        in_rents_recycler_view.setHasFixedSize(true)
    }

    override fun setRentsOutListAdapter(outList: ArrayList<Rent>) {
        outRentsAdapterIn = OutRentAdapter(view!!.context, outList.reversed()) { rent ->
            if (rent.isAccepted && !rent.hasKeys) {
                showConfirmKeyDialog(rent.id)
            } else if (rent.reason == "denied") {
                showGenericDialog("Richiesta rifiutata", "${rent.toUser} ha rifiutato la tua richiesta")
            } else if (rent.isEnded) {
                showGenericDialog(
                    "Noleggio concluso",
                    "${rent.toUser} ha confermato la ricezione delle chiavi\nHai guadagnato 20pe"
                )
            } else if (rent.isAccepted && rent.hasKeys) {
                showGenericDialog(
                    "In attesa della fine",
                    "Quando hai concluso il noleggio consegna a ${rent.toUser} le chiavi "
                )
            } else if (!rent.isAccepted) {
                showGenericDialog(
                    "Richiesta inoltrata",
                    "Hai inviato la richiesta a ${rent.toUser} in data ${rent.date} \nIn attesa di conferma.."
                )
            } else Toast.makeText(view!!.context, "Non ancora pronta la schermata", Toast.LENGTH_SHORT).show()
        }
        out_rents_recycler_view.adapter = outRentsAdapterIn
        out_rents_recycler_view.layoutManager = LinearLayoutManager(view!!.context)
        out_rents_recycler_view.setHasFixedSize(true)
    }

    override fun showLabelNoInRents() {
        label_no_in_rent_notifications.visibility = View.VISIBLE
    }

    override fun showLabelNoOutRents() {
        label_no_out_rent_notifications.visibility = View.VISIBLE
    }

    override fun showRentsInList() {
        presenter?.downloadRentsInList { list ->
            if(list.size > 0) {
                presenter?.setRentsInListAdapter(list)
            } else {
                hideInProgressBar()
                showLabelNoInRents()
            }
        }
    }

    override fun showRentsOutList() {
        presenter?.downloadRentsOutList { list ->
            if (list.size > 0) {
                presenter?.setRentsOutListAdapter(list)
            } else {
                hideOutProgressBar()
                showLabelNoOutRents()
            }
        }
    }

    override fun hideInProgressBar() {
        in_progress_bar.visibility = View.INVISIBLE
    }

    override fun hideOutProgressBar() {
        out_progress_bar.visibility = View.INVISIBLE
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showRentsInList()
        showRentsOutList()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter?.detach()
        presenter = null
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
