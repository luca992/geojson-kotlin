package io.data2viz.geojson

import kotlin.test.*

class GeoJsonObjectTest {

    @Test
    fun itShouldBeAGeoJsonObject() {
        val point = Point(LngLatAlt(0.0, 0.0))
        assertTrue(point is GeoJsonObject)
    }
}
