package com.randofilm.sergdort.platform.FilmDetails

import com.randofilm.sergdort.domain.FilmDetails.Credits
import com.randofilm.sergdort.platform.Conversion.DomainConvertible

internal data class Credits(
        val id: Int,
        val cast: List<Cast>,
        val crew: List<Crew>) : DomainConvertible<Credits> {

    override fun asDomain(): Credits = Credits(id, cast.map { it.asDomain() }, crew.map { it.asDomain() })
}

