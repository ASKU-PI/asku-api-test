package model.auth

import kotlinx.serialization.Required
import kotlinx.serialization.Serializable

@Serializable
data class User(
    @Required val username: String,
    @Required val authorities: Array<Authority>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        if (username != other.username) return false
        if (!authorities.contentEquals(other.authorities)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = username.hashCode()
        result = 31 * result + authorities.contentHashCode()
        return result
    }
}

@Serializable
data class Authority(@Required val authorityName: String)