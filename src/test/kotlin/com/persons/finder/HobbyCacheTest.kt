package com.persons.finder

import com.persons.finder.data.Hobby
import com.persons.finder.domain.services.HobbyService
import com.persons.finder.repository.HobbyRepository
import org.junit.jupiter.api.Test
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean

@SpringBootTest
class HobbyCacheTest {

    @Autowired
    private lateinit var hobbyService: HobbyService

    @MockBean
    private lateinit var hobbyRepository: HobbyRepository

    @Test
    fun `should only call repository once when fetching same hobby multiple times`() {
        val hobbyName = "Golf"
        val mockHobby = Hobby(name = hobbyName)

        // Arrange: Tell the mock what to return
        whenever(hobbyRepository.findByName(hobbyName)).thenReturn(mockHobby)

        // Act: Call the service twice
        hobbyService.getOrCreateHobby(hobbyName)
        hobbyService.getOrCreateHobby(hobbyName)

        // Assert: Verify the repository was called exactly ONCE
        verify(hobbyRepository, times(1)).findByName(hobbyName)
    }
}