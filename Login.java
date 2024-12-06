import java.time.LocalDateTime

class UserLoginService(private val users: List<User>) {
    // Track login attempts
    private val loginAttempts = mutableMapOf<String, Int>()

    fun login(email: String, password: String): LoginResult {
        // Find user by email
        val user = users.find { it.email == email }
            ?: return LoginResult.INVALID_CREDENTIALS

        // Check login attempts
        val attempts = loginAttempts.getOrDefault(email, 0)
        if (attempts >= 5) {
            return LoginResult.ACCOUNT_LOCKED
        }

        // Verify password
        if (!verifyPassword(user, password)) {
            // Increment failed attempts
            loginAttempts[email] = attempts + 1
            return LoginResult.INVALID_CREDENTIALS
        }

        // Reset login attempts on successful login
        loginAttempts[email] = 0

        // Create login session
        return LoginResult.SUCCESS(
            userId = user.id,
            email = user.email,
            loginTime = LocalDateTime.now()
        )
    }

    // Simple password verification 
    private fun verifyPassword(user: User, inputPassword: String): Boolean {
        return user.password == inputPassword.hashCode().toString()
    }

    // Sealed class for login results
    sealed class LoginResult {
        object INVALID_CREDENTIALS : LoginResult()
        object ACCOUNT_LOCKED : LoginResult()
        data class SUCCESS(
            val userId: String,
            val email: String,
            val loginTime: LocalDateTime
        ) : LoginResult()
    }
}

// Example usage
fun main() {
    // Create some users
    val users = listOf(
        User(
            id = "1",
            email = "john.doe@example.com",
            password = "SecurePassword123".hashCode().toString(),
            firstName = "John",
            lastName = "Doe"
        )
    )

    val loginService = UserLoginService(users)

    // Attempt to login
    val result = loginService.login("john.doe@example.com", "SecurePassword123")

    when (result) {
        is UserLoginService.LoginResult.SUCCESS -> 
            println("Login successful for user: ${result.email}")
        UserLoginService.LoginResult.INVALID_CREDENTIALS -> 
            println("Invalid email or password")
        UserLoginService.LoginResult.ACCOUNT_LOCKED -> 
            println("Account is locked due to multiple failed attempts")
    }
}
