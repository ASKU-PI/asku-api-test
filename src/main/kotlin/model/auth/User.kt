package model.auth

import kotlinx.serialization.Required
import kotlinx.serialization.Serializable

@Serializable
data class User(
    @Required val identifier: String,
    @Required val authorities: Array<Authority>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        if (identifier != other.identifier) return false
        if (!authorities.contentEquals(other.authorities)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = identifier.hashCode()
        result = 31 * result + authorities.contentHashCode()
        return result
    }
}

@Serializable
data class Authority(@Required val authorityName: String)