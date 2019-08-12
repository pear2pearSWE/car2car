package com.pear2pear.car2car.cognito

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes
import com.pear2pear.car2car.R
import com.pear2pear.car2car.cognito.contract.SignUpContract
import com.pear2pear.car2car.cognito.presenter.SignUpPresenter
import kotlinx.android.synthetic.main.activity_sing_up.*
import java.util.*

class SignUpActivity : AppCompatActivity(), SignUpContract.View {

    var presenter: SignUpContract.Presenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sing_up)
        initPresenter()
        bt_date_sign_up.setOnClickListener {
            showCalendar()
        }
        bt_sign_up.setOnClickListener {
            presenter?.signUp()
        }
    }

    override fun initPresenter() {
        if (presenter == null) {
            presenter = SignUpPresenter(this)
        }
        presenter?.attach(this)
    }

    override fun showConfirmCode(user: String) {
        val intent = Intent(this@SignUpActivity, ConfirmCodeActivity::class.java)
        this@SignUpActivity.finish()
        intent.putExtra("Username",user)
        startActivity(intent)
    }

    override fun showCalendar() {
        val cal = Calendar.getInstance()
        val y = cal.get(Calendar.YEAR)
        val m = cal.get(Calendar.MONTH)
        val d = cal.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this,
            AlertDialog.THEME_DEVICE_DEFAULT_DARK,
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                var day = false
                if (dayOfMonth < 10) {
                    day = true
                }

                if (monthOfYear + 1 < 10) {
                    if (day) {
                        label_birthday_sign_up.text = "0$dayOfMonth/0${monthOfYear + 1}/$year"
                    } else {
                        label_birthday_sign_up.text = "$dayOfMonth/0${monthOfYear + 1}/$year"
                    }
                } else {
                    if (day) {
                        label_birthday_sign_up.text = "0$dayOfMonth/${monthOfYear + 1}/$year"
                    } else {
                        label_birthday_sign_up.text = "$dayOfMonth/${monthOfYear + 1}/$year"
                    }
                }
            },
            y,
            m,
            d
        )
        datePickerDialog.show()
    }

    override fun showError() {
        Toast.makeText(this, "Errore, ricontrolla i campi inseriri e riprova", Toast.LENGTH_SHORT).show()
    }

    override fun getInsertedUser(): String {
        return edit_username_sign_up.text.toString()
    }

    override fun getInsertedPassword(): String {
        return edit_password_sign_up.text.toString()
    }

    override fun getAttribute(): CognitoUserAttributes {
        val userAttributes = CognitoUserAttributes()
        userAttributes.addAttribute("email", edit_mail_sign_up.text.toString())
        userAttributes.addAttribute("phone_number", edit_telephone_sign_up.text.toString())
        userAttributes.addAttribute("birthdate", label_birthday_sign_up.text.toString())
        userAttributes.addAttribute("name", edit_name_sign_up.text.toString())
        userAttributes.addAttribute("custom:custom:Surname", edit_surname_sign_up.text.toString())
        userAttributes.addAttribute("custom:custom:CF", edit_cf_sign_up.text.toString())
        userAttributes.addAttribute("custom:licenseID", edit_license_sign_up.text.toString())
        return userAttributes
    }

    override fun closeView() {
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter?.detach()
        presenter = null
    }
}
