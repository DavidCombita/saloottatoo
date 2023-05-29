package com.davidcombita.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davidcombita.data.models.MaterialsUI
import com.davidcombita.data.models.TattosUI
import com.davidcombita.domain.GetTattosUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    val getTattoUseCase: GetTattosUseCase) : ViewModel() {

    private val _tatto = MutableStateFlow(TattosUI())
    val tatto: StateFlow<TattosUI> = _tatto.asStateFlow()

    fun getTattos(){
        viewModelScope.launch{
            _tatto.update { it.copy(loading = true) }
            try{
                val material = getTattoUseCase.invoke()
                if(material.isSuccessful){
                    _tatto.update { it.copy(tattoInfo = material.body()!!, error = false) }
                }else{
                    _tatto.update { it.copy(error = true) }
                }
                _tatto.update { it.copy(loading = false) }
            }catch (e: Exception){
                Log.e("Error----", e.message.toString())
                _tatto.update { it.copy(error = true) }
                _tatto.update { it.copy(loading = false) }
            }
        }
    }

}