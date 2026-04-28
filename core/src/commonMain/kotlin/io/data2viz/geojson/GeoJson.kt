package io.data2viz.geojson

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.*
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass


/**
 * Marker interface to indicate a GeoJson object. It can be
 * a Geometry, a Feature or a FeatureCollection
 */
@Serializable
sealed interface GeoJsonObject {
    var crs: Crs?
    var bbox: DoubleArray?
}

/**
 * A feature contains a Geometry, an optional id, and optional properties.
 */
@Serializable
@SerialName("Feature")
data class Feature(
    @SerialName("properties") val propertiesObject: JsonObject? = null,
    val geometry: Geometry? = null,
    @SerialName("id") val idPrimitive: JsonPrimitive? = null
) : GeoJsonObject {
    @Transient
    override var crs: Crs? = null

    @Transient
    override var bbox: DoubleArray? = null
    val id: Any? get() = idPrimitive?.toAnyValue()
    val properties: Any? get() = propertiesObject?.toAnyValue()
}

/**
 * A feature collection is an array of features.
 */
@Serializable
@SerialName("FeatureCollection")
data class FeatureCollection(val features: List<Feature> = emptyList()) : GeoJsonObject {
    @Transient
    override var crs: Crs? = null

    @Transient
    override var bbox: DoubleArray? = null

    constructor(features: Array<Feature>) : this(features.toList())
}


interface Geometry : GeoJsonObject


@Serializable
@SerialName("Point")
data class Point(
    @Serializable(with = LngLatAltSerializer::class)
    val coordinates: LngLatAlt
) : Geometry {
    @Transient
    override var crs: Crs? = null

    @Transient
    override var bbox: DoubleArray? = null

    constructor(coordinates: DoubleArray) : this(LngLatAlt(coordinates))
    constructor(longitude: Double, latitude: Double) : this(LngLatAlt(longitude, latitude))
    constructor(longitude: Double, latitude: Double, altitude: Double) : this(LngLatAlt(longitude, latitude, altitude))
    constructor(longitude: Double, latitude: Double, altitude: Double, vararg additional: Double) : this(
        LngLatAlt(
            longitude,
            latitude,
            altitude,
            *additional
        )
    )
}

@Serializable
@SerialName("MultiPoint")
data class MultiPoint(
    val coordinates: List<@Serializable(with = LngLatAltSerializer::class) LngLatAlt>
) : Geometry {
    @Transient
    override var crs: Crs? = null

    @Transient
    override var bbox: DoubleArray? = null

    constructor(vararg points: LngLatAlt) : this(points.toList())
    constructor(coordinates: Array<DoubleArray>) : this(coordinates.map { LngLatAlt(it) })
}

@Serializable
@SerialName("LineString")
data class LineString(
    val coordinates: List<@Serializable(with = LngLatAltSerializer::class) LngLatAlt>
) : Geometry {
    @Transient
    override var crs: Crs? = null

    @Transient
    override var bbox: DoubleArray? = null

    constructor(vararg points: LngLatAlt) : this(points.toList())
    constructor(coordinates: Array<DoubleArray>) : this(coordinates.map { LngLatAlt(it) })
}

@Serializable
@SerialName("MultiLineString")
data class MultiLineString(
    val coordinates: List<List<@Serializable(with = LngLatAltSerializer::class) LngLatAlt>>
) : Geometry {
    @Transient
    override var crs: Crs? = null

    @Transient
    override var bbox: DoubleArray? = null

    constructor(vararg lines: List<LngLatAlt>) : this(lines.toList())
    constructor(coordinates: Array<Array<DoubleArray>>) : this(coordinates.map { line -> line.map { LngLatAlt(it) } })
}

