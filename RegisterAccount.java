import java.util.UUID

// User data class
data class User(
    val id: String,
    val email: String,
    val password: String,
    val firstName: String,
    val lastName: String
)

class UserRegistrationService {
    // In-memory user storage
    private val users = mutableListOf<User>()

    fun registerUser(
        email: String, 
        password: String, 
        firstName: String, 
        lastName: String
    ): User {
        // Input validation
        validateRegistration(email, password, firstName, lastName)

        // Check for existing email
        if (users.any { it.email == email }) {
            throw IllegalArgumentException("Email already registered")
        }

        // Create new user
        val newUser = User(
            id = UUID.randomUUID().toString(),
            email = email,
            password = hashPassword(password),
            firstName = firstName,
            lastName = lastName
        )

        // Save user
        users.add(newUser)

        return newUser
    }

    private fun validateRegistration(
        email: String, 
        password: String, 
        firstName: String, 
        lastName: String
    ) {
        require(email.isNotBlank()) { "Email cannot be blank" }
        require(email.contains("@")) { "Invalid email format, please enter valid email" }
        require(password.length >= 8) { "Password must be at least 8 characters" }
        require(firstName.isNotBlank()) { "First name cannot be blank" }
        require(lastName.isNotBlank()) { "Last name cannot be blank" }
    }

    // Method to get all registered users (for demonstration)
    fun getAllUsers(): List<User> = users.toList()
}

// Example usage
fun main() {
    val registrationService = UserRegistrationService()

    try {
        val user = registrationService.registerUser(
            email = "john.doe@example.com",
            password = "SecurePassword123",
            firstName = "John",
            lastName = "Doe"
        )
        println("User registered successfully: $user")
    } catch (e: Exception) {
        println("Registration failed: ${e.message}")
    }
}
