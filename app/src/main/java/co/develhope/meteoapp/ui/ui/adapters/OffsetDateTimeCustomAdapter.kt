package co.develhope.meteoapp.ui.ui.adapters

import android.util.Log
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.ZoneOffset
import org.threeten.bp.format.DateTimeFormatter
import java.lang.reflect.Type

class OffsetDateTimeCustomAdapter : JsonSerializer<OffsetDateTime>, JsonDeserializer<OffsetDateTime> {
    override fun serialize(
        src: OffsetDateTime?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        return JsonPrimitive(dateTimeFormatter.format(src)) }

    @Throws(JsonParseException::class)
    override fun deserialize(
        json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?
    ): OffsetDateTime {
        val asString = json?.asString
        if (asString != null) { Log.d("Adapter", asString) }

        return if (asString?.contains("T")  ==  true) {
            val date = LocalDateTime.parse(asString).atZone(ZoneOffset.UTC)
            Log.d("Adapter contain Time", "$date")
            date.toOffsetDateTime()
        } else {
            val date = LocalDate.parse(asString).atStartOfDay(ZoneOffset.UTC)
            Log.d("Adapter contain Time", "$date")
            date.toOffsetDateTime()
        }
    }

    companion object {
        private val dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
    }
}