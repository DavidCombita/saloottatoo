package com.davidcombita.views.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.davidcombita.R
import com.davidcombita.data.models.Categories
import com.davidcombita.data.models.Material
import com.davidcombita.data.models.TattoResponse
import com.davidcombita.utils.CustomAdapter
import com.davidcombita.viewmodels.AddMateriaViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.Serializable
import java.lang.Exception
import javax.inject.Inject

@AndroidEntryPoint
class AddMaterialActivity : AppCompatActivity() {

    private var idMaterialAux: Long = 0

    @Inject
    lateinit var viewModel: AddMateriaViewModel

    lateinit var editName: EditText
    lateinit var editDesc: EditText
    lateinit var category: Spinner
    lateinit var editQuantity: EditText
    lateinit var editUnits: EditText
    lateinit var editPrice: EditText

    var isUpdate: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_material)

        val materialUpdate = intent.serializable<Material>("updateMaterial")

        val progress = findViewById<ProgressBar>(R.id.progressBar)
        val btnAdd = findViewById<Button>(R.id.button_add_material)
        val btnBack = findViewById<ImageButton>(R.id.imageButton_back)
        editName = findViewById(R.id.editText)
        editDesc = findViewById(R.id.editText_description)
        editPrice = findViewById(R.id.editText_price)
        editQuantity = findViewById(R.id.editText_cuantity)
        category = findViewById(R.id.spinner_opciones)
        editUnits = findViewById(R.id.editText_unitis)

        if(materialUpdate!= null){
            btnAdd.text ="Actualizar producto"
            editName.setText(materialUpdate.nameProduct)
            editDesc.setText(materialUpdate.nameBrand)
            editPrice.setText(materialUpdate.unitValue.toString())
            editQuantity.setText(materialUpdate.quantity.toString())
            category.setSelection(materialUpdate.idCategory.toInt()-1)
            editUnits.setText(materialUpdate.units.toString())
            isUpdate = true
            idMaterialAux = materialUpdate.idMaterial
        }

        btnBack.setOnClickListener { onBackPressed() }

        btnAdd.setOnClickListener {
            if(isUpdate){
                val newMaterial = Material(category.selectedItemPosition.toLong()+1,
                    idMaterialAux,
                    editName.text.toString(),
                    editDesc.text.toString(),
                    editQuantity.text.toString().toLong(),
                    editUnits.text.toString().toLong(),
                    editPrice.text.toString().toLong(), 0)
                viewModel.updateMaterial(newMaterial)
            }else{
                val newMaterial = Material(category.selectedItemPosition.toLong()+1, 0,
                    editName.text.toString(),
                    editDesc.text.toString(),
                    editQuantity.text.toString().toLong(),
                    editUnits.text.toString().toLong(),
                    editPrice.text.toString().toLong(), 0)
                viewModel.saveMaterial(newMaterial)
            }
        }

        lifecycleScope.launch{
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.material.collect{ info ->
                    if(info.error){
                        Toast.makeText(this@AddMaterialActivity,
                            "Error al traer la información", Toast.LENGTH_LONG).show()
                    }else{
                        if(info.materialInfo.isNotEmpty()){
                            val adapter = CustomAdapter(this@AddMaterialActivity, info.materialInfo)
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            progress.visibility = if (info.loading) View.VISIBLE else View.GONE
                            category.adapter = adapter
                        }
                    }
                }
            }
        }

        lifecycleScope.launch{
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.saveMateria.collect{ info ->
                    try{
                        if(info){
                            Toast.makeText(this@AddMaterialActivity,
                                "Guardado correctamente", Toast.LENGTH_LONG).show()
                            onBackPressed()
                        }
                    }catch (e: Exception){
                        Toast.makeText(this@AddMaterialActivity,
                            "Error, verifique los campos escritos", Toast.LENGTH_LONG).show()

                    }
                }
            }
        }
    }

    inline fun <reified T : Serializable> Bundle.serializable(key: String) = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getSerializable(key, T::class.java)
        else -> @Suppress("DEPRECATION") getSerializable(key) as? T
    }

    inline fun <reified T : Serializable> Intent.serializable(key: String): T? = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getSerializableExtra(key, T::class.java)
        else -> @Suppress("DEPRECATION") getSerializableExtra(key) as? T
    }
}