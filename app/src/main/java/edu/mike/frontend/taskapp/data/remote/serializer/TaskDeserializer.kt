package edu.mike.frontend.taskapp.data.remote.serializer

import com.google.gson.*
import edu.mike.frontend.taskapp.data.remote.dto.PriorityDto
import edu.mike.frontend.taskapp.data.remote.dto.StatusDto
import edu.mike.frontend.taskapp.data.remote.dto.TaskDto
import java.lang.reflect.Type
import java.util.*

/**
 * Custom JSON deserializer for [TaskDto] objects.
 *
 * Handles special cases like:
 * - Mixed id formats (string or numeric)
 * - Date parsing
 * - Properly deserializing nested objects
 */
class TaskDeserializer : JsonDeserializer<TaskDto> {

    /**
     * Deserializes JSON data into a [TaskDto] object.
     *
     * @param json The JSON element to deserialize
     * @param typeOfT The type of the object to deserialize
     * @param context The deserialization context
     * @return A fully populated [TaskDto] object
     * @throws JsonParseException if there's an error during parsing
     */
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): TaskDto {
        val jsonObject = json.asJsonObject

        // Extract the id, handling both integer and string formats
        val id = try {
            jsonObject.get("id").asLong
        } catch (e: NumberFormatException) {
            jsonObject.get("id").asString.toLong()
        }

        // Extract other fields from the JSON object
        val title = jsonObject.get("title").asString
        val notes = jsonObject.get("notes")?.asString ?: ""

        // Deserialize date fields with null safety
        val createDate = jsonObject.get("createDate")?.let {
            context.deserialize<Date>(it, Date::class.java)
        } ?: Date()

        val dueDate = jsonObject.get("dueDate")?.let {
            context.deserialize<Date>(it, Date::class.java)
        } ?: Date()

        // Deserialize objects with default values
        val priority = jsonObject.get("priority")?.let {
            context.deserialize<PriorityDto>(it, PriorityDto::class.java)
        } ?: PriorityDto(2, "Medium") // Default medium priority

        val status = jsonObject.get("status")?.let {
            context.deserialize<StatusDto>(it, StatusDto::class.java)
        } ?: StatusDto(1, "Todo") // Default todo status

        return TaskDto(id, title, notes, createDate, dueDate, priority, status)
    }
}