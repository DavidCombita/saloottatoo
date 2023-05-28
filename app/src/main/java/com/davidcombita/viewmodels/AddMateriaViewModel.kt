package com.davidcombita.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davidcombita.data.models.CategoriesUI
import com.davidcombita.data.models.Material
import com.davidcombita.data.models.MaterialsUI
import com.davidcombita.domain.GetMaterialUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class AddMateriaViewModel @Inject constructor(
    val getMaterialUseCase: GetMaterialUseCase
) : ViewModel() {

    private val _material = MutableStateFlow(CategoriesUI())
    val material: StateFlow<CategoriesUI> = _material.asStateFlow()

    private val _saveMateria = MutableStateFlow(false)
    val saveMateria: StateFlow<Boolean> = _saveMateria.asStateFlow()

    init {
        viewModelScope.launch{
            _material.update { it.copy(loading = true) }
            val category = getMaterialUseCase.getCategories()
            if(category.isSuccessful){
                _material.update { it.copy(materialInfo = category.body()!!, error = false) }
            }else{
                _material.update { it.copy(error = true) }
            }
            _material.update { it.copy(loading = false) }
        }
    }

    fun saveMaterial(material: Material){
        viewModelScope.launch{
            _material.update { it.copy(loading = true) }
            val category = getMaterialUseCase.saveMaterial(material)
            _saveMateria.value = category.isSuccessful
            _material.update { it.copy(loading = false) }
        }
    }
}