@Serializable
@SerialName("Polygon")
data class Polygon(
    val coordinates: List<List<@Serializable(with = LngLatAltSerializer::class) LngLatAlt>>
) : Geometry {
    @Transient
    override var crs: Crs? = null

    @Transient
    override var bbox: DoubleArray? = null

    constructor() : this(emptyList())
    constructor(vararg ring: LngLatAlt) : this(listOf(ring.toList()))
    constructor(coordinates: Array<Array<DoubleArray>>) : this(coordinates.map { ring -> ring.map { LngLatAlt(it) } })

    val hasHoles: Boolean get() = coordinates.size > 1
    val exteriorRing: List<LngLatAlt> get() = coordinates.first()
    val interiorRings: List<List<LngLatAlt>>
        get() = if (coordinates.size > 1) coordinates.subList(
            1,
            coordinates.size
        ) else emptyList()

    fun getInteriorRing(index: Int): List<LngLatAlt> = coordinates[1 + index]
    fun withInteriorRing(points: List<LngLatAlt>): Polygon = Polygon(coordinates + listOf(points))
}

@Serializable
@SerialName("MultiPolygon")
data class MultiPolygon(
    val coordinates: List<List<List<@Serializable(with = LngLatAltSerializer::class) LngLatAlt>>>
) : Geometry {
    @Transient
    override var crs: Crs? = null

    @Transient
    override var bbox: DoubleArray? = null

    constructor() : this(emptyList())
    constructor(coordinates: Array<Array<Array<DoubleArray>>>) : this(coordinates.map { polygon ->
        polygon.map { ring ->
            ring.map {
                LngLatAlt(
                    it
                )
            }
        }
    })

    fun add(polygon: Polygon): MultiPolygon = MultiPolygon(coordinates + listOf(polygon.coordinates))
}

@Serializable
@SerialName("GeometryCollection")
data class GeometryCollection(
    val geometries: List<Geometry> = emptyList()
) : Geometry {
    @Transient
    override var crs: Crs? = null

    @Transient
    override var bbox: DoubleArray? = null

    constructor(geometries: Array<Geometry>) : this(geometries.toList())

    fun add(geometry: GeoJsonObject): GeometryCollection = GeometryCollection(geometries + (geometry as Geometry))
}


/**
 * Position is an alias on a DoubleArray that represents the coordinates
 * in degrees for longitude (index = 0), latitude (index = 1) and altitude (meters).
 * The altitude is not a mandatory information. The position can be 2 length array or
 * a 3 length array (with altitude)
 */
data class Crs(
    var type: CrsType? = CrsType.NAME,
    var properties: Map<String, Any>? = HashMap()
)

enum class CrsType {
    NAME, LINK;

    companion object {
        fun forValue(value: String): CrsType = valueOf(value.uppercase())
    }

    fun toValue(): String = name.lowercase()
}

@Deprecated("Use LngLatAlt directly", ReplaceWith("LngLatAlt"))
typealias Position = DoubleArray

/**
 * Type alias of an array of Positions
 */
@Deprecated("Use List<LngLatAlt> directly")
typealias Positions = Array<DoubleArray>

/**
 * Type alias for an array of Positions
 */
@Deprecated("Use List<LngLatAlt> directly")
typealias Line = Array<DoubleArray>

/**
 * Type alias of an array of lines.
 */
@Deprecated("Use List<List<LngLatAlt>> directly")
typealias Lines = Array<Array<DoubleArray>>

@Deprecated("Use List<List<List<LngLatAlt>>> directly")
typealias Surface = Array<Array<Array<DoubleArray>>>


@Deprecated("Use LngLatAlt.lon directly", ReplaceWith("this[0]"))
val DoubleArray.lon: Double
    get() = this[0]

@Deprecated("Use LngLatAlt.lat directly", ReplaceWith("this[1]"))
val DoubleArray.lat: Double
    get() = this[1]

@Deprecated("Use LngLatAlt.alt directly", ReplaceWith("if (size > 2) this[2] else null"))
val DoubleArray.alt: Double?
    get() = if (size > 2) this[2] else null


