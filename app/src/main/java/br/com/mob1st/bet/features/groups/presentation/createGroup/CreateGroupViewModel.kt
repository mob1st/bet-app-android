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

    init {
        /*
        ja que essa tela só cria grupos, acho que aqui tu nao precisa fazer nada no init
        esse método é útil no init quando tu precisa fazer requests ou coisas do tipo
        setAsync {
            // tu vai precisar de um
            it.data(data = it.data.copy())
        }
         */

    }

    override fun fromUi(uiEvent: GroupsUIEvent) {
        when (uiEvent) {
            is GroupsUIEvent.CreateGroup -> createGroup(uiEvent.groupName)
            is GroupsUIEvent.TryAgain -> tryAgain(uiEvent.message)
        }
    }

    private fun tryAgain(message: SimpleMessage) {
        // aqui to removeu e colocou um loading, isso quer dizer que se essa linha for executada
        // um loading infinito vai ficar rodando na tela. o que exatamente dispara esse evento?
        messageShown(message, loading = true)
    }

    private fun createGroup(groupName: String) {
        // mostra o loading na UI
        loading()

        // esse cara remove o loading e faz o tratamento dos erros.
        // depois tu pode exib
        setAsync {
            try {

                /*
                aqui tu pode ver que eu fiz o tratamento de duas exceptions que precisam de um
                tratamento "especial", porque tem uma mensagem customizada pra explicar que erro
                que deu.

                qualquer outra exception que acontever vai ser capturada pelo setAsync e vai ser
                exibido o erro padrão
                 */
                it.data(data = it.data.copy(createdGroup = createGroupUseCase(groupName)))
            } catch (e: NotAuthorizedForItException) {
                it.failure(SimpleMessage(R.string.group_create_message_error_auth))
            } catch (e: LimitExceededException) {
                it.failure(SimpleMessage(R.string.group_create_message_error_limit))
            }

        }
    }
}