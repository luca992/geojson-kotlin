package io.data2viz.geojson.jackson.jackson

import io.data2viz.geojson.jackson.LngLatAlt
import io.data2viz.geojson.jackson.geoJson
import kotlinx.serialization.json.JsonElement
import org.junit.Assert
import org.junit.Test

class LngLatAltSerializerTest {

    @Test
    fun testSerialization() {
        val position = LngLatAlt(49.43245, 52.42345, 120.34626)
        val correctJson = "[49.43245,52.42345,120.34626]"
        val producedJson = geoJson.encodeToString(
            JsonElement.serializer(),
            geoJson.encodeToJsonElement(LngLatAltSerializer, position)
        )
        Assert.assertEquals(correctJson, producedJson)
    }
}
