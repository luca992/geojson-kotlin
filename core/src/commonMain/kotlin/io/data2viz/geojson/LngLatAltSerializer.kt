package io.data2viz.geojson

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.*

object LngLatAltSerializer : KSerializer<LngLatAlt> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("LngLatAlt")

    override fun serialize(encoder: Encoder, value: LngLatAlt) {
        val jsonEncoder = encoder as JsonEncoder
        val elements = buildList {
            add(JsonPrimitive(value.longitude))
            add(JsonPrimitive(value.latitude))
            if (value.hasAltitude()) {
                add(JsonPrimitive(value.getAltitude()))
                for (d in value.getAdditionalElements()) {
                    add(JsonPrimitive(d))
                }
            }
        }
        jsonEncoder.encodeJsonElement(JsonArray(elements))
    }

    override fun deserialize(decoder: Decoder): LngLatAlt {
        val jsonDecoder = decoder as JsonDecoder
        val array = jsonDecoder.decodeJsonElement().jsonArray
        val lng = array[0].jsonPrimitive.double
        val lat = array[1].jsonPrimitive.double
        return if (array.size > 2) {
            val alt = array[2].jsonPrimitive.double
            val additional = if (array.size > 3) {
                DoubleArray(array.size - 3) { array[it + 3].jsonPrimitive.double }
            } else {
                DoubleArray(0)
            }
            LngLatAlt(lng, lat, alt, *additional)
        } else {
            LngLatAlt(lng, lat)
        }
    }
}
