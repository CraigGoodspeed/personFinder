package com.persons.finder.dto

import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

data class PersonRequest (
    @field:NotBlank(message = "Name is required")
    val name: String,

    @field:NotBlank(message = "Job title is required")
    val jobTitle: String,

    @field:Size(min = 1, message = "Please provide at least one hobby")
    val hobbies: List<String>,

    override val latitude: Double,
    override val longitude: Double
    ) : LocationRequest(latitude, longitude)
     {

}