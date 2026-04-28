package io.data2viz.geojson

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class CrsTest {

    @Test
    fun itShouldParseCrsWithLink() {
        val value = """{"crs": {
                    "type": "link",
                    "properties": {
                        "href": "http://example.com/crs/42",
                        "type": "proj4"
                        }
                    },"type":"Point",
                    "coordinates":[100.0,5.0]
                }""".trimIndent().decodeGeoJson<GeoJsonObject>()
        assertNotNull(value)
        assertEquals(CrsType.LINK, value.crs!!.type)
    }

    @Test
    fun itShouldSerializeCrsWithLink() {
        val point = Point(100.0, 1.0)
        val crs = Crs()
        crs.type = CrsType.LINK
        point.crs = crs
        val value = point.toJsonString()
        assertEquals("""{"type":"Point","crs":{"type":"link","properties":{}},"coordinates":[100.0,1.0]}""", value)
    }
}
