package com.davidcombita.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davidcombita.data.models.MaterialsUI
import com.davidcombita.data.models.TattosMaterialUI
import com.davidcombita.domain.GetMaterialUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class ReservaViewModel @Inject constructor(
    val getMaterialUseCase: GetMaterialUseCase
) : ViewModel() {

    private val _material = MutableStateFlow(TattosMaterialUI())
    val material: StateFlow<TattosMaterialUI> = _material.asStateFlow()

    fun getMaterialsTattos(tatto: Int){
        viewModelScope.launch{
            _material.update { it.copy(loading = true) }
            val material = getMaterialUseCase.getMaterialTattoByTatto(tatto)
            if(material.isSuccessful){
                _material.update { it.copy(tattoMaterialInfo = material.body()!!, error = false) }
            }else{
                _material.update { it.copy(error = true) }
            }
            _material.update { it.copy(loading = false) }
        }
    }

    fun sendReserva(toNumber: String, fecha: String,
                    style: String, size: String){
        viewModelScope.launch{
            getMaterialUseCase.sendReserva(toNumber, fecha, style, size)
        }
    }
}