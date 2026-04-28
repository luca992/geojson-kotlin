package io.data2viz.geojson

import kotlinx.serialization.encodeToString
import kotlin.test.*

class GeometryCollectionTest {

    @Test
    fun itShouldSerialize() {
        val gc = GeometryCollection()
            .add(Point(100.0, 0.0))
            .add(
                LineString(
                    LngLatAlt(101.0, 0.0),
                    LngLatAlt(102.0, 1.0)
                )
            )
        assertEquals(
            "{\"type\":\"GeometryCollection\","
                    + "\"geometries\":[{\"type\":\"Point\",\"coordinates\":[100.0,0.0]},"
                    + "{\"type\":\"LineString\",\"coordinates\":[[101.0,0.0],[102.0,1.0]]}]}",
            geoJson.encodeToString<GeoJsonObject>(gc)
        )
    }

    @Test
    fun itShouldDeserialize() {
        val geometryCollection = geoJson.decodeFromString<GeometryCollection>(
            "{\"type\":\"GeometryCollection\","
                    + "\"geometries\":[{\"type\":\"Point\",\"coordinates\":[100.0,0.0]},"
                    + "{\"type\":\"LineString\",\"coordinates\":[[101.0,0.0],[102.0,1.0]]}]}"
        )
        assertNotNull(geometryCollection)
    }

    @Test
    fun itShouldDeserializeSubtype() {
        val collection = geoJson.decodeFromString<FeatureCollection>(
            "{\"type\": \"FeatureCollection\","
                    + "  \"features\": ["
                    + "    {"
                    + "      \"type\": \"Feature\","
                    + "      \"geometry\": {"
                    + "        \"type\": \"GeometryCollection\","
                    + "        \"geometries\": ["
                    + "          {"
                    + "            \"type\": \"Point\","
                    + "            \"coordinates\": [100.0, 0.0]"
                    + "          }"
                    + "        ]"
                    + "      }"
                    + "    }"
                    + "  ]"
                    + "}"
        )
        assertNotNull(collection)
        assertTrue(collection.features[0].geometry is GeometryCollection)
    }
}
