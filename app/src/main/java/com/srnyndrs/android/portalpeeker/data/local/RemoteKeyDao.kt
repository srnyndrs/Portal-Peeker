package com.srnyndrs.android.portalpeeker.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RemoteKeyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: RemoteKeyEntity)

    @Query("SELECT * FROM remote_key WHERE id = :id")
    suspend fun getByQueryId(id: String): RemoteKeyEntity?

    @Query("SELECT created_at FROM remote_key WHERE id = :id ORDER BY created_at DESC LIMIT 1")
    suspend fun getCreationTime(id: String): Long?

    @Query("DELETE FROM remote_key WHERE id = :id")
    suspend fun deleteById(id: String)
}