package com.persons.finder.data

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class Hobby(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(unique = true, nullable = false, length = 200)
    val name: String = ""
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Hobby) return false
        return name == other.name
    }
    override fun hashCode(): Int = name.hashCode()

    override fun toString(): String {
        return "Hobby(id=$id, name='$name')"
    }
}