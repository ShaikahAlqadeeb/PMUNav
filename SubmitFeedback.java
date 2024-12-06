import java.time.LocalDateTime
import java.util.UUID

// Feedback data class
data class Feedback(
    val id: String,
    val userId: String,
    val rating: Int,
    val comment: String? = null,
    val isAnonymous: Boolean = false,
    val createdAt: LocalDateTime = LocalDateTime.now()
)

class FeedbackService {
    // In-memory storage for feedbacks
    private val feedbacks = mutableListOf<Feedback>()
    // Prevent duplicates
    private val submittedInteractions = mutableSetOf<String>()

    fun submitFeedback(
        userId: String,
        interactionId: String,
        rating: Int,
        comment: String? = null,
        isAnonymous: Boolean = false
    ): Feedback {
        // Validate rating
        require(rating in 1..5) { "Rating must be between 1 and 5" }

        // Check if feedback for this interaction already exists
        if (submittedInteractions.contains(interactionId)) {
            throw IllegalStateException("Feedback for this interaction is already submitted")
        }

        // Create feedback
        val feedback = Feedback(
            id = UUID.randomUUID().toString(),
            userId = userId,
            rating = rating,
            comment = comment,
            isAnonymous = isAnonymous
        )

        // Store feedback and mark interaction
        feedbacks.add(feedback)
        submittedInteractions.add(interactionId)

        return feedback
    }

    // Get all feedbacks (for demonstration)
    fun getAllFeedbacks(): List<Feedback> = feedbacks.toList()

    // Get average rating
    fun getAverageRating(): Double {
        return if (feedbacks.isEmpty()) 0.0 
        else feedbacks.map { it.rating }.average()
    }
}

// Example usage
fun main() {
    val feedbackService = FeedbackService()

    try {
        // Submit feedback
        val feedback = feedbackService.submitFeedback(
            userId = "user123",
            interactionId = "interaction456",
            rating = 4,
            comment = "Great experience!",
            isAnonymous = false
        )
        println("Feedback submitted: $feedback")

        // Get average rating
        println("Average Rating: ${feedbackService.getAverageRating()}")

    } catch (e: Exception) {
        println("Feedback submission failed: ${e.message}")
    }
}
