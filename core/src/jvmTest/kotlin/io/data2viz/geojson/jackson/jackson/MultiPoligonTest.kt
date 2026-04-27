package io.data2viz.geojson.jackson.jackson

import io.data2viz.geojson.jackson.GeoJsonObject
import io.data2viz.geojson.jackson.LngLatAlt
import io.data2viz.geojson.jackson.MultiPolygon
import io.data2viz.geojson.jackson.Polygon
import io.data2viz.geojson.jackson.geoJson
import kotlinx.serialization.encodeToString
import org.junit.Test

import org.junit.Assert.assertEquals

class MultiPoligonTest {

    @Test
    fun itShouldSerialize() {
        var multiPolygon = MultiPolygon()
        multiPolygon = multiPolygon.add(
            Polygon(
                LngLatAlt(102.0, 2.0),
                LngLatAlt(103.0, 2.0),
                LngLatAlt(103.0, 3.0),
                LngLatAlt(102.0, 3.0),
                LngLatAlt(102.0, 2.0)
            )
        )
        val polygon = Polygon(listOf(MockData.EXTERNAL))
        val polygonWithHole = polygon.withInteriorRing(MockData.INTERNAL)
        multiPolygon = multiPolygon.add(polygonWithHole)
        //language=JSON
        assertEquals(
            """{"type":"MultiPolygon","coordinates":[[[[102.0,2.0],[103.0,2.0],[103.0,3.0],[102.0,3.0],[102.0,2.0]]],[[[100.0,0.0],[101.0,0.0],[101.0,1.0],[100.0,1.0],[100.0,0.0]],[[100.2,0.2],[100.8,0.2],[100.8,0.8],[100.2,0.8],[100.2,0.2]]]]}""",
            geoJson.encodeToString<GeoJsonObject>(multiPolygon)
        )
    }

    @Test
    fun itShouldDeserialize() {
        val multiPolygon = geoJson.decodeFromString<MultiPolygon>(
            """{"type":"MultiPolygon",
                "coordinates":[
                    [[[102.0,2.0],[103.0,2.0],[103.0,3.0],[102.0,3.0],[102.0,2.0]]],
                    [[[100.0,0.0],[101.0,0.0],[101.0,1.0],[100.0,1.0],[100.0,0.0]],[[100.2,0.2],[100.8,0.2],[100.8,0.8],[100.2,0.8],[100.2,0.2]]]
                ]
                }""".trimIndent()
        )
        assertEquals(2, multiPolygon.coordinates.size.toLong())
    }
}
