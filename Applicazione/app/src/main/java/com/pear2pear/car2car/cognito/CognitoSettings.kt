package com.pear2pear.car2car.cognito

import android.content.Context
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool
import com.amazonaws.regions.Regions

class CognitoSettings(val context: Context) {
    val userPoolId:String="us-east-1_BwaIFOt0m"
    val clientId:String="4i747a0beip81otfr4s9k363q1"
    val clientSecret:String="fbbdnq3vo3dv7eingihs4le4thukbahgbhs9u1l7r07pvl5jdl1"
    val cognitoRegion=Regions.US_EAST_1

    fun getUserPool():CognitoUserPool{
        return CognitoUserPool(context,userPoolId,clientId,clientSecret,cognitoRegion)
    }
}