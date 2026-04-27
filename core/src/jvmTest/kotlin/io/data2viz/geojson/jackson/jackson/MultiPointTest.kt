package io.data2viz.geojson.jackson.jackson

import io.data2viz.geojson.jackson.GeoJsonObject
import io.data2viz.geojson.jackson.LngLatAlt
import io.data2viz.geojson.jackson.MultiPoint
import io.data2viz.geojson.jackson.geoJson
import kotlinx.serialization.encodeToString
import org.junit.Test

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull

class MultiPointTest {

    @Test
    fun itShouldSerializeMultiPoint() {
        val multiPoint = MultiPoint(
            LngLatAlt(100.0, 0.0),
            LngLatAlt(101.0, 1.0)
        )
        //language=JSON
        assertEquals(
            """{"type":"MultiPoint","coordinates":[[100.0,0.0],[101.0,1.0]]}""",
            geoJson.encodeToString<GeoJsonObject>(multiPoint)
        )
    }

    @Test
    fun itShouldDeserializeMultiPoint() {
        val multiPoint = geoJson.decodeFromString<MultiPoint>(
            """{"type":"MultiPoint","coordinates":[[100.0,0.0],[101.0,1.0]]}"""
        )
        assertNotNull(multiPoint)
        val coordinates = multiPoint.coordinates
        PointTest.assertLngLatAlt(100.0, 0.0, java.lang.Double.NaN, coordinates[0])
        PointTest.assertLngLatAlt(101.0, 1.0, java.lang.Double.NaN, coordinates[1])
    }
}
