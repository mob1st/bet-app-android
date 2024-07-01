package br.com.mob1st.features.finances.impl.data.repositories.suggestions

import br.com.mob1st.core.kotlinx.coroutines.IoCoroutineDispatcher
import br.com.mob1st.features.finances.impl.TwoCentsDb
import br.com.mob1st.features.finances.impl.domain.entities.BuilderNextAction
import br.com.mob1st.features.finances.impl.domain.entities.CategorySuggestion
import br.com.mob1st.features.finances.impl.domain.entities.FixedExpensesStep
import br.com.mob1st.features.finances.impl.domain.entities.FixedIncomesStep
import br.com.mob1st.features.finances.impl.domain.entities.VariableExpensesStep
import br.com.mob1st.features.finances.impl.utils.testTwoCentsDb
import io.mockk.every
import io.mockk.mockk
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
        mapper = SelectSuggestionsMapper(
            mockk {
                every { map(any(), any()) } returns emptyList()
            },
        )
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
        expectedSuggestions: Set<CategorySuggestion.Name>,
    ) = runTest(io) {
        val suggestions = repository.getByStep(step).first()
        assertEquals(
            expectedSuggestions,
            suggestions.map { it.name }.toSet(),
        )
    }

    object StepProvider : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext): Stream<out Arguments>? {
            return Stream.of(
                Arguments.of(FixedExpensesStep, fixedExpensesSuggestions),
                Arguments.of(VariableExpensesStep, variableExpensesSuggestions),
                Arguments.of(FixedIncomesStep, fixedIncomeSuggestions),
            )
        }
    }

    companion object {
        private val fixedExpensesSuggestions = setOf(
            CategorySuggestion.Name.RentOrMortgage,
            CategorySuggestion.Name.PropertyTaxes,
            CategorySuggestion.Name.HealthInsurance,
            CategorySuggestion.Name.CarInsurance,
            CategorySuggestion.Name.PublicTransport,
            CategorySuggestion.Name.HomeInsurance,
            CategorySuggestion.Name.LoanPayments,
            CategorySuggestion.Name.InternetSubscription,
            CategorySuggestion.Name.CellPhonePlan,
            CategorySuggestion.Name.CableOrStreamingServices,
            CategorySuggestion.Name.MusicStreamingServices,
            CategorySuggestion.Name.MagazineOrNewspaperSubscriptions,
            CategorySuggestion.Name.Gym,
            CategorySuggestion.Name.AssociationFees,
            CategorySuggestion.Name.PrivateRetirementPlans,
            CategorySuggestion.Name.PersonalEducation,
            CategorySuggestion.Name.ChildrenSchool,
            CategorySuggestion.Name.Childcare,
        )

        private val variableExpensesSuggestions = setOf(
            CategorySuggestion.Name.Groceries,
            CategorySuggestion.Name.DiningOut,
            CategorySuggestion.Name.FoodDelivery,
            CategorySuggestion.Name.WeekdayLunch,
            CategorySuggestion.Name.CoffeeSnacks,
            CategorySuggestion.Name.TransportationFuel,
            CategorySuggestion.Name.PublicTransportTickets,
            CategorySuggestion.Name.Cinema,
            CategorySuggestion.Name.Concerts,
            CategorySuggestion.Name.ElectronicGames,
            CategorySuggestion.Name.SportsTickets,
            CategorySuggestion.Name.Bars,
            CategorySuggestion.Name.NightClubs,
            CategorySuggestion.Name.HouseholdSupplies,
            CategorySuggestion.Name.FitnessRecreation,
        )

        private val fixedIncomeSuggestions = setOf(
            CategorySuggestion.Name.Salary,
            CategorySuggestion.Name.Pension,
            CategorySuggestion.Name.RentalIncome,
        )
    }
}
