package com.example.expenses

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.expenses.data.DATABASE_NAME
import com.example.expenses.data.ExpensesDatabase
import com.example.expenses.data.ExpensesRVAdapter
import com.example.expenses.data.models.Expenses
import com.example.expenses.data.models.TypesExpenses
import com.example.expenses.databinding.ActivityShowBinding

class ShowActivity : AppCompatActivity() {

    private var expList: MutableList<Expenses> = mutableListOf()
    private var typesList: MutableList<TypesExpenses> = mutableListOf()
    private var db: ExpensesDatabase? = null
    private lateinit var b: ActivityShowBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityShowBinding.inflate(layoutInflater)
        setContentView(b.root)
        db = Room.databaseBuilder(this, ExpensesDatabase::class.java, DATABASE_NAME).build()
    }

    private fun getExpenses() {
        expList.clear()
        typesList.clear()
        db?.expensesDAO()?.getAllTypesExpenses()?.observe(this, androidx.lifecycle.Observer {
            typesList.addAll(it)
        })
        db?.expensesDAO()?.getAllExpenses()?.observe(this, androidx.lifecycle.Observer {
            expList.addAll(it)
            getRecycleView()
        })
    }

    override fun onResume() {
        super.onResume()
        getExpenses()
    }

    private fun getRecycleView(){
        val rvListener = object : ExpensesRVAdapter.ItemClickListener {
            override fun onItemClick(view: View?, position: Int) {
                val intent = Intent(this@ShowActivity, MainActivity::class.java)
                intent.putExtra("pos", position)
                intent.putExtra("uuid", expList[position].uuid.toString())
                intent.putExtra("uuidType", typesList[position].uuid.toString())
                startActivity(intent)
            }
        }
        val adapter = ExpensesRVAdapter(this, expList, typesList)
        adapter.setClickListener(rvListener)
        b.rv.adapter = adapter
    }
}