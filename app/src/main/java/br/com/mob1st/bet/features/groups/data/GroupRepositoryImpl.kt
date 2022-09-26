package br.com.mob1st.bet.features.groups.data

/*
Tu pode ver que nos imports não tem nada referenciando o firebase, porque o GroupCollection isolou
ele do projeto
 */
import br.com.mob1st.bet.core.coroutines.DispatcherProvider
import br.com.mob1st.bet.core.utils.functions.suspendRunCatching
import br.com.mob1st.bet.features.competitions.domain.CompetitionEntry
import br.com.mob1st.bet.features.groups.domain.CreateGroupException
import br.com.mob1st.bet.features.groups.domain.Group
import br.com.mob1st.bet.features.groups.domain.GroupEntry
import br.com.mob1st.bet.features.groups.domain.GroupRepository
import br.com.mob1st.bet.features.profile.domain.User
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Factory

@Factory
class GroupRepositoryImpl(
    private val groupCollection: GroupCollection,
    private val dispatcherProvider: DispatcherProvider
) : GroupRepository {

    private val io get() = dispatcherProvider.io

    override suspend fun create(
        founder: User,
        name: String,
        competitionEntry: CompetitionEntry
    ): GroupEntry = withContext(io) {

        val group = Group(
            name = name,
            competition = competitionEntry,
            description = "",
            // the founder is the first member in the group
            memberCount = 1
        )
        val entry = group.toEntry()
        suspendRunCatching {
            groupCollection.create(founder, group).let { entry }
        }.getOrElse {
            throw CreateGroupException(
                groupEntry = entry,
                competitionEntry = competitionEntry,
                cause = it,
            )
        }
    }
}