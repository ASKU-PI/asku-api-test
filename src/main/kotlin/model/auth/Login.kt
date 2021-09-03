package model.auth

import kotlinx.serialization.Required
import kotlinx.serialization.Serializable

@Serializable
data class Login(
    @Required val email: String,
    @Required val password: String
)