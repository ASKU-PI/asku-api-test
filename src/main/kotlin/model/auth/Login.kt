package model.auth

import kotlinx.serialization.Required
import kotlinx.serialization.Serializable

@Serializable
data class Login(
    @Required val username: String,
    @Required val password: String
)