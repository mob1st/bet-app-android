package br.com.mob1st.bet.features.groups.presentation.createGroup

import br.com.mob1st.bet.R
import br.com.mob1st.bet.core.ui.state.SimpleMessage
import br.com.mob1st.bet.core.ui.state.StateViewModel
import br.com.mob1st.bet.features.groups.domain.CreateGroupUseCase
import br.com.mob1st.bet.features.groups.domain.GroupEntry
import br.com.mob1st.bet.features.groups.domain.MembershipLimitException
import br.com.mob1st.bet.features.groups.domain.NotAuthorizedForItException
import org.koin.android.annotation.KoinViewModel

data class CreateGroupData(
    val name: String = "",
    val createdGroup: GroupEntry? = null
)

sealed class CreateGroupUIEvent {
    data class CreateGroup(val groupName: String) : CreateGroupUIEvent()
    data class TryAgain(val message: SimpleMessage) : CreateGroupUIEvent()
}


@KoinViewModel
class CreateGroupViewModel(
    private val createGroupUseCase: CreateGroupUseCase
) : StateViewModel<CreateGroupData, CreateGroupUIEvent>(CreateGroupData(), loading = false) {

    override fun fromUi(uiEvent: CreateGroupUIEvent) {
        when (uiEvent) {
            is CreateGroupUIEvent.CreateGroup -> createGroup(uiEvent.groupName)
            is CreateGroupUIEvent.TryAgain -> tryAgain(uiEvent.message)
        }
    }

    private fun tryAgain(message: SimpleMessage) {
        messageShown(message, loading = true)
    }

    private fun createGroup(groupName: String) {
        loading()

        setAsync {
            try {
                it.data(data = it.data.copy(createdGroup = createGroupUseCase(groupName)))
            } catch (e: NotAuthorizedForItException) {
                it.failure(SimpleMessage(R.string.group_create_message_error_auth))
            } catch (e: MembershipLimitException) {
                it.failure(SimpleMessage(R.string.group_create_message_error_limit))
            }
        }
    }
}