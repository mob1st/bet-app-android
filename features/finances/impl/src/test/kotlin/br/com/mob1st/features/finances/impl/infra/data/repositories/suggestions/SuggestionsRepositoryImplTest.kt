package br.com.mob1st.features.finances.impl.infra.data.repositories.suggestions

import br.com.mob1st.core.kotlinx.coroutines.IoCoroutineDispatcher
import br.com.mob1st.features.finances.impl.R
import br.com.mob1st.features.finances.impl.TwoCentsDb
import br.com.mob1st.features.finances.impl.domain.entities.BuilderNextAction
import br.com.mob1st.features.finances.impl.domain.entities.FixedExpensesStep
import br.com.mob1st.features.finances.impl.domain.entities.FixedIncomesStep
import br.com.mob1st.features.finances.impl.domain.entities.VariableExpensesStep
import br.com.mob1st.features.finances.impl.infra.data.repositories.categories.SelectCategoryViewsMapper
import br.com.mob1st.features.finances.impl.infra.data.system.StringIdProvider
import br.com.mob1st.features.finances.impl.utils.testTwoCentsDb
import io.mockk.every
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class SuggestionsRepositoryImplTest {
    private lateinit var repository: SuggestionsRepositoryImpl
    private lateinit var db: TwoCentsDb
    private lateinit var mapper: SelectSuggestionsMapper
    private val io = IoCoroutineDispatcher(
        UnconfinedTestDispatcher(),
    )

    @BeforeEach
    fun setUp() {
        db = testTwoCentsDb()
        mapper = SelectSuggestionsMapper(SelectCategoryViewsMapper, FakeStringIdProvider)
        repository = SuggestionsRepositoryImpl(
            io = io,
            db = db,
            mapper = mapper,
        )
    }

    @ParameterizedTest
    @ArgumentsSource(StepProvider::class)
    fun `GIVEN a list of suggestions WHEN get by step THEN assert type and expense is used`(
        step: BuilderNextAction.Step,
        expectedSuggestions: Set<Int>,
    ) = runTest(io) {
        every { }
        val suggestions = repository.getByStep(step).first()
        assertEquals(
            expectedSuggestions,
            suggestions.map { it.nameResId }.toSet(),
        )
    }

    object StepProvider : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext): Stream<out Arguments>? {
            return Stream.of(
                Arguments.of(FixedExpensesStep, FakeStringIdProvider.fixedExpensesSuggestions),
                Arguments.of(VariableExpensesStep, FakeStringIdProvider.variableExpensesSuggestions),
                Arguments.of(FixedIncomesStep, FakeStringIdProvider.fixedIncomeSuggestions),
            )
        }
    }

    object FakeStringIdProvider : StringIdProvider {
        val fixedExpensesSuggestions = mapOf(
            "finances_builder_suggestions_item_rent_or_mortgage" to R.string.finances_builder_suggestions_item_rent_or_mortgage,
            "finances_builder_suggestions_item_property_taxes" to R.string.finances_builder_suggestions_item_property_taxes,
            "finances_builder_suggestions_item_health_insurance" to R.string.finances_builder_suggestions_item_health_insurance,
            "finances_builder_suggestions_item_car_insurance" to R.string.finances_builder_suggestions_item_car_insurance,
            "finances_builder_suggestions_item_public_transport" to R.string.finances_builder_suggestions_item_public_transport,
            "finances_builder_suggestions_item_home_insurance" to R.string.finances_builder_suggestions_item_home_insurance,
            "finances_builder_suggestions_item_loan_payments" to R.string.finances_builder_suggestions_item_loan_payments,
            "finances_builder_suggestions_item_internet_subscription" to R.string.finances_builder_suggestions_item_internet_subscription,
            "finances_builder_suggestions_item_cell_phone_plan" to R.string.finances_builder_suggestions_item_cell_phone_plan,
            "finances_builder_suggestions_item_cable_or_streaming_services" to R.string.finances_builder_suggestions_item_cable_or_streaming_services,
            "finances_builder_suggestions_item_music_streaming_services" to R.string.finances_builder_suggestions_item_music_streaming_services,
            "finances_builder_suggestions_item_magazine_or_newspaper_subscriptions" to R.string.finances_builder_suggestions_item_magazine_or_newspaper_subscriptions,
            "finances_builder_suggestions_item_gym" to R.string.finances_builder_suggestions_item_gym,
            "finances_builder_suggestions_item_association_fees" to R.string.finances_builder_suggestions_item_association_fees,
            "finances_builder_suggestions_item_private_retirement_plans" to R.string.finances_builder_suggestions_item_private_retirement_plans,
            "finances_builder_suggestions_item_personal_education" to R.string.finances_builder_suggestions_item_personal_education,
            "finances_builder_suggestions_item_children_school" to R.string.finances_builder_suggestions_item_children_school,
            "finances_builder_suggestions_item_childcare" to R.string.finances_builder_suggestions_item_childcare,
        )

        val variableExpensesSuggestions = mapOf(
            "finances_builder_suggestions_item_groceries" to R.string.finances_builder_suggestions_item_groceries,
            "finances_builder_suggestions_item_dining_out" to R.string.finances_builder_suggestions_item_dining_out,
            "finances_builder_suggestions_item_food_delivery" to R.string.finances_builder_suggestions_item_food_delivery,
            "finances_builder_suggestions_item_weekday_lunch" to R.string.finances_builder_suggestions_item_weekday_lunch,
            "finances_builder_suggestions_item_coffee_snacks" to R.string.finances_builder_suggestions_item_coffee_snacks,
            "finances_builder_suggestions_item_transportation_fuel" to R.string.finances_builder_suggestions_item_transportation_fuel,
            "finances_builder_suggestions_item_public_transport_tickets" to R.string.finances_builder_suggestions_item_public_transport_tickets,
            "finances_builder_suggestions_item_cinema" to R.string.finances_builder_suggestions_item_cinema,
            "finances_builder_suggestions_item_concerts" to R.string.finances_builder_suggestions_item_concerts,
            "finances_builder_suggestions_item_electronic_games" to R.string.finances_builder_suggestions_item_electronic_games,
            "finances_builder_suggestions_item_sports_tickets" to R.string.finances_builder_suggestions_item_sports_tickets,
            "finances_builder_suggestions_item_bars" to R.string.finances_builder_suggestions_item_bars,
            "finances_builder_suggestions_item_night_clubs" to R.string.finances_builder_suggestions_item_night_clubs,
            "finances_builder_suggestions_item_household_supplies" to R.string.finances_builder_suggestions_item_household_supplies,
            "finances_builder_suggestions_item_fitness_recreation" to R.string.finances_builder_suggestions_item_fitness_recreation,
        )

        val fixedIncomeSuggestions = mapOf(
            "finances_builder_suggestions_item_salary" to R.string.finances_builder_suggestions_item_salary,
            "finances_builder_suggestions_item_pension" to R.string.finances_builder_suggestions_item_pension,
            "finances_builder_suggestions_item_rental_income" to R.string.finances_builder_suggestions_item_rental_income,
        )

        private val all = fixedExpensesSuggestions + variableExpensesSuggestions + fixedIncomeSuggestions

        override fun get(identifier: String): Int? {
            return all[identifier]
        }
    }
}
