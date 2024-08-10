@file:OptIn(ExperimentalMaterial3Api::class)

package br.com.mob1st.features.finances.impl.ui.category.components.sheet

import android.os.Parcelable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import br.com.mob1st.core.androidx.navigation.jsonParcelableType
import br.com.mob1st.core.design.molecules.transitions.route
import br.com.mob1st.features.finances.impl.domain.entities.Category
import br.com.mob1st.features.finances.impl.domain.entities.GetCategoryIntent
import br.com.mob1st.features.finances.impl.ui.category.detail.CategoryDetailPage
import br.com.mob1st.features.finances.impl.ui.category.navigation.CategoryNavRoute
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryBottomSheet(
    intent: GetCategoryIntent,
    onDismiss: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val navController = rememberNavController()
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )
    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismiss,
    ) {
        Box(modifier = Modifier.fillMaxHeight(0.95f)) {
            CategoryNavHost(
                navController = navController,
                intent = intent,
                onSubmit = {
                    scope.launch {
                        sheetState.hide()
                    }
                },
            )
        }
    }
}

@OptIn(InternalSerializationApi::class)
@Composable
private fun CategoryNavHost(
    navController: NavHostController,
    intent: GetCategoryIntent,
    onSubmit: () -> Unit,
) {
    val args = when (intent) {
        is GetCategoryIntent.Create -> Args(name = intent.name)
        is GetCategoryIntent.Edit -> Args(
            id = intent.id.value,
        )
    }
    NavHost(
        navController = navController,
        startDestination = CategoryNavRoute.Detail(args = args),
    ) {
        route<CategoryNavRoute.Detail>(
            typeMap = mapOf(typeOf<Args>() to jsonParcelableType<Args>()),
        ) {
            val parArgs = it.toRoute<CategoryNavRoute.Detail>().args
            val parIntent = when {
                parArgs.name != null -> GetCategoryIntent.Create(parArgs.name)
                parArgs.id != null -> GetCategoryIntent.Edit(Category.Id(parArgs.id))
                else -> error("Invalid args")
            }
            CategoryDetailPage(
                intent = parIntent,
                onSubmit = {
                    navController.navigateUp()
                    onSubmit()
                },
            )
        }
    }
}

@Parcelize
@Serializable
data class Args(
    val id: Long? = null,
    val name: String? = null,
) : Parcelable
