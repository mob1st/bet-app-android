package br.com.mob1st.bet.features.groups

import br.com.mob1st.bet.core.ui.state.FetchedData
import br.com.mob1st.bet.core.ui.state.SimpleMessage
import br.com.mob1st.bet.core.ui.state.StateViewModel
import br.com.mob1st.bet.features.groups.domain.GroupEntry
import br.com.mob1st.bet.features.groups.domain.GroupRepository
import br.com.mob1st.bet.features.profile.domain.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.koin.android.annotation.KoinViewModel

data class GroupsListData(
    val groupList: List<GroupEntry> = emptyList()
) : FetchedData {
    override fun hasData(): Boolean = groupList.isNotEmpty()
}

sealed class GroupsUIEvent {
    data class TryAgain(val message: SimpleMessage) : GroupsUIEvent()
}

@KoinViewModel
class GroupTabViewModel(
    private val repository: GroupRepository,
    private val userRepository: UserRepository
) : StateViewModel<GroupsListData, GroupsUIEvent>(GroupsListData(), loading = false) {
    override fun fromUi(uiEvent: GroupsUIEvent) {
        when (uiEvent) {
            is GroupsUIEvent.TryAgain -> tryAgain(uiEvent.message)
        }
    }

    private val _groups = MutableStateFlow<List<GroupEntry>>(emptyList())
    val groups: StateFlow<List<GroupEntry>> = _groups

    init {
        getGroups()
    }

    private fun tryAgain(message: SimpleMessage) {
        messageShown(message, loading = true)
        getGroups()
    }

    fun getGroups() {
        setAsync {
            val listGroups = repository.getGroups(userRepository.get())
            logger.d("fetch ${listGroups.size} groups")
            _groups.value = listGroups
            it.data(data = it.data.copy(groupList = listGroups))
        }
    }
}


