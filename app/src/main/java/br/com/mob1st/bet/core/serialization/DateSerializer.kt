package br.com.mob1st.bet.core.serialization

import br.com.mob1st.bet.core.arrow.dateTimeIso
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.util.Date

/**
 * Default date serializer for ISO date time format
 */
object DateSerializer : KSerializer<Date> {

    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(
        serialName = "Date",
        kind = PrimitiveKind.STRING
    )

    override fun deserialize(decoder: Decoder): Date {
        val strDate = decoder.decodeString()
        return dateTimeIso.reverseGet(strDate)
    }

    override fun serialize(encoder: Encoder, value: Date) {
        encoder.encodeString(dateTimeIso.get(value))
    }
}