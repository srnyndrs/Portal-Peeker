package com.srnyndrs.android.portalpeeker.data.remote

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.srnyndrs.android.portalpeeker.data.local.CharacterDatabase
import com.srnyndrs.android.portalpeeker.data.local.CharacterEntity
import com.srnyndrs.android.portalpeeker.data.local.RemoteKeyEntity
import com.srnyndrs.android.portalpeeker.data.mapper.toCharacterEntity
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalPagingApi::class)
class CharacterRemoteMediator(
    private val database: CharacterDatabase,
    private val api: CharacterApi,
    private var query: String
): RemoteMediator<Int, CharacterEntity>() {

    override suspend fun initialize(): InitializeAction {
        val cacheTimeout = TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS)
        return if(System.currentTimeMillis() - (database.remoteKeyDao.getCreationTime(query) ?: 0) < cacheTimeout) {
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, CharacterEntity>
    ): MediatorResult {
        val remoteKey = database.withTransaction {
            database.remoteKeyDao.getByQueryId(query)
        }
        val loadKey = when (loadType) {
            LoadType.REFRESH -> {
                remoteKey?.nextOffset?.minus(1) ?: 1
            }
            LoadType.PREPEND -> {
                remoteKey?.previousOffset ?: return MediatorResult.Success(endOfPaginationReached = remoteKey != null)
            }
            LoadType.APPEND -> {
                remoteKey?.nextOffset ?: return MediatorResult.Success(endOfPaginationReached = remoteKey != null)
            }
        }
        val localData = database.withTransaction { database.characterDao.getByQueryName(query) }
        try {
            val response = api.getCharacters(loadKey, query)
            val info = response.info
            val characters = response.results

            database.withTransaction {
                if(loadType == LoadType.REFRESH) {
                    database.remoteKeyDao.deleteById(query)
                    if(query != "") {
                        database.characterDao.deleteByNameQuery(query)
                    }
                }

                database.remoteKeyDao.insert(
                    RemoteKeyEntity(
                        id = query,
                        nextOffset = if(info.pages > loadKey) loadKey.plus(1) else null,
                        previousOffset = if(loadKey > 1) loadKey.minus(1) else null
                    )
                )

                database.characterDao.insertOrReplace(characters.map { it.toCharacterEntity() })
            }
            return MediatorResult.Success(endOfPaginationReached = response.info.next == null)
        } catch (e: Exception) {
            if(localData.isNotEmpty()) {
                return MediatorResult.Success(endOfPaginationReached = true)
            }
            return MediatorResult.Error(e)
        }
    }
}