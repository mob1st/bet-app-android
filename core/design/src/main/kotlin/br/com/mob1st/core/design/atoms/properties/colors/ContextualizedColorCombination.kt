package br.com.mob1st.core.design.atoms.properties.colors

import androidx.compose.runtime.Composable
import br.com.mob1st.core.design.atoms.colors.material.LocalExtensionsColorFamilies
import br.com.mob1st.core.design.atoms.colors.material.LocalMaterialColorFamilies
import br.com.mob1st.core.design.atoms.colors.material.families.ColorCombination
import br.com.mob1st.core.design.atoms.colors.material.families.ColorFamily

/**
 * Provides a [ColorCombination] based on a context.
 */
interface ContextualizedColorCombination {
    /**
     * Resolves the [ColorFamily].
     * @return The [ColorFamily].
     */
    @Composable
    fun resolve(): ColorCombination
}

/**
 * Provides the color family for the fixed expenses category type.
 */
data object FixedContainerCombination : ContextualizedColorCombination {
    @Composable
    override fun resolve(): ColorCombination {
        return LocalExtensionsColorFamilies.current.fixedExpenses.containerCombination
    }
}

/**
 * Provides the color family for the incomes category type.
 */
data object IncomesContainerCombination : ContextualizedColorCombination {
    @Composable
    override fun resolve(): ColorCombination {
        return LocalExtensionsColorFamilies.current.incomes.containerCombination
    }
}

/**
 * Provides the color family for the seasonal expenses category type.
 */
data object SeasonalContainerCombination : ContextualizedColorCombination {
    @Composable
    override fun resolve(): ColorCombination {
        return LocalExtensionsColorFamilies.current.seasonalExpenses.containerCombination
    }
}

/**
 * Provides the color family for the variable expenses category type.
 */
data object VariableContainerCombination : ContextualizedColorCombination {
    @Composable
    override fun resolve(): ColorCombination {
        return LocalExtensionsColorFamilies.current.variableExpenses.containerCombination
    }
}

data object BackgroundContainerCombination : ContextualizedColorCombination {
    @Composable
    override fun resolve(): ColorCombination {
        return LocalMaterialColorFamilies.current.backgroundFamily.containerCombination
    }
}
