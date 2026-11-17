package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface ActivationCodeDao {

    @Query("SELECT * FROM activation_codes ORDER BY isUsed ASC, price ASC, id ASC")
    fun getAllCodes(): Flow<List<ActivationCodeEntity>>

    @Query("SELECT COUNT(*) FROM activation_codes")
    fun getTotalCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM activation_codes WHERE isUsed = 0")
    fun getAvailableCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM activation_codes WHERE isUsed = 1")
    fun getUsedCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM activation_codes")
    suspend fun getCountSync(): Int

    @Query("SELECT code FROM activation_codes WHERE price = :price")
    suspend fun getCodesByPriceSync(price: Int): List<String>

    @Query("SELECT * FROM activation_codes WHERE code = :code LIMIT 1")
    suspend fun findByCode(code: String): ActivationCodeEntity?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCodes(codes: List<ActivationCodeEntity>)

    @Query(
        """
        UPDATE activation_codes 
        SET isUsed = 1, usedAt = :usedAt, usedByName = :usedByName, usedByPhone = :usedByPhone 
        WHERE id = :id AND isUsed = 0
        """
    )
    suspend fun markCodeUsed(
        id: Long,
        usedAt: Long,
        usedByName: String,
        usedByPhone: String
    ): Int

    @Query(
        """
        UPDATE activation_codes 
        SET isUsed = 0, usedAt = NULL, usedByName = '', usedByPhone = '' 
        WHERE id = :id
        """
    )
    suspend fun resetCode(id: Long)

    @Query("DELETE FROM activation_codes WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Transaction
    suspend fun redeemCodeAtomic(
        code: String,
        usedByName: String,
        usedByPhone: String
    ): ActivationCodeEntity? {
        val existing = findByCode(code) ?: return null
        if (existing.isUsed) return null
        val now = System.currentTimeMillis()
        val updatedRows = markCodeUsed(
            id = existing.id,
            usedAt = now,
            usedByName = usedByName,
            usedByPhone = usedByPhone
        )
        if (updatedRows == 0) return null
        return existing.copy(
            isUsed = true,
            usedAt = now,
            usedByName = usedByName,
            usedByPhone = usedByPhone
        )
    }
}
