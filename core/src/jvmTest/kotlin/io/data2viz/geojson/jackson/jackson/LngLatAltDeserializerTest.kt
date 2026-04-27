package io.data2viz.geojson.jackson.jackson

import io.data2viz.geojson.jackson.LngLatAlt
import io.data2viz.geojson.jackson.geoJson
import kotlinx.serialization.json.JsonElement
import org.junit.Assert
import org.junit.Test

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
        Assert.assertTrue(lngLatAlt == lngLatAlt)
    }
}
