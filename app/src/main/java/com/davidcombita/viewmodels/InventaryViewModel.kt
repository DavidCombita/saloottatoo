package com.davidcombita.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davidcombita.data.models.MaterialsUI
import com.davidcombita.domain.GetMaterialUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class InventaryViewModel @Inject constructor(
    val getMaterialUseCase: GetMaterialUseCase
) : ViewModel() {
    private val _material = MutableStateFlow(MaterialsUI())
    val material: StateFlow<MaterialsUI> = _material.asStateFlow()

    fun getInventary(){
        viewModelScope.launch{
            _material.update { it.copy(loading = true) }
            val material = getMaterialUseCase.invoke()
            if(material.isSuccessful){
                _material.update { it.copy(materialInfo = material.body()!!, error = false) }
            }else{
                _material.update { it.copy(error = true) }
            }
            _material.update { it.copy(loading = false) }
        }
    }
}