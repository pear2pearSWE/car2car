package com.pear2pear.car2car.cognito.presenter

import android.annotation.SuppressLint
import android.content.Context
import co.metalab.asyncawait.async
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationDetails
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChallengeContinuation
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler
import com.pear2pear.car2car.activities.HomeActivity
import com.pear2pear.car2car.cognito.contract.LoginContract
import com.pear2pear.car2car.cognito.contract.VerifySessionContract
import com.pear2pear.car2car.cognito.model.CognitoIdentityProvider
import com.pear2pear.car2car.restcalls.GameRestCalls
import java.lang.Exception

class LoginPresenter(context: Context):LoginContract.Presenter {

    var activity : LoginContract.View = context as HomeActivity

    var view: VerifySessionContract.View = context as HomeActivity

    var model = CognitoIdentityProvider(context)

    var user = ""
    var psw = ""

    val authenticationHandle = object : AuthenticationHandler { //Gestore di callback per il processo di autenticazione dell'utente.
        @SuppressLint("ResourceAsColor")
        override fun onFailure(exception: Exception?) {
            exception?.printStackTrace()
            activity.showError()

        }

        override fun onSuccess(userSession: CognitoUserSession?, newDevice: CognitoDevice?) {
            activity.hideDialog(userSession)
            async{
                val User=
                    await{ GameRestCalls(userSession!!.accessToken.jwtToken).getUserGame(userSession.username)}
                if(!User.tutorial){
                    view?.showWelcomeTutorialDialog()

                }
            }
        }

        override fun getAuthenticationDetails( //Chiama il dev per ottenere le credenziali per un utente.
            authenticationContinuation: AuthenticationContinuation,
            userId: String?
        ) {

            val authenticationDetails = AuthenticationDetails(user,psw,null)
            authenticationContinuation.setAuthenticationDetails(authenticationDetails)
            authenticationContinuation.continueTask()
        }

        override fun authenticationChallenge(continuation: ChallengeContinuation?) { //Challenge generica
            continuation!!.continueTask()
        }

        override fun getMFACode(continuation: MultiFactorAuthenticationContinuation?) { //MultifactorAuthentication
            continuation!!.continueTask()
        }
    }

    override fun verifyLogin(user:String, psw:String){
        this.user = user
        this.psw = psw
        model.confirmLogin(authenticationHandle)
    }
}