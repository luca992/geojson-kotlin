package io.data2viz.geojson

import kotlinx.serialization.encodeToString
import kotlin.test.*

class PointTest {

    @Test
    fun itShouldSerializeAPoint() {
        val point = Point(100.0, 0.0)
        //language=JSON
        assertEquals(
            """{"type":"Point","coordinates":[100.0,0.0]}""",
            geoJson.encodeToString<GeoJsonObject>(point)
        )
    }

    @Test
    fun itShouldDeserializeAPoint() {
        val value = geoJson.decodeFromString<GeoJsonObject>(
            "{\"type\":\"Point\",\"coordinates\":[100.0,5.0]}"
        )
        assertNotNull(value)
        assertTrue(value is Point)
        val point = value as Point
        assertLngLatAlt(100.0, 5.0, Double.NaN, point.coordinates)
    }

    @Test
    fun itShouldDeserializeAPointWithAltitude() {
        val value = geoJson.decodeFromString<GeoJsonObject>(
            """{"type":"Point","coordinates":[100.0,5.0,123]}"""
        )
        val point = value as Point
        assertLngLatAlt(100.0, 5.0, 123.0, point.coordinates)
    }

    @Test
    fun itShouldSerializeAPointWithAltitude() {
        val point = Point(100.0, 0.0, 256.0)
        //language=JSON
        assertEquals(
            """{"type":"Point","coordinates":[100.0,0.0,256.0]}""",
            geoJson.encodeToString<GeoJsonObject>(point)
        )
    }

    @Test
    fun itShouldDeserializeAPointWithAdditionalAttributes() {
        val value = geoJson.decodeFromString<GeoJsonObject>(
            """{"type":"Point","coordinates":[100.0,5.0,123,456,789.2]}"""
        )
        val point = value as Point
        assertLngLatAlt(100.0, 5.0, 123.0, doubleArrayOf(456.0, 789.2), point.coordinates)
    }

    @Test
    fun itShouldSerializeAPointWithAdditionalAttributes() {
        val point = Point(100.0, 0.0, 256.0, 345.0, 678.0)
        //language=JSON
        assertEquals(
            """{"type":"Point","coordinates":[100.0,0.0,256.0,345.0,678.0]}""",
            geoJson.encodeToString<GeoJsonObject>(point)
        )
    }

    @Test
    fun itShouldSerializeAPointWithAdditionalAttributesAndNull() {
        val point = Point(100.0, 0.0, 256.0, 345.0, 678.0)
        //language=JSON
        assertEquals(
            """{"type":"Point","coordinates":[100.0,0.0,256.0,345.0,678.0]}""",
            geoJson.encodeToString<GeoJsonObject>(point)
        )
    }

    companion object {

        fun assertLngLatAlt(
            expectedLongitude: Double, expectedLatitude: Double, expectedAltitude: Double,
            point: LngLatAlt?
        ) {
            assertLngLatAlt(expectedLongitude, expectedLatitude, expectedAltitude, DoubleArray(0), point!!)
        }

        fun assertLngLatAlt(
            expectedLongitude: Double, expectedLatitude: Double, expectedAltitude: Double,
            expectedAdditionalElements: DoubleArray, point: LngLatAlt
        ) {
            assertEquals(expectedLongitude, point.longitude, 0.00001)
            assertEquals(expectedLatitude, point.latitude, 0.00001)
            if (expectedAltitude.isNaN()) {
                assertFalse(point.hasAltitude())
            } else {
                assertEquals(expectedAltitude, point.getAltitude(), 0.00001)
                assertTrue(expectedAdditionalElements.contentEquals(point.getAdditionalElements()))
            }
        }
    }
}
