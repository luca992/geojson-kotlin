package io.data2viz.geojson

import kotlinx.serialization.encodeToString
import kotlin.test.*

class LineStringTest {

    @Test
    fun itShouldSerializeMultiPoint() {
        val lineString = LineString(
            LngLatAlt(100.0, 0.0),
            LngLatAlt(101.0, 1.0)
        )
        //language=JSON
        assertEquals(
            """{"type":"LineString","coordinates":[[100.0,0.0],[101.0,1.0]]}""",
            geoJson.encodeToString<GeoJsonObject>(lineString)
        )
    }

    @Test
    fun itShouldDeserializeLineString() {
        val lineString = geoJson.decodeFromString<LineString>(
            """{"type":"LineString","coordinates":[[100.0,0.0],[101.0,1.0]]}"""
        )
        assertNotNull(lineString)
        val coordinates = lineString.coordinates
        PointTest.assertLngLatAlt(100.0, 0.0, Double.NaN, coordinates[0])
        PointTest.assertLngLatAlt(101.0, 1.0, Double.NaN, coordinates[1])
    }
}
