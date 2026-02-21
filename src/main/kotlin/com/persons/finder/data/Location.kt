package com.persons.finder.data

import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.MapsId
import javax.persistence.OneToOne

@Entity
data class Location(

    @Id
    val referenceId: Long? = null,

    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    @OneToOne
    @JoinColumn(name = "referenceId", referencedColumnName = "id")
    @MapsId
    val person: Person? = null

) {
    override fun toString(): String {
        return "Location(referenceId=$referenceId, latitude=$latitude, longitude=$longitude)"
    }
}
