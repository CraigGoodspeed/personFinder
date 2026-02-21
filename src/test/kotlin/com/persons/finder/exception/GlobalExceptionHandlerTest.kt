package com.persons.finder.exception

import com.persons.finder.facade.PersonManagementService
import com.persons.finder.presentation.PersonController
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
// Highlighting the critical imports here:
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath

@WebMvcTest(PersonController::class)
class GlobalExceptionHandlerTest {


    @MockBean lateinit var personManagementService: PersonManagementService

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `should return 400 when json is invalid`() {
        val invalidJson = """
            {
                "name": "", 
                "latitude": 500.0 
            }
        """.trimIndent()

        // Act & Assert
        mockMvc.perform(post("/api/v1/persons")
            .contentType(MediaType.APPLICATION_JSON) // Use JSON here
            .content(invalidJson))
            .andDo(print())
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.error").value("Malformed JSON request") )

    }

    @Test
    fun `should return 400 when json is valid but fails validation`() {
        val invalidJson = """
            {
                "name": "", 
                "jobTitle": "DEV",
                "latitude": 890.0,
                "longitude": 100.0,
                 "hobbies":["dance"]
            }
        """.trimIndent()

        // Act & Assert
        mockMvc.perform(post("/api/v1/persons")
            .contentType(MediaType.APPLICATION_JSON) // Use JSON here
            .content(invalidJson))
            .andDo(print())
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.latitude").value("Latitude must be between -90 and 90"))
            .andExpect(jsonPath("$.name").value("Name is required") )
    }



}