package com.example.hambaapp
import junit.framework.TestCase.assertEquals
import org.junit.Test

class UserTest {

    @Test
    fun testUserProperties() {
        // Create a User instance
        val user = User(
            email = "test@example.com",
            fullName = "John Doe",
            phoneNumber = "1234567890"
        )

        // Asserting that the properties of the User instance match the expected values
        assertEquals("test@example.com", user.email)
        assertEquals("John Doe", user.fullName)
        assertEquals("1234567890", user.phoneNumber)
    }

    @Test
    fun testUserDefaultValues() {
        // Create a User instance with default values
        val user = User()

        // Assert that the properties have default values (null in this case)
        assertEquals(null, user.email)
        assertEquals(null, user.fullName)
        assertEquals(null, user.phoneNumber)
    }

    @Test
    fun testUserToString() {
        // Create a User instance
        val user = User(
            email = "test@example.com",
            fullName = "John Doe",
            phoneNumber = "1234567890"
        )

        // Assert that the toString() method generates the expected string representation
        assertEquals("User(email=test@example.com, fullName=John Doe, phoneNumber=1234567890)", user.toString())
    }
}
