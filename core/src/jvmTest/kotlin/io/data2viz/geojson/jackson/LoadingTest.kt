package io.data2viz.geojson.jackson

import io.data2viz.geojson.toGeoJsonObject
import org.junit.Test

import org.junit.Assert.assertNotNull

class LoadingTest {

    @Test
    fun loadNyGeoJson() {
        val json = this.javaClass.getResourceAsStream("/ny.json")!!.bufferedReader().readText()
        val time = System.currentTimeMillis()
        val geojson = geoJson.decodeFromString<GeoJsonObject>(json)
        val geoJsonObject = geojson.toGeoJsonObject()
        println("loading in ${System.currentTimeMillis() - time} ms.")
    }
}
