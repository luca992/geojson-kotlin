package io.data2viz.geojson

/**
 * Construct a LngLatAlt with additional elements.
 * The specification allows for any number of additional elements in a position, after lng, lat, alt.
 * http://geojson.org/geojson-spec.html#positions
 */
class LngLatAlt(
    var longitude: Double,
    var latitude: Double,
    private var altitude: Double = Double.NaN,
    vararg additionalElements: Double
) {

    private var additionalElements = DoubleArray(0)

    val lon: Double get() = longitude
    val lat: Double get() = latitude
    val alt: Double? get() = if (hasAltitude()) altitude else null

    val size: Int get() = if (hasAltitude()) 3 + getAdditionalElements().size else 2

    operator fun get(index: Int): Double = when (index) {
        0 -> longitude
        1 -> latitude
        2 -> getAltitude()
        else -> getAdditionalElements()[index - 3]
    }

    constructor(array: DoubleArray) : this(
        array[0], array[1],
        if (array.size > 2) array[2] else Double.NaN,
        *if (array.size > 3) array.sliceArray(3..<array.size) else doubleArrayOf()
    )

    fun hasAltitude(): Boolean = !altitude.isNaN()

    private fun hasAdditionalElements(): Boolean = additionalElements.isNotEmpty()

    fun getAltitude(): Double = altitude

    fun setAltitude(altitude: Double) {
        this.altitude = altitude
        checkAltitudeAndAdditionalElements()
    }

    fun getAdditionalElements(): DoubleArray = additionalElements

    fun setAdditionalElements(vararg additionalElements: Double) {
        require(additionalElements.none { it.isNaN() }) { "No additional elements may be NaN." }
        require(additionalElements.none { it.isInfinite() }) { "No additional elements may be infinite." }
        this.additionalElements = additionalElements
        checkAltitudeAndAdditionalElements()
    }

    fun toDoubleArray(): DoubleArray = buildList {
        add(longitude)
        add(latitude)
        if (hasAltitude()) {
            add(altitude)
            additionalElements.forEach { add(it) }
        }
    }.toDoubleArray()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is LngLatAlt) return false
        return longitude == other.longitude
                && latitude == other.latitude
                && altitude.toBits() == other.altitude.toBits()
                && additionalElements.contentEquals(other.getAdditionalElements())
    }

    override fun hashCode(): Int {
        var result = longitude.toBits().hashCode()
        result = 31 * result + latitude.toBits().hashCode()
        result = 31 * result + altitude.toBits().hashCode()
        for (element in additionalElements) {
            result = 31 * result + element.toBits().hashCode()
        }
        return result
    }

    override fun toString(): String {
        var s = "LngLatAlt{longitude=$longitude, latitude=$latitude, altitude=$altitude"
        s += if (additionalElements.isNotEmpty()) additionalElements.joinToString(
            prefix = ", additionalElements=[",
            postfix = "]}"
        ) else "}"
        return s
    }

    private fun checkAltitudeAndAdditionalElements() {
        require(hasAltitude() || !hasAdditionalElements()) { "Additional Elements are only valid if Altitude is also provided." }
    }

    init {
        setAdditionalElements(*additionalElements)
        checkAltitudeAndAdditionalElements()
    }
}
