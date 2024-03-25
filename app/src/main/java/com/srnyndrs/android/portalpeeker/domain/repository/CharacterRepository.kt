package com.srnyndrs.android.portalpeeker.domain.repository

import androidx.paging.PagingData
import com.srnyndrs.android.portalpeeker.domain.model.Character
import kotlinx.coroutines.flow.Flow

interface CharacterRepository {
    fun getCharacters(name: String): Flow<PagingData<Character>>
}