package com.nitintraders.order.viewmodel

import android.app.Application
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nitintraders.order.utils.dataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val application: Application,
) : AndroidViewModel(application) {

    val readSelectedLanguage: Flow<String> =
        application.applicationContext.dataStore.data.map { preferences ->
            preferences[stringPreferencesKey("selectedLanguage")] ?: "en"
        }

    fun setAppLanguage(languageCode: String) {
        viewModelScope.launch {
            application.applicationContext.dataStore.edit { preferences ->
                preferences[stringPreferencesKey("selectedLanguage")] = languageCode
            }
        }
    }
}
