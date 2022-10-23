package com.example.expenses

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.room.Room
import com.example.expenses.data.DATABASE_NAME
import com.example.expenses.data.ExpensesDatabase
import com.example.expenses.data.models.Expenses
import com.example.expenses.data.models.TypesExpenses
import com.example.expenses.databinding.ActivityMainBinding
import java.util.*
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    private var pos: Int = -1
    private var expId: UUID? = null
    private var typeExpId: UUID? = null
    private var db: ExpensesDatabase? = null
    private var executor = Executors.newSingleThreadExecutor()
    private lateinit var binding: ActivityMainBinding
    private var expList: MutableList<Expenses> = mutableListOf()
    private var expTypeList: MutableList<TypesExpenses> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = Room.databaseBuilder(this, ExpensesDatabase::class.java, DATABASE_NAME).build()
        pos = intent.getIntExtra("pos", -1)
        val uuidText = intent.getStringExtra("uuid")
        val uuidTypeText = intent.getStringExtra("uuidType")
        converterUUID(uuidText, uuidTypeText)

        getExpenses()

        //selectEditText()
        binding.addButton.setOnClickListener{
            if (pos == -1) {
                binding.addButton.text = "Добавить"
                if (binding.editTextName.text.toString() != ""
                    && binding.editTextCost.text.toString() != ""
                    && binding.editTextDesc.text.toString() != "") {
                    addExpenses(binding.editTextName.text.toString(),
                        binding.editTextCost.text.toString().toInt(),
                        binding.editTextDesc.text.toString())
                    binding.editTextName.setText("")
                    binding.editTextCost.setText("")
                    binding.editTextDesc.setText("")
                }
                else {
                    Toast.makeText(this, "Нельзя добавить пустую строку", Toast.LENGTH_SHORT).show()
                }
            }
            else{
                if (binding.editTextName.text.toString() != ""
                    && binding.editTextCost.text.toString() != ""
                    && binding.editTextDesc.text.toString() != ""){
                    executor.execute {
                        db?.expensesDAO()?.updateTypeExpenses(
                            TypesExpenses(typeExpId!!, binding.editTextDesc.text.toString())
                        )
                        db?.expensesDAO()?.updateExpenses(
                            Expenses(expId!!,
                                binding.editTextName.text.toString(),
                                binding.editTextCost.text.toString().toInt(),
                                typeExpId!!)
                        )
                    }
                    Toast.makeText(this, "Значения изменены", Toast.LENGTH_SHORT).show()
                    super.onBackPressed()
                }
                else{
                    Toast.makeText(this, "Нельзя добавить пустую строку", Toast.LENGTH_SHORT).show()
                }
            }
        }
        binding.showButton.setOnClickListener{
            val intent = Intent(this, ShowActivity::class.java)
            startActivity(intent)
        }
        binding.delete.setOnClickListener{
            executor.execute {
                db?.expensesDAO()?.deleteExpenses(
                    Expenses(expId!!,
                        binding.editTextName.text.toString(),
                        binding.editTextCost.text.toString().toInt(),
                        typeExpId!!)
                )
                db?.expensesDAO()?.deleteType(
                    TypesExpenses(typeExpId!!, binding.editTextDesc.text.toString())
                )
            }
            binding.editTextName.setText("")
            binding.editTextCost.setText("")
            binding.editTextDesc.setText("")
            Toast.makeText(this, "Удалено", Toast.LENGTH_SHORT).show()
            binding.delete.isEnabled = false
            super.onBackPressed()
        }
    }

    private fun converterUUID(uuidExpensesText: String?, uuidTypeText: String?){
        if(uuidExpensesText != null && uuidTypeText != null){
            expId = UUID.fromString(uuidExpensesText)
            typeExpId = UUID.fromString(uuidTypeText)
        }
    }

    /*private fun selectEditText(){
        if(pos > -1){
            binding.addButton.text = "Изменить"
            binding.delete.visibility = View.VISIBLE
            binding.delete.isEnabled = true
            executor.execute {
                val expenses = db?.expensesDAO()?.getExpenses(expId!!)
                val typeName = db?.expensesDAO()?.getTypeExpensesName(typeExpId!!)

                runOnUiThread(Runnable {
                    kotlin.run {
                        binding.editTextName.setText(expenses?.nameExpenses)
                        binding.editTextCost.setText(expenses?.costExpenses.toString())
                        binding.editTextDesc.setText(typeName)
                    }
                })
            }
        }
        else {
            binding.delete.visibility = View.INVISIBLE
            binding.addButton.text = "Добавить"
        }


    }*/

    private fun getExpenses() {
        expList.clear()
        expTypeList.clear()
        db?.expensesDAO()?.getAllTypesExpenses()?.observe(this, androidx.lifecycle.Observer {
            runOnUiThread(Runnable{
                kotlin.run {
                    expTypeList.addAll(it)
                }
            })

        })
        db?.expensesDAO()?.getAllExpenses()?.observe(this, androidx.lifecycle.Observer {
            runOnUiThread(Runnable{
                kotlin.run {
                    expList.addAll(it)
                }
            })
        })
    }

    private fun addExpenses(name: String, cost: Int, nameType:String)
    {
        var expTypeContains: Boolean = true
        val uuidType = UUID.randomUUID()

        expTypeList.forEach{
            if (it.typesExpenses == binding.editTextDesc.text.toString()){
                expTypeContains = false
            }
        }
        if (expTypeContains) {
            executor.execute {
                db?.expensesDAO()?.addTypeExpenses(
                    TypesExpenses(uuidType, nameType)
                )
                db?.expensesDAO()?.addExpenses(
                    Expenses(UUID.randomUUID(), name, cost, uuidType)
                )
            }
        } else if (!expTypeContains) {
            Executors.newSingleThreadExecutor().execute {
                val uuid = db?.expensesDAO()?.getTypeExpensesName(nameType)
                val exp = uuid?.let { Expenses(uuidType, name, cost, it) }
                if (exp != null) {
                    db?.expensesDAO()?.addExpenses(exp)
                }
            }
        }
        else
        {
            Toast.makeText(this, "Уже имеется", Toast.LENGTH_SHORT).show()
        }
    }
}