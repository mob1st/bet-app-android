package design.atoms

import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Color
import br.com.mob1st.core.design.atoms.colors.ColorSchemeFactory
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.headers
import io.kotest.data.row
import io.kotest.data.table
import io.kotest.matchers.shouldBe

class SchemeFactoryTest : BehaviorSpec({
    Given("a device theme setup where true is dark and false is light") {
        When("create") {
            Then("result should be the expected") {
                forAll(deviceSetupToExpected) { (isDark, expected) ->
                    val actual = ColorSchemeFactory.create(isDark)
                    actual.toString() shouldBe expected.toString()
                }
            }
        }
    }
}) {
    private companion object Expected {
        val lightTheme =
            ColorScheme(
                primary = Color(0xFF5d4ac5),
                onPrimary = Color(0xFFffffff),
                primaryContainer = Color(0xFFe5deff),
                onPrimaryContainer = Color(0xFF1a0064),
                secondary = Color(0xFF5455a9),
                onSecondary = Color(0xFFffffff),
                secondaryContainer = Color(0xFFe2dfff),
                onSecondaryContainer = Color(0xFF0d0664),
                tertiary = Color(0xFF835400),
                onTertiary = Color(0xFFffffff),
                tertiaryContainer = Color(0xFFffddb5),
                onTertiaryContainer = Color(0xFF2a1800),
                error = Color(0xFF9c404a),
                onError = Color(0xFFffffff),
                errorContainer = Color(0xFFffdadb),
                onErrorContainer = Color(0xFF40000d),
                background = Color(0xFFfefbff),
                onBackground = Color(0xFF001849),
                surface = Color(0xFFfefbff),
                onSurface = Color(0xFF001849),
                outline = Color(0xFF79767f),
                surfaceVariant = Color(0xFFe5e0ec),
                onSurfaceVariant = Color(0xFF48454f),
                surfaceTint = Color(0xFF5D4AC5),
                outlineVariant = Color(0xFFC9C5D0),
                inversePrimary = Color(0xFFC8BFFF),
                inverseSurface = Color(0xFF002B75),
                inverseOnSurface = Color(0xFFEEF0FF),
                scrim = Color(0xFF000000),
            )

        val darkTheme =
            ColorScheme(
                primary = Color(0xFFc8bfff),
                onPrimary = Color(0xFF2e0996),
                primaryContainer = Color(0xFF452eac),
                onPrimaryContainer = Color(0xFFe5deff),
                secondary = Color(0xFFc1c1ff),
                onSecondary = Color(0xFF252477),
                secondaryContainer = Color(0xFF3c3c8f),
                onSecondaryContainer = Color(0xFFe2dfff),
                tertiary = Color(0xFFffb957),
                onTertiary = Color(0xFF462b00),
                tertiaryContainer = Color(0xFF643f00),
                onTertiaryContainer = Color(0xFFffddb5),
                error = Color(0xFFffb2b6),
                onError = Color(0xFF5f121f),
                errorContainer = Color(0xFF7d2934),
                onErrorContainer = Color(0xFFffdadb),
                background = Color(0xFF001849),
                onBackground = Color(0xFFdbe1ff),
                surface = Color(0xFF001849),
                onSurface = Color(0xFFdbe1ff),
                outline = Color(0xFF938f99),
                surfaceVariant = Color(0xFF48454f),
                onSurfaceVariant = Color(0xFFc9c5d0),
                surfaceTint = Color(0xFFC8BFFF),
                outlineVariant = Color(0xFF48454F),
                inversePrimary = Color(0xFF5D4AC5),
                inverseSurface = Color(0xFFDBE1FF),
                inverseOnSurface = Color(0xFF001849),
                scrim = Color(0xFF000000),
            )

        val deviceSetupToExpected =
            table(
                headers("what is a header"),
                row(false to lightTheme),
                row(true to darkTheme),
            )
    }
}
