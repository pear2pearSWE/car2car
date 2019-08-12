package com.pear2pear.car2car.cognito

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import kotlinx.android.synthetic.main.activity_my_account.*
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserDetails
import com.pear2pear.car2car.R
import com.pear2pear.car2car.activities.ChooseAvatarActivity
import com.pear2pear.car2car.assets.User
import com.pear2pear.car2car.cognito.contract.MyAccountContract
import com.pear2pear.car2car.cognito.presenter.MyAccountPresenter
import kotlinx.android.synthetic.main.dialog_delete_account.view.*

class MyAccountActivity : AppCompatActivity(), MyAccountContract.View {

    var presenter = MyAccountPresenter(this)

    var token = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_account)

        if (intent.extras != null) {
            val bundle: Bundle = intent.extras
            token = bundle.getString("token")
        }
        presenter!!.setCurrentToken(token)

        presenter.getDetails()
        presenter.getLevel()
        bt_delete_account_my_account.setOnClickListener() {
            presenter.confirmDeleteUser()
        }
        bt_change_avatar_my_account.setOnClickListener {
            val chooseAvatar = Intent(this, ChooseAvatarActivity::class.java)
            chooseAvatar.putExtra("token",token)
            startActivity(chooseAvatar)
        }
    }

    override fun showDialog() {
        val builder = AlertDialog.Builder(this)
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.dialog_delete_account, null)
        builder.setTitle("Eliminazione account")
        builder.setMessage("Sicuro di voler eliminare il tuo account???")
        builder.setView(mDialogView)
        val dialog: AlertDialog = builder.create()
        mDialogView.bt_confirm_delete_account.setOnClickListener() {
            presenter.deleteUser()
            dialog.dismiss()
        }
        mDialogView.bt_cancel_delete_account.setOnClickListener() {
            mDialogView.destroyDrawingCache()
            dialog.dismiss()
        }
        dialog.show()
    }

    override fun showDetails(list: CognitoUserDetails) {
        text_username_my_account.text = CognitoSettings(this).getUserPool().currentUser.userId
        text_name_my_account.text = list.attributes.attributes["name"]
        text_surname_my_account.text = list.attributes.attributes["custom:custom:Surname"]
        text_telephone_my_account.text = list.attributes.attributes["phone_number"]
        text_email_my_account.text = list.attributes.attributes["email"]
        text_bithday_my_account.text = list.attributes.attributes["birthdate"]
        text_cf_my_account.text = list.attributes.attributes["custom:custom:CF"]
        text_licence_my_account.text = list.attributes.attributes["custom:licenseID"]
    }

    override fun showGameInfo(result: User) {
        text_level_my_account.text = "Lilvello ${result.getLevel()}"
        LevelprogressBar.progress = result.getCurrentExp()
        LevelPercentageView.text = "${result.getCurrentExp()} pe"
        if (result.avatar == "mouse") { icon_user_my_account.setImageResource(R.drawable.avatar_mouse) }
        else if (result.avatar == "cat") { icon_user_my_account.setImageResource(R.drawable.avatar_cat) }
        else if (result.avatar == "dog") { icon_user_my_account.setImageResource(R.drawable.avatar_dog) }
        else if (result.avatar == "bull") { icon_user_my_account.setImageResource(R.drawable.avatar_bull) }
        else if (result.avatar == "lion") { icon_user_my_account.setImageResource(R.drawable.avatar_lion) }
        else if (result.avatar == "giraffe") { icon_user_my_account.setImageResource(R.drawable.avatar_giraffe) }
    }

    override fun closeView() {
        finish()
    }

    override fun onRestart() {
        super.onRestart()
        val refresh = Intent(this, MyAccountActivity::class.java)
        refresh.putExtra("token",token)
        startActivity(refresh)
        this.finish()
    }
}
