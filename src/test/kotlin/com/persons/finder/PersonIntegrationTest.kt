package com.persons.finder


import com.fasterxml.jackson.databind.ObjectMapper
import com.jayway.jsonpath.JsonPath
import org.awaitility.Awaitility.await
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.get
import org.springframework.web.context.WebApplicationContext
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping
import java.util.concurrent.TimeUnit
import kotlin.jvm.java

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
class PersonIntegrationTest @Autowired constructor(
    val mockMvc: MockMvc,
    val objectMapper: ObjectMapper
) {

    @Autowired
    private lateinit var context: WebApplicationContext

    @BeforeEach
    fun printEndpoints() {
        val handlerMapping = context.getBean(RequestMappingHandlerMapping::class.java)
        handlerMapping.handlerMethods.forEach { (key, value) ->
            println("Mapped: $key to $value")
        }
    }

    @Test
    fun `should create person and then find them nearby`() {
        val personRequest = mapOf(
            "name" to "Alice",
            "jobTitle" to "Developer",
            "hobbies" to listOf("Hiking", "Reading"),
            "latitude" to 40.7128,
            "longitude" to -74.0060
        )

        // 1. Test Create
        mockMvc.post("/api/v1/persons") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(personRequest)
        }.andExpect {
            status { isCreated() }
        }

        // 2. Test Search (NYC is roughly 40, -74)
        val searchRequest = mapOf(
            "latitude" to 40.70,
            "longitude" to -74.01,
            "radius" to 10.0
        )

        mockMvc.get("/api/v1/persons/nearby") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(searchRequest)
        }.andExpect {
            status { isOk() }
            jsonPath("$[0].person.name") { value("Alice") }
        }
    }


    @Test
    fun `should create person and then be able to map another person with the same hobbies`() {
        val personRequest = mutableMapOf(
            "name" to "Alice",
            "jobTitle" to "Developer",
            "hobbies" to listOf("Hiking", "Reading"),
            "latitude" to 40.7128,
            "longitude" to -74.0060
        )

        // 1. Test Create
        mockMvc.post("/api/v1/persons") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(personRequest)
        }.andExpect {
            status { isCreated() }
        }

        personRequest["name"] =  "fred"

        mockMvc.post("/api/v1/persons") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(personRequest)
        }.andExpect {
            status { isCreated() }
        }

        val searchRequest = mapOf(
            "latitude" to 40.70,
            "longitude" to -74.01,
            "radius" to 10.0
        )


        mockMvc.get("/api/v1/persons/nearby") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(searchRequest)
        }.andExpect {
            status { isOk() }
            jsonPath("$[0].person.name") { value("Alice") }
            jsonPath("$[1].person.name") { value("fred")}
        }
    }

    @Test
    fun `should return 400 when latitude is invalid`() {
        val badRequest = mapOf(
            "name" to "Bob",
            "jobTitle" to "Tester",
            "hobbies" to listOf("Gaming"),
            "latitude" to 150.0, // Invalid!
            "longitude" to 0.0
        )

        mockMvc.post("/api/v1/persons") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(badRequest)
        }.andExpect {
            status { isBadRequest() }
        }
    }

    @Test
    fun `should eventually update person with a generated bio`() {

        val hobbies = listOf("Hiking", "Reading", "random unknown hobby")
        val personRequest = mapOf(
            "name" to "Alice",
            "jobTitle" to "Developer",
            "hobbies" to listOf("Hiking", "Reading", "random unknown hobby"),
            "latitude" to 40.7128,
            "longitude" to -74.0060
        )

        // 1. Test Create
        mockMvc.post("/api/v1/persons") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(personRequest)
        }.andExpect {
            status { isCreated() }
        }

        // 3. Assert (The Awaitility way)
        await().atMost(120, TimeUnit.SECONDS).untilAsserted {
            val searchRequest = mapOf(
                "latitude" to 40.70,
                "longitude" to -74.01,
                "radius" to 10.0
            )

            val result = mockMvc.get("/api/v1/persons/nearby") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(searchRequest)
            }.andReturn()

            val name = JsonPath.read<String>(result.response.contentAsString, "$[0].person.name")
            val bio = JsonPath.read<String>(result.response.contentAsString, "$[0].person.extremelyFunkyBio")
            assert(name == "Alice")
            hobbies.forEach {
                hobby -> assert(
                    bio.contains(hobby)
                )
            }
            print(result.response.contentAsString)
        }
    }
}