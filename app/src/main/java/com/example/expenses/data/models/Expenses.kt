package com.example.expenses.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.expenses.data.EXPENSES_TABLE
import org.jetbrains.annotations.NotNull
import java.util.*

@Entity(tableName = EXPENSES_TABLE)
data class Expenses (
    @PrimaryKey(autoGenerate = false)
    @NotNull
    var uuid: UUID,
    var nameExpenses: String,
    var costExpenses: Int,

    @ColumnInfo(index = true)
    var typeExpenses: UUID
    )