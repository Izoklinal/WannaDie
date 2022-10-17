package com.example.expenses.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.expenses.data.models.Expenses
import com.example.expenses.data.models.TypesExpenses

@Database(entities = [TypesExpenses::class, Expenses::class], version = 1)
abstract class ExpensesDatabase : RoomDatabase() {
    abstract fun expensesDAO(): ExpensesDAO
}