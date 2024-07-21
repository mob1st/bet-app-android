package br.com.mob1st.features.finances.impl.infra.data.repositories.suggestions

import br.com.mob1st.core.androidx.assets.AssetsGetter
import br.com.mob1st.core.androidx.resources.StringGetter
import br.com.mob1st.core.androidx.resources.StringIdGetter
import br.com.mob1st.core.kotlinx.coroutines.IoCoroutineDispatcher
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
 * @property io The IO dispatcher.
 */
internal class CategorySuggestionsRepositoryImpl(
    private val io: IoCoroutineDispatcher,
    private val stringGetter: StringGetter,
    private val stringIdGetter: StringIdGetter,
    private val assetsGetter: AssetsGetter,
) : CategorySuggestionRepository {
    override fun getByStep(
        step: BuilderNextAction.Step,
    ): Flow<List<CategorySuggestion>> = flow {
        val list = SuggestionListPerStep[step]
        val suggestions = list.mapNotNull(::map)
        emit(suggestions)
    }.flowOn(io)

    private fun map(suggestion: String): CategorySuggestion? {
        val resId = stringIdGetter[suggestion]
        val imageFile = assetsGetter["icons/$suggestion.svg"]
        return if (resId != null && imageFile.exists()) {
            CategorySuggestion(
                name = stringGetter[resId],
                image = Uri(imageFile.absolutePath),
            )
        } else {
            Timber.w("Could not map suggestion $suggestion")
            null
        }
    }
}
