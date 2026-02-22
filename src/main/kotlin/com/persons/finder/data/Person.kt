package com.persons.finder.data

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.JoinColumn
import javax.persistence.JoinTable
import javax.persistence.ManyToMany

@Entity
data class Person(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val name: String = "",
    @ManyToMany(cascade = [CascadeType.MERGE, CascadeType.REFRESH])
    @JoinTable(
        name = "person_hobbies",
        joinColumns = [JoinColumn(name = "person_id")],
        inverseJoinColumns = [JoinColumn(name = "hobby_id")]
    )
    var hobbies: MutableSet<Hobby> = mutableSetOf(),
    val jobTitle: String = "",
    @Column(length = 2000)
    var extremelyFunkyBio: String = "",

    ) {
    override fun toString(): String {
        return "Person(id=$id, name='$name', jobTitle='$jobTitle')"
    }
}
