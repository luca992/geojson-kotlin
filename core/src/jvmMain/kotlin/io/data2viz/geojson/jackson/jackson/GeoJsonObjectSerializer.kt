package io.data2viz.geojson.jackson.jackson

import io.data2viz.geojson.jackson.*
import kotlinx.serialization.json.*

// ---- CRS type ----

enum class CrsType {
    NAME, LINK;

    companion object {
        @JvmStatic
        fun forValue(value: String): CrsType = valueOf(value.uppercase())
    }

    fun toValue(): String = name.lowercase()
}

// ---- Convenience functions for crs/bbox-aware serialization ----

fun GeoJsonObject.toJsonString(): String {
    val element = geoJson.encodeToJsonElement<GeoJsonObject>(this)
    if (crs == null && bbox == null) return element.toString()
    val original = element.jsonObject
    val ordered = linkedMapOf<String, JsonElement>()
    // "type" first
    original["type"]?.let { ordered["type"] = it }
    // crs right after type (matches Jackson field order)
    if (crs != null) {
        ordered["crs"] = buildJsonObject {
            crs!!.type?.let { put("type", it.toValue()) }
            crs!!.properties?.let { put("properties", it.toJsonElement()) }
        }
    }
    // remaining fields in original order
    for ((k, v) in original) {
        if (k != "type") ordered[k] = v
    }
    if (bbox != null) {
        ordered["bbox"] = JsonArray(bbox!!.map { JsonPrimitive(it) })
    }
    return JsonObject(ordered).toString()
}

fun <T : GeoJsonObject> String.decodeGeoJson(): T {
    val jsonObj = geoJson.parseToJsonElement(this).jsonObject
    @Suppress("UNCHECKED_CAST")
    val result = geoJson.decodeFromJsonElement<GeoJsonObject>(JsonObject(jsonObj)) as T
    decodeCrs(jsonObj, result)
    return result
}

// ---- crs/bbox helpers ----

private fun decodeCrs(obj: JsonObject, target: GeoJsonObject) {
    obj["crs"]?.let { crsEl ->
        if (crsEl !is JsonNull) {
            val crsObj = crsEl.jsonObject
            val crs = Crs()
            crsObj["type"]?.jsonPrimitive?.content?.let { crs.type = CrsType.forValue(it) }
            crsObj["properties"]?.let {
                if (it !is JsonNull) {
                    @Suppress("UNCHECKED_CAST")
                    crs.properties = it.toAnyValue() as? Map<String, Any>
                }
            }
            target.crs = crs
        }
    }
    obj["bbox"]?.let { bboxEl ->
        if (bboxEl !is JsonNull) {
            target.bbox = bboxEl.jsonArray.map { it.jsonPrimitive.double }.toDoubleArray()
        }
    }
}

// ---- Value conversions ----

fun JsonElement.toAnyValue(): Any? = when (this) {
    is JsonNull -> null
    is JsonPrimitive -> when {
        isString -> content
        else -> booleanOrNull ?: intOrNull ?: longOrNull ?: doubleOrNull ?: content
    }
    is JsonArray -> map { it.toAnyValue() }
    is JsonObject -> entries.associate { (k, v) -> k to v.toAnyValue() }
}

internal fun Any?.toJsonElement(): JsonElement = when (this) {
    null -> JsonNull
    is String -> JsonPrimitive(this)
    is Number -> JsonPrimitive(this)
    is Boolean -> JsonPrimitive(this)
    is Map<*, *> -> buildJsonObject {
        forEach { (k, v) -> put(k.toString(), v.toJsonElement()) }
    }
    is List<*> -> buildJsonArray { forEach { add(it.toJsonElement()) } }
    else -> JsonPrimitive(toString())
}
