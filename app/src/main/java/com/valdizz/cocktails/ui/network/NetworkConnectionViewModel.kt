package com.valdizz.cocktails.ui.network

import android.content.Context
import androidx.lifecycle.ViewModel

/**
 * ViewModel class implements internet connection check.
 *
 * @author Vlad Kornev
 */
class NetworkConnectionViewModel(context: Context) : ViewModel() {

    private val networkConnection = NetworkConnectionLiveData(context)

    fun getConnection(): NetworkConnectionLiveData {
        return networkConnection
    }
}