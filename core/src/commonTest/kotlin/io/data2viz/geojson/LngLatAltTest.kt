package io.data2viz.geojson

import kotlin.test.*

class LngLatAltTest {

    @Test
    fun should_LngLatAlt_equals_without_alt() {
        val first = LngLatAlt(14.0, 13.0)
        val second = LngLatAlt(14.0, 13.0)
        assertEquals(second, first)
    }

    @Test
    fun should_LngLatAlt_equals_with_alt() {
        val first = LngLatAlt(14.0, 13.0, 15.0)
        val second = LngLatAlt(14.0, 13.0, 15.0)
        assertEquals(second, first)
    }

    @Test
    fun should_not_LngLatAlt_equals_with_alt() {
        val first = LngLatAlt(14.0, 13.0, 15.0)
        val second = LngLatAlt(14.0, 13.0, 16.0)
        assertNotEquals(second, first)
    }

    @Test
    fun should_not_LngLatAlt_equals_without_alt() {
        val first = LngLatAlt(14.0, 14.0, 15.0)
        val second = LngLatAlt(14.0, 13.0, 16.0)
        assertNotEquals(second, first)
    }

    @Test
    fun should_LngLatAlt_equals_with_additional_elements() {
        val first = LngLatAlt(14.0, 14.0, 15.0, 16.0, 17.0)
        val second = LngLatAlt(14.0, 14.0, 15.0, 16.0, 17.0)
        assertEquals(second, first)
        assertEquals(second.hashCode().toLong(), first.hashCode().toLong())
    }

    @Test
    fun should_LngLatAlt_equals_with_additional_elements_and_null() {
        val first = LngLatAlt(14.0, 14.0, 15.0, 16.0, 17.0)
        val second = LngLatAlt(14.0, 14.0, 15.0, 16.0, 17.0)
        assertEquals(second, first)
        assertEquals(second.hashCode().toLong(), first.hashCode().toLong())
    }

    @Test
    fun should_not_LngLatAlt_equals_without_additional_elements() {
        val first = LngLatAlt(14.0, 14.0, 15.0, 16.0, 17.0)
        val second = LngLatAlt(14.0, 14.0, 15.0)
        assertNotEquals(second, first)
        assertNotEquals(second.hashCode().toLong(), first.hashCode().toLong())
    }

    @Test
    fun should_not_LngLatAlt_equals_with_additional_elements_in_different_order() {
        val first = LngLatAlt(14.0, 14.0, 15.0, 16.0, 17.0)
        val second = LngLatAlt(14.0, 14.0, 15.0, 17.0, 16.0)
        assertNotEquals(second, first)
        assertNotEquals(second.hashCode().toLong(), first.hashCode().toLong())
    }

    @Test
    fun should_not_LngLatAlt_equals_with_additional_elements_and_different_size() {
        val first = LngLatAlt(14.0, 14.0, 15.0, 16.0, 17.0)
        val second = LngLatAlt(14.0, 14.0, 15.0, 16.0, 17.0, 18.0)
        assertNotEquals(second, first)
        assertNotEquals(second.hashCode().toLong(), first.hashCode().toLong())
    }

    @Test
    fun should_LngLatAlt_throw_if_alt_not_specified_in_constructor() {
        assertFailsWith<IllegalArgumentException> {
            LngLatAlt(14.0, 14.0, Double.NaN, 16.0, 17.0)
        }
    }

    @Test
    fun should_LngLatAlt_throw_if_alt_set_to_Nan_with_additional_elements() {
        val lngLatAlt = LngLatAlt(14.0, 14.0, 15.0, 16.0, 17.0)
        assertFailsWith<IllegalArgumentException> {
            lngLatAlt.setAltitude(Double.NaN)
        }
    }

    @Test
    fun should_LngLatAlt_throw_if_additional_elements_set_with_missing_alt() {
        val lngLatAlt = LngLatAlt(14.0, 14.0)
        assertFailsWith<IllegalArgumentException> {
            lngLatAlt.setAdditionalElements(42.0)
        }
    }

    @Test
    fun should_LngLatAlt_throw_if_additional_elements_set_with_Nan_alt() {
        val lngLatAlt = LngLatAlt(14.0, 14.0, Double.NaN)
        assertFailsWith<IllegalArgumentException> {
            lngLatAlt.setAdditionalElements(42.0)
        }
    }

    @Test
    fun should_LngLatAlt_throw_if_any_additional_elements_constructed_to_Nan() {
        assertFailsWith<IllegalArgumentException> {
            LngLatAlt(14.0, 14.0, 15.0, 16.0, Double.NaN, 17.0)
        }
    }

    @Test
    fun should_LngLatAlt_throw_if_any_additional_elements_constructed_to_Positive_Infinity() {
        assertFailsWith<IllegalArgumentException> {
            LngLatAlt(14.0, 14.0, 15.0, 16.0, Double.POSITIVE_INFINITY, 17.0)
        }
    }

    @Test
    fun should_LngLatAlt_throw_if_any_additional_elements_constructed_to_Negative_Infinity() {
        assertFailsWith<IllegalArgumentException> {
            LngLatAlt(14.0, 14.0, 15.0, 16.0, Double.NEGATIVE_INFINITY, 17.0)
        }
    }

    @Test
    fun should_LngLatAlt_throw_if_any_additional_elements_set_to_Nan() {
        val lngLatAlt = LngLatAlt(14.0, 14.0, 15.0)
        assertFailsWith<IllegalArgumentException> {
            lngLatAlt.setAdditionalElements(16.0, Double.NaN, 17.0)
        }
    }

    @Test
    fun should_LngLatAlt_throw_if_any_additional_elements_set_to_Positive_Infinity() {
        val lngLatAlt = LngLatAlt(14.0, 14.0, 15.0)
        assertFailsWith<IllegalArgumentException> {
            lngLatAlt.setAdditionalElements(16.0, Double.POSITIVE_INFINITY, 17.0)
        }
    }

    @Test
    fun should_LngLatAlt_throw_if_any_additional_elements_set_to_Negative_Infinity() {
        val lngLatAlt = LngLatAlt(14.0, 14.0, 15.0)
        assertFailsWith<IllegalArgumentException> {
            lngLatAlt.setAdditionalElements(16.0, Double.NEGATIVE_INFINITY, 17.0)
        }
    }
}
