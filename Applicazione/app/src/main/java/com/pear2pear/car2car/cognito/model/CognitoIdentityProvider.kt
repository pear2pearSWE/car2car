package com.pear2pear.car2car.cognito.model

import android.content.Context
import android.os.AsyncTask
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GenericHandler
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GetDetailsHandler
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.SignUpHandler
import com.pear2pear.car2car.assets.User
import com.pear2pear.car2car.cognito.CognitoSettings

class CognitoIdentityProvider(var context: Context) {

    /* ConfirmCodeModel */
    fun confirmUser(handler: GenericHandler, user:String,code:String){
        println("conferma model")
        CognitoSettings(context).getUserPool().getUser(user).confirmSignUpInBackground(code,false,handler)
    }

    /* LoginModel */
    fun confirmLogin(authenticationHandler: AuthenticationHandler){
        val user= CognitoSettings(context).getUserPool().currentUser
        user.getSessionInBackground(authenticationHandler)
    }

    /* MyAccountModel */
    fun getDetails(handler: GetDetailsHandler){
        CognitoSettings(context).getUserPool().currentUser.getDetailsInBackground(handler)
    }

    fun deleteUser(handler: GenericHandler){
        CognitoSettings(context).getUserPool().currentUser.deleteUserInBackground(handler)
    }

    fun getLevel(task: AsyncTask<String, Int, User>){
        task.execute(CognitoSettings(context).getUserPool().currentUser.userId)
    }

    /* SignUpModel */
    fun signUp(user:String, psw:String, handler: SignUpHandler, attributes: CognitoUserAttributes){
        CognitoSettings(context).getUserPool().signUpInBackground(user,psw,attributes,null,handler)
    }

    /* VerifySessionModel */
    fun verify(authenticationHandler: AuthenticationHandler, user: CognitoUser){
        user.getSessionInBackground(authenticationHandler)
    }

}