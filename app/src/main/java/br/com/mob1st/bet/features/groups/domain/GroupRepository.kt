package br.com.mob1st.bet.features.groups.domain

import br.com.mob1st.bet.core.logs.Debuggable
import br.com.mob1st.bet.features.competitions.domain.CompetitionEntry
import br.com.mob1st.bet.features.profile.domain.User

/**
 * Manages CRUD operation on [Group] entity and everything related to it
 */
interface GroupRepository {

    /**
     * Creates a new group for the given user
     * @param founder the id of the logged user responsible to create the group. it will be the
     * first member of the group
     * @param name the name of the group
     * @param competitionEntry the competition entry values to relate the group that will be created
     */
    suspend fun create(
        founder: User,
        name: String,
        competitionEntry: CompetitionEntry
    ) : GroupEntry

    suspend fun getGroups(founder: User) : List<GroupEntry>
}

class CreateGroupException(
    private val groupEntry: GroupEntry,
    private val competitionEntry: CompetitionEntry,
    cause: Throwable
) : Exception("unable to create the group", cause), Debuggable {
    override fun logProperties(): Map<String, Any> {
        return (groupEntry to competitionEntry).toLogMap()
    }
}

class GetGroupsListException(
    cause: Throwable
) : Exception("Unable to get groups from user", cause), Debuggable {
    override fun logProperties(): Map<String, Any?> {
        return mapOf()
    }
}