package io.data2viz.geojson.jackson.jackson

import io.data2viz.geojson.jackson.GeoJsonObject
import io.data2viz.geojson.jackson.Point
import io.data2viz.geojson.jackson.LngLatAlt
import org.junit.Test
import org.junit.Assert.assertTrue

class GeoJsonObjectTest {

    @Test
    fun itShouldBeAGeoJsonObject() {
        val point = Point(LngLatAlt(0.0, 0.0))
        assertTrue(point is GeoJsonObject)
    }
}
