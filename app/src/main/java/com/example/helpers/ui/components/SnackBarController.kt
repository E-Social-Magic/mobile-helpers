package com.example.helpers.ui.components

import androidx.compose.material.SnackbarHostState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SnackBarController(private val scope: CoroutineScope){

    private var snackBarJob: Job?=null

    init {
        cancelActiveSnack()
    }
    fun getScope() = scope

    fun showSnackbar(  snackbarHostState: SnackbarHostState, message: String, actionLabel: String){

        if (snackBarJob == null){
            snackBarJob = scope.launch {
                snackbarHostState.showSnackbar(message = message,actionLabel = actionLabel)
                cancelActiveSnack()
            }
        }
        else{
            cancelActiveSnack()
            snackBarJob = scope.launch {
               snackbarHostState.showSnackbar(message = message,actionLabel =actionLabel)
                cancelActiveSnack()
            }

        }
    }
    private fun cancelActiveSnack(){
        snackBarJob?.let{ job ->
            job.cancel()
            snackBarJob = Job()
        }
    }
}