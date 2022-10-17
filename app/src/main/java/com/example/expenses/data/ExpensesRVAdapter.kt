package com.example.expenses.data

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.expenses.R
import com.example.expenses.data.models.Expenses
import com.example.expenses.data.models.TypesExpenses

class ExpensesRVAdapter(context: Context?,
                        private val data:MutableList<Expenses>,
                        private val dataTypes:MutableList<TypesExpenses>)
    : RecyclerView.Adapter<ExpensesRVAdapter.ExpenseViewHolder>() {

    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val view: View = layoutInflater.inflate(R.layout.item_expense, parent, false)
        return ExpenseViewHolder(view)
    }

    private var iClickListener: ItemClickListener? = null

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        val item = data[position]
        val itemType = dataTypes[position]
        holder.name.text = item.nameExpenses
        holder.cost.text = item.costExpenses.toString()
        holder.type.text = itemType.typesExpenses
    }

    override fun getItemCount(): Int = data.size

    inner class ExpenseViewHolder(item: View) : RecyclerView.ViewHolder(item), View.OnClickListener{
        var name: TextView = item.findViewById(R.id.tv_title)
        var cost: TextView = item.findViewById(R.id.tv_cost)
        var type: TextView = item.findViewById(R.id.tv_description)
        init {
            itemView.setOnClickListener(this)
        }
        override fun onClick(view: View?){
            iClickListener?.onItemClick(view, adapterPosition)
        }
    }

    fun setClickListener(itemClickListener: ItemClickListener?){
        iClickListener = itemClickListener
    }
    interface ItemClickListener{
        fun onItemClick(view: View?, position: Int)
    }
}