package br.com.mob1st.features.finances.impl.infra.data.ram

import br.com.mob1st.core.kotlinx.structures.Money
import br.com.mob1st.core.kotlinx.structures.Uuid
import br.com.mob1st.core.kotlinx.structures.generateStringId
import br.com.mob1st.features.finances.impl.fakes.FakeRecurrenceLocalizationProvider
import br.com.mob1st.features.finances.publicapi.domain.entities.BudgetItem
import br.com.mob1st.features.finances.publicapi.domain.entities.Recurrence
import br.com.mob1st.features.finances.publicapi.domain.entities.RecurrentCategory
import br.com.mob1st.tests.featuresutils.RandomIdGenerator
import io.mockk.every
import kotlinx.datetime.Month
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream
import kotlin.test.assertEquals

@ExtendWith(RandomIdGenerator::class)
internal class RecurrencesSuggestionFactoryTest {
    @ParameterizedTest
    @ArgumentsSource(Companion::class)
    fun `GIVEN a localization provider WHEN create THEN the right list should be is used`(
        factory: RecurrenceSuggestionFactory,
        expected: List<RecurrentCategory>,
    ) {
        every { generateStringId() } returns "a"
        val actual = factory(FakeRecurrenceLocalizationProvider())

        assertEquals(
            expected = expected,
            actual = actual,
        )
    }

    companion object : ArgumentsProvider {
        private val expectedFixedExpensesList =
            listOf(
                RecurrentCategorySuggestion.RENT,
                RecurrentCategorySuggestion.MORTGAGE,
                RecurrentCategorySuggestion.ENERGY,
                RecurrentCategorySuggestion.INTERNET,
                RecurrentCategorySuggestion.PHONE,
                RecurrentCategorySuggestion.TV_STREAMING,
                RecurrentCategorySuggestion.MUSIC_STREAMING,
                RecurrentCategorySuggestion.INSURANCES,
                RecurrentCategorySuggestion.EDUCATION,
                RecurrentCategorySuggestion.GYM,
                RecurrentCategorySuggestion.TRANSPORT,
            ).map {
                RecurrentCategory(
                    id = Uuid("a"),
                    description = it.name,
                    amount = Money.Zero,
                    type = BudgetItem.Type.EXPENSE,
                    recurrence = Recurrence.Fixed(),
                )
            }

        private val expectedVariableExpensesList =
            listOf(
                RecurrentCategorySuggestion.LUNCH,
                RecurrentCategorySuggestion.WEEKEND_RESTAURANT,
                RecurrentCategorySuggestion.FOOD_DELIVERY,
                RecurrentCategorySuggestion.BARS,
                RecurrentCategorySuggestion.ENTERTAINMENT,
                RecurrentCategorySuggestion.BEAUTY,
            ).map {
                RecurrentCategory(
                    id = Uuid("a"),
                    description = it.name,
                    amount = Money.Zero,
                    type = BudgetItem.Type.EXPENSE,
                    recurrence = Recurrence.Variable,
                )
            }

        private val expectedSeasonalExpensesList =
            listOf(
                RecurrentCategorySuggestion.HOLIDAYS,
                RecurrentCategorySuggestion.CHRISTMAS,
                RecurrentCategorySuggestion.GIFTS,
                RecurrentCategorySuggestion.CLOTHES,
                RecurrentCategorySuggestion.ELECTRONICS,
                RecurrentCategorySuggestion.FURNITURE,
            ).map {
                RecurrentCategory(
                    id = Uuid("a"),
                    description = it.name,
                    amount = Money.Zero,
                    type = BudgetItem.Type.EXPENSE,
                    recurrence =
                        Recurrence.Seasonal(
                            month = Month.JANUARY,
                            day = 1,
                        ),
                )
            }

        private val incomesList = emptyList<RecurrentCategory>()

        override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
            return Stream.of(
                Arguments.of(
                    FixedExpanseFactory,
                    expectedFixedExpensesList,
                ),
                Arguments.of(
                    VariableExpanseFactory,
                    expectedVariableExpensesList,
                ),
                Arguments.of(
                    SeasonalExpanseFactory,
                    expectedSeasonalExpensesList,
                ),
                Arguments.of(
                    IncomeFactory,
                    incomesList,
                ),
            )
        }
    }
}
