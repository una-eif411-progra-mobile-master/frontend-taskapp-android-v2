package edu.mike.frontend.taskapp.data.remote.serializer

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import edu.mike.frontend.taskapp.data.remote.dto.AuthResponseDto
import java.lang.reflect.Type

/**
 * Custom deserializer for AuthResponseDto.
 *
 * This class handles custom deserialization logic for authentication responses,
 * particularly useful if the API response format doesn't directly match your DTO.
 */
class AuthResponseDeserializer : JsonDeserializer<AuthResponseDto> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): AuthResponseDto {
        val jsonObject = json.asJsonObject

        return AuthResponseDto(
            token = jsonObject.get("token")?.asString ?: "",
            userId = jsonObject.get("userId")?.asString ?: ""
        )
    }
}