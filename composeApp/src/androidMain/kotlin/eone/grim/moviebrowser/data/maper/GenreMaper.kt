package eone.grim.moviebrowser.data.maper

import eone.grim.moviebrowser.data.remote.dto.GenreDto
import eone.grim.moviebrowser.domain.entity.Genre

fun GenreDto.toDomain(): Genre = Genre(
    id = id,
    name = name
)
