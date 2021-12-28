package com.example.stepstobeersv1

import android.app.Activity.RESULT_OK
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.FitnessOptions
import com.google.android.gms.fitness.data.DataType
import com.google.android.gms.fitness.data.Field

class getStepCount :Fragment(){
    private val fitnessOptions: FitnessOptions by lazy {
        FitnessOptions.builder()
            .addDataType(DataType.TYPE_STEP_COUNT_CUMULATIVE)
            .addDataType(DataType.TYPE_STEP_COUNT_DELTA)
            .build()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        fitSignIn()
    }

    /*
     * Checks whether the user is signed in. If so, executes the specified
     * function. If the user is not signed in, initiates the sign-in flow,
     * specifying the function to execute after the user signs in.
     */
    private fun fitSignIn() {
        if (oAuthPermissionsApproved()) {
            readDailySteps()
        } else {
            GoogleSignIn.requestPermissions(
                this,
                SIGN_IN_REQUEST_CODE,
                getGoogleAccount(),
                fitnessOptions
            )
        }
    }

    private fun oAuthPermissionsApproved() =
        GoogleSignIn.hasPermissions(getGoogleAccount(), fitnessOptions)

    /*
     * Gets a Google account for use in creating the fitness client. This is
     * achieved by either using the last signed-in account, or if necessary,
     * prompting the user to sign in. It's better to use the
     * getAccountForExtension() method instead of the getLastSignedInAccount()
     * method because the latter can return null if there has been no sign in
     * before.
     */
    private fun getGoogleAccount(): GoogleSignInAccount =
        GoogleSignIn.getAccountForExtension(requireContext(), fitnessOptions)

    /*
     * Handles the callback from the OAuth sign in flow, executing the function
     * after sign-in is complete.
     */
    override fun onActivityResult(
        requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            RESULT_OK -> {
                readDailySteps()
            }
            else -> {
                // Handle error.
            }
        }
    }

    /*
     * Reads the current daily step total.
     */
    private fun readDailySteps() {
        Fitness.getHistoryClient(requireContext(), getGoogleAccount())
            .readDailyTotal(DataType.TYPE_STEP_COUNT_DELTA)
            .addOnSuccessListener { dataSet ->
                val total = when {
                    dataSet.isEmpty -> 0
                    else -> dataSet.dataPoints.first()
                        .getValue(Field.FIELD_STEPS).asInt()
                }

                Log.i(TAG, "Total steps: $total")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "There was a problem getting the step count.", e)
            }
    }

    companion object {
        const val SIGN_IN_REQUEST_CODE = 1001
    }

}