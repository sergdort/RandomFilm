package com.randofilm.sergdort.platform.FilmDetails

import com.google.gson.annotations.SerializedName
import com.randofilm.sergdort.domain.FilmDetails.Crew
import com.randofilm.sergdort.platform.Conversion.DomainConvertible

internal data class Crew(
        @SerializedName("credit_id")
        val creditId: String,
        val department: String,
        val id: Int,
        val job: String,
        val name: String,
        @SerializedName("profile_path")
        val profilePicturePath: String?) : DomainConvertible<Crew> {

    override fun asDomain(): Crew = Crew(creditId, department, id, job, name, profilePicturePath)
}