val geoJsonModule = SerializersModule {
    polymorphic(GeoJsonObject::class) {
        subclass(Point::class)
        subclass(MultiPoint::class)
        subclass(LineString::class)
        subclass(MultiLineString::class)
        subclass(Polygon::class)
        subclass(MultiPolygon::class)
        subclass(GeometryCollection::class)
        subclass(Feature::class)
        subclass(FeatureCollection::class)
    }
    polymorphic(Geometry::class) {
        subclass(Point::class)
        subclass(MultiPoint::class)
        subclass(LineString::class)
        subclass(MultiLineString::class)
        subclass(Polygon::class)
        subclass(MultiPolygon::class)
        subclass(GeometryCollection::class)
    }
}

val geoJson = Json {
    serializersModule = geoJsonModule
    ignoreUnknownKeys = true
    explicitNulls = false
}

fun GeoJsonObject.toJsonString(): String {
    val element = geoJson.encodeToJsonElement<GeoJsonObject>(this)
    if (crs == null && bbox == null) return element.toString()
    val original = element.jsonObject
    val ordered = linkedMapOf<String, JsonElement>()
    original["type"]?.let { ordered["type"] = it }
    if (crs != null) {
        ordered["crs"] = buildJsonObject {
            crs!!.type?.let { put("type", it.toValue()) }
            crs!!.properties?.let { put("properties", it.toJsonElement()) }
        }
    }
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
    jsonObj["crs"]?.let { crsEl ->
        if (crsEl !is JsonNull) {
            val crsObj = crsEl.jsonObject
            val crs = Crs()
            crsObj["type"]?.jsonPrimitive?.content?.let { crs.type = CrsType.forValue(it) }
            @Suppress("UNCHECKED_CAST")
            crsObj["properties"]?.let { if (it !is JsonNull) crs.properties = it.toAnyValue() as? Map<String, Any> }
            result.crs = crs
        }
    }
    jsonObj["bbox"]?.let { bboxEl ->
        if (bboxEl !is JsonNull) result.bbox = bboxEl.jsonArray.map { it.jsonPrimitive.double }.toDoubleArray()
    }
    return result
}

/**
 * Parse the String as a GeoJsonObject.
 */
fun String.toGeoJsonObject(): GeoJsonObject =
    geoJson.decodeFromString<GeoJsonObject>(this)

/**
 * Retrieve a list of Feature (FeatureCollection) which have properties.
 * You need to pass an extractionFunction to transform the extracted properties
 * to a specific Properties class.
 *
 * For instance:
 * ```
 * json.toFeaturesAndProperties { City( intProp("id"), stringProp("name") }
 * ```
 * @return a list of Pair<Feature, T>
 */
fun <T> String.toFeaturesAndProperties(extractFunction: FeatureProperties.() -> T): List<Pair<Feature, T>> {
    val featureCollection = geoJson.decodeFromString<FeatureCollection>(this)
    val fp = FeatureProperties()
    return featureCollection.features.map { feature ->
        @Suppress("UNCHECKED_CAST")
        fp.properties = feature.properties as? Map<String, Any?> ?: mapOf()
        Pair(feature, extractFunction(fp))
    }
}

/**
 * This class simplifies the access to feature properties. It should only
 * be used as a way to deserialize the properties of a feature using
 * the String.toFeaturesAndProperties function.
 */
class FeatureProperties {
    var properties: Map<String, Any?> = mapOf()
    fun stringProperty(name: String): String = properties[name] as String
    fun intProperty(name: String): Int = properties[name] as Int
    fun booleanProperty(name: String): Boolean = properties[name] as Boolean
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

internal fun JsonElement.toAnyValue(): Any? = when (this) {
    is JsonNull -> null
    is JsonPrimitive -> when {
        isString -> content
        else -> booleanOrNull ?: intOrNull ?: longOrNull ?: doubleOrNull ?: content
    }

    is JsonArray -> map { it.toAnyValue() }
    is JsonObject -> entries.associate { (k, v) -> k to v.toAnyValue() }
}
