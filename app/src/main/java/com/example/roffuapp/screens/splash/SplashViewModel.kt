package com.example.roffuapp.screens.splash

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.roffuapp.repositories.UserRepository
import com.example.roffuapp.sealed.UiState
import com.example.roffuapp.utils.APP_LAUNCHED
import com.example.roffuapp.utils.LOGGED_USER_ID
import com.example.roffuapp.utils.UserPref
import com.example.roffuapp.utils.dataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
@SuppressLint("StaticFieldLeak")
class SplashViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val userRepository: UserRepository,
) : ViewModel() {
    val uiState = mutableStateOf<UiState>(UiState.Idle)

    /** Is the app launched before or not? */
    val isAppLaunchedBefore = context.dataStore.data.map {
        it[APP_LAUNCHED] ?: false
    }

    /** Logged user's id as flow  */
    val loggedUserId = context.dataStore.data.map {
        it[LOGGED_USER_ID]
    }

    fun checkLoggedUser(userId: Int, onCheckFinish: () -> Unit) {
        viewModelScope.launch {
            userRepository.getLoggedUser(userId = userId)?.let {
                Timber.d("Logged user exist !")
                UserPref.updateUser(user = it)
            }
            onCheckFinish()
        }
    }
}