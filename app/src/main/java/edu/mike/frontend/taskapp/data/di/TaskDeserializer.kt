package edu.mike.frontend.taskapp.data.di

        import com.google.gson.*
        import edu.mike.frontend.taskapp.data.model.PriorityDto
        import edu.mike.frontend.taskapp.data.model.StatusDto
        import edu.mike.frontend.taskapp.data.model.TaskDto
        import java.lang.reflect.Type
        import java.util.*

        class TaskDeserializer : JsonDeserializer<TaskDto> {

            override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): TaskDto {
                val jsonObject = json.asJsonObject

                // Extract the id, handling both integer and string formats
                val id = try {
                    jsonObject.get("id").asLong  // Try to get it as a long
                } catch (e: NumberFormatException) {
                    jsonObject.get("id").asString.toLong()  // Fallback: Parse the string to long
                }

                // Extract other fields from the JSON object
                val title = jsonObject.get("title").asString
                val notes = jsonObject.get("notes").asString
                val createDate = context.deserialize<Date>(jsonObject.get("createDate"), Date::class.java)
                val dueDate = context.deserialize<Date>(jsonObject.get("dueDate"), Date::class.java)
                val priority = context.deserialize<PriorityDto>(jsonObject.get("priority"), PriorityDto::class.java)
                val status = context.deserialize<StatusDto>(jsonObject.get("status"), StatusDto::class.java)

                // Return the deserialized TaskDto object
                return TaskDto(id, title, notes, createDate, dueDate, priority, status)
            }
        }