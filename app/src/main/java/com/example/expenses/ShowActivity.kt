package com.example.expenses

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
    private var expTypeList: MutableList<TypesExpenses> = mutableListOf()
    private var indexChanged = -1
    private var db: ExpensesDatabase? = null
    private lateinit var b: ActivityShowBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityShowBinding.inflate(layoutInflater)
        setContentView(b.root)
        db = Room.databaseBuilder(this, ExpensesDatabase::class.java, DATABASE_NAME).build()
    }

    private fun getExpenses() { // work king
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
                    Log.d("working", "$expList\n$expTypeList")
                    getRecycleView()
                    //updateRecycle()
                }
            })
        })
    }

    /*private fun updateRecycle(){
        val adapter = ExpensesRVAdapter(this, expList, expTypeList)
        val rvListener = object : ExpensesRVAdapter.ItemClickListener{
            override fun onItemClick(view: View?, position: Int) {
                val intent = Intent(this@ShowActivity, MainActivity::class.java)
                intent.putExtra("num", position)
                indexChanged = position
                startActivity(intent)
                Toast.makeText(this@ShowActivity, "Вы выбрали $position вкладку", Toast.LENGTH_SHORT).show()
            }
        }
        adapter.setClickListener(rvListener)
        b.rv.adapter = adapter
        b.rv.layoutManager = LinearLayoutManager(this)
    }*/

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
                intent.putExtra("uuidType", expTypeList[position].uuid.toString())
                startActivity(intent)
            }
        }
        val adapter = ExpensesRVAdapter(this, expList, expTypeList)
        adapter.setClickListener(rvListener)
        b.rv.adapter = adapter
    }
}