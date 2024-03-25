package com.srnyndrs.android.portalpeeker.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CharacterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplace(characters: List<CharacterEntity>)

    @Query("SELECT * FROM characterentity WHERE LOWER(name) LIKE '%' || LOWER(:name) || '%'")
    fun pagingSource(name: String): PagingSource<Int, CharacterEntity>

    @Query("SELECT * FROM characterentity WHERE LOWER(name) LIKE '%' || LOWER(:name) || '%'")
    fun getByQueryName(name: String): List<CharacterEntity>

    @Query("DELETE FROM characterentity WHERE LOWER(name) LIKE '%' || LOWER(:name) || '%'")
    suspend fun deleteByNameQuery(name: String)
}