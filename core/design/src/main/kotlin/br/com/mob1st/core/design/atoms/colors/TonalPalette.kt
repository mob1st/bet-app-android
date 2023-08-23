package br.com.mob1st.core.design.atoms.colors
import androidx.compose.ui.graphics.Color

/**
 * A tons of colors organized in a Material Design 3 palette.
 */
internal data class TonalPalette constructor(
    val x0: Color = Color.Unspecified,
    val x5: Color = Color.Unspecified,
    val x10: Color = Color.Unspecified,
    val x15: Color = Color.Unspecified,
    val x20: Color = Color.Unspecified,
    val x25: Color = Color.Unspecified,
    val x30: Color = Color.Unspecified,
    val x35: Color = Color.Unspecified,
    val x40: Color = Color.Unspecified,
    val x50: Color = Color.Unspecified,
    val x60: Color = Color.Unspecified,
    val x70: Color = Color.Unspecified,
    val x80: Color = Color.Unspecified,
    val x90: Color = Color.Unspecified,
    val x95: Color = Color.Unspecified,
    val x98: Color = Color.Unspecified,
    val x99: Color = Color.Unspecified,
    val x100: Color = Color.Unspecified,
) {
    @Suppress("MagicNumber")
    /*
      Color names from https://coolors.co/
     */
    companion object {

        fun TropicalIndigo() = TonalPalette(
            x0 = Color(0xff000000),
            x10 = Color(0xff1a0064),
            x20 = Color(0xff2e0996),
            x25 = Color(0xff391fa1),
            x30 = Color(0xff452eac),
            x35 = Color(0xff513cb9),
            x40 = Color(0xff5d4ac5),
            x50 = Color(0xff7764e0),
            x60 = Color(0xff917ffc),
            x70 = Color(0xffac9fff),
            x80 = Color(0xffc8bfff),
            x90 = Color(0xffe5deff),
            x95 = Color(0xfff4eeff),
            x98 = Color(0xfffdf8ff),
            x99 = Color(0xfffffbff),
            x100 = Color(0xffffffff)
        )

        fun Periwinkle() = TonalPalette(
            x0 = Color(0xff000000),
            x10 = Color(0xff0d0664),
            x20 = Color(0xff252477),
            x25 = Color(0xff313083),
            x30 = Color(0xff3c3c8f),
            x35 = Color(0xff48499c),
            x40 = Color(0xff5455a9),
            x50 = Color(0xff6d6ec4),
            x60 = Color(0xff8788df),
            x70 = Color(0xffa2a3fc),
            x80 = Color(0xffc1c1ff),
            x90 = Color(0xffe2dfff),
            x95 = Color(0xfff2efff),
            x98 = Color(0xfffcf8ff),
            x99 = Color(0xfffffbff),
            x100 = Color(0xffffffff)
        )

        fun AntiqueWhite() = TonalPalette(
            x0 = Color(0xff000000),
            x10 = Color(0xff2a1800),
            x20 = Color(0xff462b00),
            x25 = Color(0xff543500),
            x30 = Color(0xff643f00),
            x35 = Color(0xff734a00),
            x40 = Color(0xff835400),
            x50 = Color(0xffa46b00),
            x60 = Color(0xffc48319),
            x70 = Color(0xffe39d35),
            x80 = Color(0xffffb957),
            x90 = Color(0xffffddb5),
            x95 = Color(0xffffeedd),
            x98 = Color(0xfffff8f4),
            x99 = Color(0xfffffbff),
            x100 = Color(0xffffffff)
        )

        fun GhostWhite() = TonalPalette(
            x0 = Color(0xff000000),
            x10 = Color(0xff001849),
            x20 = Color(0xff002b75),
            x25 = Color(0xff103683),
            x30 = Color(0xff21428f),
            x35 = Color(0xff2f4e9c),
            x40 = Color(0xff3c5ba9),
            x50 = Color(0xff5674c4),
            x60 = Color(0xff718ee0),
            x70 = Color(0xff8ca8fc),
            x80 = Color(0xffb3c5ff),
            x90 = Color(0xffdbe1ff),
            x95 = Color(0xffeef0ff),
            x98 = Color(0xfffaf8ff),
            x99 = Color(0xfffefbff),
            x100 = Color(0xffffffff)
        )

        fun SalmonPink() = TonalPalette(
            x0 = Color(0xff000000),
            x10 = Color(0xff40000d),
            x20 = Color(0xff5f121f),
            x25 = Color(0xff6e1e29),
            x30 = Color(0xff7d2934),
            x35 = Color(0xff8d353f),
            x40 = Color(0xff9c404a),
            x50 = Color(0xffbb5861),
            x60 = Color(0xffda717a),
            x70 = Color(0xfffa8a93),
            x80 = Color(0xffffb2b6),
            x90 = Color(0xffffdadb),
            x95 = Color(0xffffedec),
            x98 = Color(0xfffff8f7),
            x99 = Color(0xfffffbff),
            x100 = Color(0xffffffff)
        )

        fun Gray() = TonalPalette(
            x0 = Color(0xff000000),
            x10 = Color(0xff1c1b22),
            x20 = Color(0xff312f38),
            x25 = Color(0xff3c3a43),
            x30 = Color(0xff48454f),
            x35 = Color(0xff53515a),
            x40 = Color(0xff605d66),
            x50 = Color(0xff79767f),
            x60 = Color(0xff938f99),
            x70 = Color(0xffada9b4),
            x80 = Color(0xffc9c5d0),
            x90 = Color(0xffe5e0ec),
            x95 = Color(0xfff4effa),
            x98 = Color(0xfffdf8ff),
            x99 = Color(0xfffffbff),
            x100 = Color(0xffffffff)
        )
    }
}
