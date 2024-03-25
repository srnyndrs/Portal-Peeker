package com.srnyndrs.android.portalpeeker.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [CharacterEntity::class, RemoteKeyEntity::class],
    version = 1
)
abstract class CharacterDatabase: RoomDatabase() {
    abstract val characterDao: CharacterDao
    abstract val remoteKeyDao: RemoteKeyDao
}