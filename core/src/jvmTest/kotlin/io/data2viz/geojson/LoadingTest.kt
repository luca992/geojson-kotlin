package io.data2viz.geojson

import kotlin.test.Test
import kotlin.test.assertNotNull

class LoadingTest {

    @Test
    fun loadNyGeoJson() {
        val json = this.javaClass.getResourceAsStream("/ny.json")!!.bufferedReader().readText()
        val time = System.currentTimeMillis()
        val geoJsonObject = json.toGeoJsonObject()
        assertNotNull(geoJsonObject)
        println("loading in ${System.currentTimeMillis() - time} ms.")
    }
}
