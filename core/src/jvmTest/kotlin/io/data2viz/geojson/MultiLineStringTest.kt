package io.data2viz.geojson

import kotlinx.serialization.encodeToString
import kotlin.test.*

class MultiLineStringTest {

    @Test
    fun itShouldSerialize() {
        val multiLineString = MultiLineString(
            listOf(
                LngLatAlt(100.0, 0.0),
                LngLatAlt(101.0, 1.0)
            ),
            listOf(
                LngLatAlt(102.0, 2.0),
                LngLatAlt(103.0, 3.0)
            )
        )
        //language=JSON
        assertEquals(
            """{"type":"MultiLineString","coordinates":[[[100.0,0.0],[101.0,1.0]],[[102.0,2.0],[103.0,3.0]]]}""",
            geoJson.encodeToString<GeoJsonObject>(multiLineString)
        )
    }
}
