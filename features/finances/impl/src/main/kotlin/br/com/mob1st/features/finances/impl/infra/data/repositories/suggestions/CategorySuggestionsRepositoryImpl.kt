package br.com.mob1st.features.finances.impl.infra.data.repositories.suggestions

import br.com.mob1st.core.androidx.assets.AssetsGetter
import br.com.mob1st.core.androidx.resources.StringIdGetter
import br.com.mob1st.core.kotlinx.coroutines.DefaultCoroutineDispatcher
import br.com.mob1st.core.kotlinx.structures.Uri
import br.com.mob1st.features.finances.impl.domain.entities.BuilderNextAction
import br.com.mob1st.features.finances.impl.domain.entities.CategorySuggestion
import br.com.mob1st.features.finances.impl.domain.repositories.CategorySuggestionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber

/**
 * Concrete implementation of the [CategorySuggestionRepository] interface.
 * @param default The default coroutine dispatcher.
 * @param stringIdGetter Provide the suggestions names based on its string identifier.
 * @param assetsGetter Provide the file links for the suggestions images. It doesn't need to create/load the file on
 * disk.
 * @param suggestionListPerStep Does the map of step to suggestions identifiers list
 */
internal class CategorySuggestionsRepositoryImpl(
    private val default: DefaultCoroutineDispatcher,
    private val stringIdGetter: StringIdGetter,
    private val assetsGetter: AssetsGetter,
    private val suggestionListPerStep: SuggestionListPerStep,
) : CategorySuggestionRepository {
    override fun getByStep(
        step: BuilderNextAction.Step,
    ): Flow<List<CategorySuggestion>> = flow {
        val list = suggestionListPerStep[step]
        val suggestions = list.mapNotNull {
            map(it)
        }
        emit(suggestions)
    }.flowOn(default)

    private suspend fun map(suggestion: String): CategorySuggestion? {
        val name = stringIdGetter.getString(suggestion)
        val imagePath = assetsGetter.get("icons/$suggestion.svg")
        return if (name != null && imagePath != null) {
            CategorySuggestion(
                name = name,
                image = Uri(imagePath),
            )
        } else {
            Timber.w("Could not map suggestion $suggestion")
            null
        }
    }
}
