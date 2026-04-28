package io.data2viz.geojson

import kotlinx.serialization.json.JsonElement
import kotlin.test.*

class LngLatAltSerializerTest {

    @Test
    fun testSerialization() {
        val position = LngLatAlt(49.43245, 52.42345, 120.34626)
        val correctJson = "[49.43245,52.42345,120.34626]"
        val producedJson = geoJson.encodeToString(
            JsonElement.serializer(),
            geoJson.encodeToJsonElement(LngLatAltSerializer, position)
        )
        assertEquals(correctJson, producedJson)
    }
}
