package com.pear2pear.car2car.cognito.presenter

import android.annotation.SuppressLint
import android.content.Context
import android.os.AsyncTask
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserDetails
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GenericHandler
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GetDetailsHandler
import com.pear2pear.car2car.assets.User
import com.pear2pear.car2car.cognito.MyAccountActivity
import com.pear2pear.car2car.cognito.contract.MyAccountContract
import com.pear2pear.car2car.cognito.model.CognitoIdentityProvider
import com.pear2pear.car2car.restcalls.GameRestCalls

class MyAccountPresenter(context: Context) : MyAccountContract.Presenter {

    var activty : MyAccountContract.View = context as MyAccountActivity

    var model = CognitoIdentityProvider(context)

    var token:String=""

    override fun setCurrentToken(token: String) {
        this.token = token
    }

    val handler = object : GetDetailsHandler {
        override fun onSuccess(list: CognitoUserDetails) {
            activty.showDetails(list) //carica la lista di dati dell'utente
        }

        override fun onFailure(exception: Exception) {
        }
    }

    val deleteHandler = object : GenericHandler {
        override fun onSuccess() {
            activty.closeView()
        }

        override fun onFailure(exception: java.lang.Exception?) {
        }
    }

    //Prelevo punti esperienza da User
    val gametask = @SuppressLint("StaticFieldLeak")
    object : AsyncTask<String, Int, User>() {
        override fun doInBackground(vararg p0: String): User? {
            var u: User? = null
            try {
                u = GameRestCalls().getUserGame(p0[0])
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return u
        }

        override fun onProgressUpdate(vararg values: Int?) {
            super.onProgressUpdate(*values)
        }

        override fun onPostExecute(result: User?) {
            super.onPostExecute(result)
            if (result != null) {
                activty.showGameInfo(result)
            } else {
                println("")
            }
        }
    }

    override fun getDetails() {
        model.getDetails(handler)
    }

    override fun confirmDeleteUser() {
        activty.showDialog()
    }

    override fun deleteUser() {
        model.deleteUser(deleteHandler)
    }

    override fun getLevel() {
        model.getLevel(gametask)
    }
}