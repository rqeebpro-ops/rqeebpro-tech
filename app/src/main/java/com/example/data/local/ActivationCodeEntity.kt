package com.example.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "activation_codes",
    indices = [Index(value = ["code"], unique = true)]
)
data class ActivationCodeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val code: String,
    val price: Int,
    val isUsed: Boolean = false,
    val usedAt: Long? = null,
    val usedByName: String = "",
    val usedByPhone: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
