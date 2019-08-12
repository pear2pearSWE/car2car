package com.pear2pear.car2car.cognito

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.pear2pear.car2car.R
import com.pear2pear.car2car.cognito.contract.ConfirmCodeContract
import com.pear2pear.car2car.cognito.presenter.ConfirmCodePresenter
import kotlinx.android.synthetic.main.activity_confirm_code.*

class ConfirmCodeActivity : AppCompatActivity(), ConfirmCodeContract.View {

    var presenter : ConfirmCodeContract.Presenter?=null

    var user:String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm_code)
        initPresenter()
        if (intent.extras != null) {
            val bundle: Bundle = intent.extras
            bt_confirm_code.setOnClickListener {
                presenter?.confirm(bundle.getString("Username"), edit_verify_code.text.toString())
            }

        }


    }

    override fun initPresenter() {
        if (presenter == null) {
            presenter = ConfirmCodePresenter(this)
        }
        presenter?.attach(this)
    }

    override fun showError() {
        Toast.makeText(this, "Codice errato, riprova.", Toast.LENGTH_SHORT).show()
    }

    override fun showSuccess() {
        Toast.makeText(this, "Codice confermato corettamente", Toast.LENGTH_SHORT).show()
        this.finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter?.detach()
        presenter = null
    }
}
