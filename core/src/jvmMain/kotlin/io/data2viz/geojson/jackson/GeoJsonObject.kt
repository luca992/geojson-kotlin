package io.data2viz.geojson.jackson

import io.data2viz.geojson.jackson.jackson.CrsType
import io.data2viz.geojson.jackson.jackson.LngLatAltSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject

@Serializable
sealed interface GeoJsonObject {
    var crs: Crs?
    var bbox: DoubleArray?
}

@Serializable
sealed interface Geometry : GeoJsonObject

@Serializable @SerialName("Point")
data class Point(
    @Serializable(with = LngLatAltSerializer::class)
    val coordinates: LngLatAlt
) : Geometry {
    @Transient override var crs: Crs? = null
    @Transient override var bbox: DoubleArray? = null
    constructor(longitude: Double, latitude: Double) : this(LngLatAlt(longitude, latitude))
    constructor(longitude: Double, latitude: Double, altitude: Double) : this(LngLatAlt(longitude, latitude, altitude))
    constructor(longitude: Double, latitude: Double, altitude: Double, vararg additional: Double) : this(LngLatAlt(longitude, latitude, altitude, *additional))

}

@Serializable @SerialName("MultiPoint")
data class MultiPoint(
    val coordinates: List<@Serializable(with = LngLatAltSerializer::class) LngLatAlt>
) : Geometry {
    @Transient override var crs: Crs? = null
    @Transient override var bbox: DoubleArray? = null
    constructor(vararg points: LngLatAlt) : this(points.toList())

}

@Serializable @SerialName("LineString")
data class LineString(
    val coordinates: List<@Serializable(with = LngLatAltSerializer::class) LngLatAlt>
) : Geometry {
    @Transient override var crs: Crs? = null
    @Transient override var bbox: DoubleArray? = null
    constructor(vararg points: LngLatAlt) : this(points.toList())

}

@Serializable @SerialName("MultiLineString")
data class MultiLineString(
    val coordinates: List<List<@Serializable(with = LngLatAltSerializer::class) LngLatAlt>>
) : Geometry {
    @Transient override var crs: Crs? = null
    @Transient override var bbox: DoubleArray? = null
    constructor(vararg lines: List<LngLatAlt>) : this(lines.toList())

}

@Serializable @SerialName("Polygon")
data class Polygon(
    val coordinates: List<List<@Serializable(with = LngLatAltSerializer::class) LngLatAlt>>
) : Geometry {
    @Transient override var crs: Crs? = null
    @Transient override var bbox: DoubleArray? = null
    constructor() : this(emptyList())
    constructor(vararg ring: LngLatAlt) : this(listOf(ring.toList()))
    val exteriorRing: List<LngLatAlt> get() = coordinates.first()
    val interiorRings: List<List<LngLatAlt>> get() = if (coordinates.size > 1) coordinates.subList(1, coordinates.size) else emptyList()
    fun getInteriorRing(index: Int): List<LngLatAlt> = coordinates[1 + index]
    fun withInteriorRing(points: List<LngLatAlt>): Polygon = Polygon(coordinates + listOf(points))

}

@Serializable @SerialName("MultiPolygon")
data class MultiPolygon(
    val coordinates: List<List<List<@Serializable(with = LngLatAltSerializer::class) LngLatAlt>>>
) : Geometry {
    @Transient override var crs: Crs? = null
    @Transient override var bbox: DoubleArray? = null
    constructor() : this(emptyList())
    fun add(polygon: Polygon): MultiPolygon = MultiPolygon(coordinates + listOf(polygon.coordinates))

}

@Serializable @SerialName("GeometryCollection")
data class GeometryCollection(
    val geometries: List<Geometry> = emptyList()
) : Geometry {
    @Transient override var crs: Crs? = null
    @Transient override var bbox: DoubleArray? = null
    fun add(geometry: GeoJsonObject): GeometryCollection = GeometryCollection(geometries + (geometry as Geometry))

}

@Serializable
@SerialName("Feature")
data class Feature(
    val properties: JsonObject? = null,
    val geometry: Geometry? = null,
    val id: JsonElement? = null
) : GeoJsonObject {
    @Transient override var crs: Crs? = null
    @Transient override var bbox: DoubleArray? = null

}

@Serializable @SerialName("FeatureCollection")
data class FeatureCollection(
    val features: List<Feature> = emptyList()
) : GeoJsonObject {
    @Transient override var crs: Crs? = null
    @Transient override var bbox: DoubleArray? = null

}

// ---- CRS ----

data class Crs(
    var type: CrsType? = CrsType.NAME,
    var properties: Map<String, Any>? = HashMap()
)

// ---- Json instance ----

val geoJson = Json {
    ignoreUnknownKeys = true
    explicitNulls = false
}
