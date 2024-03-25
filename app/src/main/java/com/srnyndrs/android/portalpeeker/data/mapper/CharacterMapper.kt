package com.srnyndrs.android.portalpeeker.data.mapper

import com.srnyndrs.android.portalpeeker.data.local.CharacterEntity
import com.srnyndrs.android.portalpeeker.data.remote.CharacterDto
import com.srnyndrs.android.portalpeeker.domain.model.Character

fun CharacterDto.toCharacterEntity(): CharacterEntity {
    return CharacterEntity(
        id = id,
        name = name,
        status = status,
        imageUrl = image,
        location = location.name
    )
}

fun CharacterEntity.toCharacter(): Character {
    return Character(
        id = id,
        name = name,
        status = status,
        imageUrl = imageUrl,
        location = location
    )
}