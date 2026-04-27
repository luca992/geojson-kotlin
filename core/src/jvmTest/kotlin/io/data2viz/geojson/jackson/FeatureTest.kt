package io.data2viz.geojson.jackson

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.JsonObject
import org.junit.Test

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull

class FeatureTest {

    private val testObject = Feature(
        geometry = Point(100.0, 0.0),
        properties = JsonObject(emptyMap())
    )

    @Test
    fun itShouldHaveProperties() {
        assertNotNull(testObject.properties)
    }

    @Test
    fun itShouldSerializeFeature() {
        // http://geojson.org/geojson-spec.html#feature-objects
        // A feature object must have a member with the name "properties".
        // The value of the properties member is an object (any JSON object or a JSON null value).

        //language=JSON
        assertEquals(
            """{"type":"Feature","properties":{},"geometry":{"type":"Point","coordinates":[100.0,0.0]}}""",
            geoJson.encodeToString<GeoJsonObject>(testObject)
        )
    }
}
