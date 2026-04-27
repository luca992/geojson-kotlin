package io.data2viz.geojson.jackson.jackson

import io.data2viz.geojson.jackson.GeoJsonObject
import io.data2viz.geojson.jackson.LngLatAlt
import io.data2viz.geojson.jackson.Polygon
import io.data2viz.geojson.jackson.geoJson
import kotlinx.serialization.encodeToString
import org.junit.Test

import org.junit.Assert.assertEquals

class PolygonTest {

    @Test
    fun itShouldSerialize() {
        val polygon = Polygon(listOf(MockData.EXTERNAL))
        //language=JSON
        assertEquals(
            """{"type":"Polygon","coordinates":[[[100.0,0.0],[101.0,0.0],[101.0,1.0],[100.0,1.0],[100.0,0.0]]]}""",
            geoJson.encodeToString<GeoJsonObject>(polygon)
        )
    }

    @Test
    fun itShouldSerializeWithHole() {
        val polygon = Polygon(listOf(MockData.EXTERNAL))
        val polygonWithHole = polygon.withInteriorRing(MockData.INTERNAL)
        //language=JSON
        assertEquals(
            """{"type":"Polygon","coordinates":[[[100.0,0.0],[101.0,0.0],[101.0,1.0],[100.0,1.0],[100.0,0.0]],[[100.2,0.2],[100.8,0.2],[100.8,0.8],[100.2,0.8],[100.2,0.2]]]}""",
            geoJson.encodeToString<GeoJsonObject>(polygonWithHole)
        )
    }

    @Test
    fun itShouldDeserialize() {
        val polygon = geoJson.decodeFromString<Polygon>(
            """{"type":"Polygon","coordinates":[[[100.0,0.0],[101.0,0.0],[101.0,1.0],[100.0,1.0],[100.0,0.0]],[[100.2,0.2],[100.8,0.2],[100.8,0.8],[100.2,0.8],[100.2,0.2]]]}"""
        )
        assertListEquals(MockData.EXTERNAL, polygon.exteriorRing)
        assertListEquals(MockData.INTERNAL, polygon.getInteriorRing(0))
        assertListEquals(MockData.INTERNAL, polygon.interiorRings[0])
    }

    private fun assertListEquals(expectedList: List<LngLatAlt>, actualList: List<LngLatAlt>) {
        for (x in actualList.indices) {
            val expected = expectedList[x]
            val actual = actualList[x]
            PointTest.assertLngLatAlt(expected.longitude, expected.latitude, expected.getAltitude(), actual)
        }
    }
}
