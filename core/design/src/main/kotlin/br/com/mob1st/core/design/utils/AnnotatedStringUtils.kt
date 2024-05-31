package br.com.mob1st.core.design.utils

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.util.fastMap

/**
 * Helper function used to find the [SpanStyle]s in the given paragraph range and also convert the
 * range of those [SpanStyle]s to paragraph local range.
 *
 * @param start The start index of the paragraph range, inclusive
 * @param end The end index of the paragraph range, exclusive
 * @return The list of converted [SpanStyle]s in the given paragraph range
 */
internal fun AnnotatedString.getLocalSpanStyles(
    start: Int,
    end: Int,
): List<AnnotatedString.Range<SpanStyle>> {
    if (start == end) return emptyList()
    if (spanStyles.isEmpty()) return emptyList()
    // If the given range covers the whole AnnotatedString, return SpanStyles without conversion.
    if (start == 0 && end >= text.length) {
        return spanStyles
    }
    return spanStyles.fastFilter { intersect(start, end, it.start, it.end) }
        .fastMap {
            AnnotatedString.Range(
                it.item,
                it.start.coerceIn(start, end) - start,
                it.end.coerceIn(start, end) - start,
            )
        }
}

/**
 * Helper function used to find the [ParagraphStyle]s in the given range and also convert the range
 * of those styles to the local range.
 *
 * @param start The start index of the range, inclusive
 * @param end The end index of the range, exclusive
 */
internal fun AnnotatedString.getLocalParagraphStyle(
    start: Int,
    end: Int,
): List<AnnotatedString.Range<ParagraphStyle>> {
    if (start == end) return emptyList()
    if (paragraphStyles.isEmpty()) return emptyList()
    // If the given range covers the whole AnnotatedString, return SpanStyles without conversion.
    if (start == 0 && end >= text.length) {
        return paragraphStyles
    }

    return paragraphStyles.fastFilter { intersect(start, end, it.start, it.end) }
        .fastMap {
            AnnotatedString.Range(
                it.item,
                it.start.coerceIn(start, end) - start,
                it.end.coerceIn(start, end) - start,
            )
        }
}

private fun intersect(
    lStart: Int,
    lEnd: Int,
    rStart: Int,
    rEnd: Int,
) = maxOf(lStart, rStart) < minOf(lEnd, rEnd) ||
    contains(lStart, lEnd, rStart, rEnd) || contains(rStart, rEnd, lStart, lEnd)

private fun contains(
    baseStart: Int,
    baseEnd: Int,
    targetStart: Int,
    targetEnd: Int,
) = (baseStart <= targetStart && targetEnd <= baseEnd) &&
    (baseEnd != targetEnd || (targetStart == targetEnd) == (baseStart == baseEnd))
