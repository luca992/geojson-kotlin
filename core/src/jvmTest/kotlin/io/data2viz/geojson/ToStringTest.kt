package io.data2viz.geojson

import kotlin.test.*

class ToStringTest {

    @Test
    fun itShouldToStringFeature() {
        assertEquals("Feature(propertiesObject=null, geometry=null, idPrimitive=null)", Feature().toString())
    }

    @Test
    fun itShouldToStringFeatureCollection() {
        assertEquals("FeatureCollection(features=[])", FeatureCollection().toString())
    }

    @Test
    fun itShouldToStringPoint() {
        val geometry = Point(10.0, 20.0)
        assertEquals(
            "Point(coordinates=LngLatAlt{longitude=10.0, latitude=20.0, altitude=NaN})",
            geometry.toString()
        )
    }

    @Test
    fun itShouldToStringPointWithAdditionalElements() {
        val geometry = Point(10.0, 20.0, 30.0, 40.0, 50.0)
        assertEquals(
            "Point(coordinates=LngLatAlt{longitude=10.0, latitude=20.0, altitude=30.0, additionalElements=[40.0, 50.0]})",
            geometry.toString()
        )
    }

    @Test
    fun itShouldToStringPointWithAdditionalElementsAndIgnoreNulls() {
        val geometry = Point(10.0, 20.0, 30.0, 40.0, 50.0)
        assertEquals(
            "Point(coordinates=LngLatAlt{longitude=10.0, latitude=20.0, altitude=30.0, additionalElements=[40.0, 50.0]})",
            geometry.toString()
        )
    }

    @Test
    fun itShouldToStringPolygon() {
        val geometry = Polygon(
            LngLatAlt(10.0, 20.0),
            LngLatAlt(30.0, 40.0),
            LngLatAlt(10.0, 40.0),
            LngLatAlt(10.0, 20.0)
        )
        assertEquals(
            "Polygon(coordinates=[[LngLatAlt{longitude=10.0, latitude=20.0, altitude=NaN}, "
                    + "LngLatAlt{longitude=30.0, latitude=40.0, altitude=NaN}, LngLatAlt{longitude=10.0, latitude=40.0, altitude=NaN}, "
                    + "LngLatAlt{longitude=10.0, latitude=20.0, altitude=NaN}]])",
            geometry.toString()
        )
    }

    @Test
    fun itShouldToStringMultiPolygon() {
        val polygon1 = Polygon(
            LngLatAlt(10.0, 20.0), LngLatAlt(30.0, 40.0),
            LngLatAlt(10.0, 40.0), LngLatAlt(10.0, 20.0)
        )
        val geometry = polygon1.let { p1 ->
            val p2 = Polygon(
                LngLatAlt(5.0, 20.0), LngLatAlt(30.0, 40.0),
                LngLatAlt(10.0, 40.0), LngLatAlt(5.0, 20.0)
            )
            MultiPolygon().add(p1).add(p2)
        }
        assertEquals(
            "MultiPolygon(coordinates=[[[LngLatAlt{longitude=10.0, latitude=20.0, altitude=NaN}, "
                    + "LngLatAlt{longitude=30.0, latitude=40.0, altitude=NaN}, "
                    + "LngLatAlt{longitude=10.0, latitude=40.0, altitude=NaN}, "
                    + "LngLatAlt{longitude=10.0, latitude=20.0, altitude=NaN}]], "
                    + "[[LngLatAlt{longitude=5.0, latitude=20.0, altitude=NaN}, "
                    + "LngLatAlt{longitude=30.0, latitude=40.0, altitude=NaN}, "
                    + "LngLatAlt{longitude=10.0, latitude=40.0, altitude=NaN}, "
                    + "LngLatAlt{longitude=5.0, latitude=20.0, altitude=NaN}]]])",
            geometry.toString()
        )
    }

    @Test
    fun itShouldToStringLineString() {
        val geometry = LineString(
            LngLatAlt(49.0, 9.0),
            LngLatAlt(41.0, 1.0)
        )
        assertEquals(
            "LineString(coordinates=["
                    + "LngLatAlt{longitude=49.0, latitude=9.0, altitude=NaN}, "
                    + "LngLatAlt{longitude=41.0, latitude=1.0, altitude=NaN}])",
            geometry.toString()
        )
    }

    @Test
    fun itShouldToStringMultiLineString() {
        val geometry = MultiLineString(
            listOf(LngLatAlt(49.0, 9.0), LngLatAlt(41.0, 1.0)),
            listOf(LngLatAlt(10.0, 20.0), LngLatAlt(30.0, 40.0))
        )
        assertEquals(
            "MultiLineString(coordinates=[[LngLatAlt{longitude=49.0, latitude=9.0, altitude=NaN}, "
                    + "LngLatAlt{longitude=41.0, latitude=1.0, altitude=NaN}], "
                    + "[LngLatAlt{longitude=10.0, latitude=20.0, altitude=NaN}, "
                    + "LngLatAlt{longitude=30.0, latitude=40.0, altitude=NaN}]])",
            geometry.toString()
        )
    }
}
