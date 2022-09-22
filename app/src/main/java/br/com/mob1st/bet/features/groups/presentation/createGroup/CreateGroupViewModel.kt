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
        /*
        aqui tu confundiu o que eu te expliquei no zapzap.

        firestore.collection("groups").document(userAuth.getId().orEmpty())
                 .collection("Grupos").document(groupName).set(groupDetails)

        O firebase cobra por documento acessado, entao o ideal é tentar fazer consumir o mínimo de
        documentos possíveis, por isso podemos mudar um pouco a estrutura de collections que tu fez.

        Imagina que vamos ter uma tela que lista os grupos do usuário logado, e quando o usuário
        clica em um grupo nós vemos a lista de membros (outros usuários desse grupo).

        Vamos por parte, começando pela tela que lista os grupos cujo o usuário logado é membro.
        Aqui, a forma mais fácil de fazer isso é tendo uma collection dentro da collection
        'users', numa estrutura mais ou menos assim:

        users/<<ID_DO_USUARIO>>/memberships/<<ID_DA_INSCRÇÃO_DO_GRUPO>>

        toda vez que o usuário criar um grupo ou entrar em um grupo criado por outra pessoa,
        criamos um documento nessa collection member ship

        Esse documento vai ter apenas infos relevates pra tela que LISTA os grupos.
        Vamos imaginar que só queremos exibir:
        - nome do grupo
        - foto do grupo

        Não mostramos todos os membros dele, o ranking dos membros dele, qual a competição que ele
        está vinculado, descrição e muitas outras coisas que podemos ter.
        Essas infos são acessadas somente na tela de detalhe do grupo, e ela pode ser acessada por
        todos os membros dele, então nós precisamos criar uma outra collection pra listar todos os
        grupos criados, na raiz do firebase. No final fica mais ou menos assim:

        groups/<<ID_DO_GRUPO>>/members/<<ID_DO_MEMBRO>>
        users/<<ID_DO_USUARIO>>/memberships/

        dentro de groups/<<ID_DO_GRUPO>> a gente as infos completas de um grupo (alem das basicas, tb descricao, competicao vinculada, etc...)
        dentro de groups/<<ID_DO_GRUPO>>/members/<<ID_DO_MEMBRO>> a gente tem algumas infos dos
        membros, como nome, pontos e foto e uma referencia pro documento respectivo ao user.

         */

        // como vamos criar mais de um documento, o melhor é rodar tudo dentro de batched write
        // mais infos aqui: https://firebase.google.com/docs/firestore/manage-data/transactions
        val batch = firestore.batch()

        // a tela de detalhe do grupo deve buscar as infos dessa coleção
        val groupRef = firestore
            .collection("groups")
            // se vc nao passar nada por parametro, o firestore gera um id automatico, que é o que queremos
            // assim vc nao precisa se preocupar com "nomes repetidos"
            .document()
        batch.set(
            groupRef,
            mapOf(
                "name" to groupName,
                "image" to "www.porntube.com/kidbengala.jpg",
                "membersCount" to 1, // começa com 1 pq o usuário que criou é membro,
                "description" to "nem sei se precisa",
                //"competition" to firestore.collection("competitions").document("id da competicao")
            )
        )

        // vamos criar o primeiro membro do grupo com o usuário logado
        val memberRef = groupRef.collection("members").document()
        batch.set(
            memberRef,
            mapOf(
                // cuidado com esse !!, mas por hora vamos fazer pra simplificar
                "ref" to firestore.collection("users").document(userAuth.getId()!!),
                "name" to userAuth.get()!!.name,
                "image" to userAuth.get()!!.imageUrl
            )
        )

        // agora nós criamos o documento dentro da coleção do usuário logado
        // assim saberemos todos os grupos que o usuário é membro.
        // a tela que lista os grupos deve buscar os grupos dessa coleção
        val membershipRef = firestore.document(userAuth.getId()!!)
            .collection("membership")
            .document()
        batch.set(
            membershipRef,
            mapOf(
                // repete as infos básicas do grupo
                "name" to groupName,
                "url" to "www.porntube.com/kidbengala.jpg",
                "ref" to groupRef
            )
        )

        // esse comando insere tudo ao mesmo tempo. se um falhar tudo falha
        // isso é bom pq dai garantimos uma operação atomica
        batch.commit()

    }
}