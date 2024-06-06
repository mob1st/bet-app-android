package br.com.mob1st.features.finances.impl.ui.tabs

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import br.com.mob1st.core.design.atoms.theme.BetTheme
import br.com.mob1st.core.design.organisms.helper.Helper
import br.com.mob1st.core.design.utils.ThemedPreview
import br.com.mob1st.features.finances.impl.R
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition

@Composable
internal fun CashFlowEmptyVariant(onClickSetupBudget: () -> Unit) {
    val helperComposition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.empty_operations))

    Helper(
        modifier = Modifier.padding(24.dp),
        imageContent = {
            LottieAnimation(
                composition = helperComposition,
                iterations = LottieConstants.IterateForever,
            )
        },
        titleContent = {
            Text(stringResource(R.string.finances_operations_empty_helper_title))
        },
        descriptionContent = {
            Text(stringResource(R.string.finances_operations_empty_helper_description))
        },
        buttonContent = {
            Button(onClick = onClickSetupBudget) {
                Text(stringResource(R.string.finances_operations_empty_helper_button))
            }
        },
    )
}

@Composable
@ThemedPreview
private fun TransactionListTabEmptyVariantPreview() {
    BetTheme {
        CashFlowEmptyVariant(onClickSetupBudget = {})
    }
}
