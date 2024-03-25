package com.srnyndrs.android.portalpeeker.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.srnyndrs.android.portalpeeker.data.local.CharacterDatabase
import com.srnyndrs.android.portalpeeker.data.mapper.toCharacter
import com.srnyndrs.android.portalpeeker.data.remote.CharacterApi
import com.srnyndrs.android.portalpeeker.data.remote.CharacterRemoteMediator
import com.srnyndrs.android.portalpeeker.domain.model.Character
import com.srnyndrs.android.portalpeeker.domain.repository.CharacterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class CharacterRepositoryImpl @Inject constructor(
    private val database: CharacterDatabase,
    private val api: CharacterApi
): CharacterRepository {

    override fun getCharacters(name: String): Flow<PagingData<Character>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20
            ),
            remoteMediator = CharacterRemoteMediator(
                database = database,
                api = api,
                query = name
            ),
            pagingSourceFactory = {
                database.characterDao.pagingSource(name)
            }
        ).flow.map { pagingData ->
            pagingData.map { it.toCharacter() }
        }
    }
}