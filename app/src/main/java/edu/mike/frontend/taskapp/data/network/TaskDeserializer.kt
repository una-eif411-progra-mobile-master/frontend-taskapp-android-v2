package edu.mike.frontend.taskapp.data.network

import com.google.gson.*
import edu.mike.frontend.taskapp.data.model.Priority
import edu.mike.frontend.taskapp.data.model.Status
import edu.mike.frontend.taskapp.data.model.Task
import java.lang.reflect.Type
import java.util.*

/**
 * Custom deserializer for the Task class to handle JSON deserialization.
 */
class TaskDeserializer : JsonDeserializer<Task> {

    /**
     * Deserializes a JSON element into a Task object.
     *
     * @param json The JSON element to deserialize.
     * @param typeOfT The type of the object to deserialize to.
     * @param context The context for deserialization.
     * @return The deserialized Task object.
     * @throws JsonParseException If the JSON is not in the expected format.
     */
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Task {
        val jsonObject = json.asJsonObject

        // Extract the id, handling both integer and string formats
        val id = try {
            jsonObject.get("id").asInt  // Try to get it as an integer
        } catch (e: NumberFormatException) {
            jsonObject.get("id").asString.toInt()  // Fallback: Parse the string to integer
        }

        // Extract other fields from the JSON object
        val title = jsonObject.get("title").asString
        val notes = jsonObject.get("notes").asString
        val createDate = context.deserialize<Date>(jsonObject.get("createDate"), Date::class.java)
        val dueDate = context.deserialize<Date>(jsonObject.get("dueDate"), Date::class.java)
        val priority = context.deserialize<Priority>(jsonObject.get("priority"), Priority::class.java)
        val status = context.deserialize<Status>(jsonObject.get("status"), Status::class.java)

        // Return the deserialized Task object
        return Task(id, title, notes, createDate, dueDate, priority, status)
    }
}