package io.data2viz.geojson

import kotlinx.serialization.json.JsonElement
import kotlin.test.*

/**
 * Created by babbleshack on 27/11/16.
 */
class LngLatAltDeserializerTest {

    @Test
    fun deserializeMongoLngLatAlt() {
        val lngLatAlt = LngLatAlt(10.0, 15.0, 5.0)
        val lngLatAltJson = geoJson.encodeToString(
            JsonElement.serializer(),
            geoJson.encodeToJsonElement(LngLatAltSerializer, lngLatAlt)
        )
        lngLatAltJson.replace("10.0", "\"10.0\"")
        lngLatAltJson.replace("15.0", "\"15.0\"")
        val lngLatAlt1 = geoJson.decodeFromString(LngLatAltSerializer, lngLatAltJson)
        assertTrue(lngLatAlt == lngLatAlt)
    }
}
