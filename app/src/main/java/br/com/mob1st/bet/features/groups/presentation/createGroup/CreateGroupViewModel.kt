package br.com.mob1st.bet.features.groups.presentation.createGroup

import android.os.LimitExceededException
import br.com.mob1st.bet.R
import br.com.mob1st.bet.core.ui.state.AsyncState
import br.com.mob1st.bet.core.ui.state.SimpleMessage
import br.com.mob1st.bet.core.ui.state.StateViewModel
import br.com.mob1st.bet.features.groups.domain.CreateGroupUseCase
import br.com.mob1st.bet.features.groups.domain.GroupEntry
import br.com.mob1st.bet.features.groups.domain.NotAuthorizedForItException
import br.com.mob1st.bet.features.profile.domain.UserAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.koin.android.annotation.KoinViewModel

data class GroupData(
    val name: String = "",

    // quando a UI receber esse valor NÃO NULO, tu pode fechar a tela e voltar para a lista de grupos
    val createdGroup: GroupEntry? = null
)

sealed class GroupsUIEvent {
    data class CreateGroup(val groupName: String) : GroupsUIEvent()
    data class TryAgain(val message: SimpleMessage) : GroupsUIEvent()
}


@KoinViewModel
class CreateGroupViewModel(
    private val createGroupUseCase: CreateGroupUseCase
) : StateViewModel<GroupData, GroupsUIEvent>(GroupData(), loading = false) {
    
    override fun fromUi(uiEvent: GroupsUIEvent) {
        when (uiEvent) {
            is GroupsUIEvent.CreateGroup -> createGroup(uiEvent.groupName)
            is GroupsUIEvent.TryAgain -> tryAgain(uiEvent.message)
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
            } catch (e: LimitExceededException) {
                it.failure(SimpleMessage(R.string.group_create_message_error_limit))
            }

        }
    }
}