package br.com.mob1st.bet.features.groups.presentation.createGroup

import br.com.mob1st.bet.core.ui.state.AsyncState
import br.com.mob1st.bet.core.ui.state.SimpleMessage
import br.com.mob1st.bet.core.ui.state.StateViewModel
import br.com.mob1st.bet.features.profile.domain.UserAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.koin.android.annotation.KoinViewModel

data class GroupData(
    val name: String = ""
)

sealed class GroupsUIEvent {
    data class CreateGroup(val groupName: String) : GroupsUIEvent()
    data class TryAgain(val message: SimpleMessage) : GroupsUIEvent()
}

@KoinViewModel
class CreateGroupViewModel(
    private val userAuth: UserAuth
) : StateViewModel<GroupData, GroupsUIEvent>(
    AsyncState(data = GroupData(), loading = true)
) {
    private val firestore = Firebase.firestore

    init {
        setAsync {
            it.data(data = it.data.copy())
        }
    }

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
        val mockMembers = arrayListOf("Paulo Baier", "Vini Jr", "Neymar")

        val groupDetails = hashMapOf(
            "name" to groupName,
            "members" to mockMembers
        )

        firestore.collection("groups").document(userAuth.getId().orEmpty())
                 .collection("Grupos").document(groupName).set(groupDetails)
    }
}