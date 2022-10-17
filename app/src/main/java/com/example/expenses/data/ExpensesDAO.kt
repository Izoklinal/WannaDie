package com.example.expenses.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.expenses.data.models.Expenses
import com.example.expenses.data.models.TypesExpenses
import java.util.*

@Dao
interface ExpensesDAO {
    @Query("SELECT * FROM $TYPES_TABLE")
    fun getAllTypesExpenses(): LiveData<MutableList<TypesExpenses>>
    @Query("SELECT typesExpenses FROM $TYPES_TABLE WHERE uuid =:id")
    fun getTypeExpensesName(id: UUID): String
    @Update
    fun updateTypeExpenses (type: TypesExpenses)
    @Insert
    fun addTypeExpenses (type: TypesExpenses)
    @Delete
    fun deleteType (type: TypesExpenses)

    @Query("SELECT * FROM $EXPENSES_TABLE")
    fun getAllExpenses(): LiveData<MutableList<Expenses>>
    @Query("SELECT * FROM $EXPENSES_TABLE WHERE uuid=:id")
    fun getExpenses(id: UUID): Expenses
    @Insert
    fun addExpenses(expenses: Expenses)
    @Update
    fun updateExpenses(expenses: Expenses)
    @Delete
    fun deleteExpenses(expenses: Expenses)
}