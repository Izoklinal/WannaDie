package com.example.expenses.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.expenses.data.TYPES_TABLE
import java.util.*

@Entity(tableName = TYPES_TABLE)
data class TypesExpenses (
    @PrimaryKey(autoGenerate = false)
    var uuid: UUID,
    var typesExpenses: String
)