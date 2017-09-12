package com.example.hi.onetime;

/**
 * Created by Hi on 26-06-2017.
 */

import android.content.Context;

import com.amazonaws.mobileconnectors.lambdainvoker.LambdaFunction;

public interface MyInterface {

    /**
     * Invoke the Lambda function "AndroidBackendLambdaFunction".
     * The function name is the method name.
     */
    @LambdaFunction
    ResponseClass oneTimeLambdaFunction(RequestClass request);

}
