package br.com.mob1st.bet.features.groups.domain

import br.com.mob1st.bet.core.analytics.AnalyticsTool
import br.com.mob1st.bet.core.logs.Debuggable
import br.com.mob1st.bet.features.competitions.domain.CompetitionRepository
import br.com.mob1st.bet.features.groups.domain.CreateGroupUseCase.Companion.MEMBERSHIP_LIMIT
import br.com.mob1st.bet.features.profile.domain.Anonymous
import br.com.mob1st.bet.features.profile.domain.UserRepository
import org.koin.core.annotation.Factory

/**
 * Executes the business logic related to create a group for betting
 */
@Factory
class CreateGroupUseCase(
    private val userRepository: UserRepository,
    private val groupRepository: GroupRepository,
    private val competitionRepository: CompetitionRepository,
    private val analyticsTool: AnalyticsTool,
) {

    /**
     * @param groupName the name of the group
     */
    suspend operator fun invoke(
        groupName: String,
    ): GroupEntry {
        val user = userRepository.get()

        if (user.authType == Anonymous) {
            throw NotAuthorizedForItException()
        }

        if (user.membershipCount >= MEMBERSHIP_LIMIT) {
            throw MembershipLimitException(user.membershipCount)
        }
        val competitionEntry = competitionRepository.getDefaultCompetition().toEntry()
        val groupEntry = groupRepository.create(
            founder = user,
            name = groupName,
            competitionEntry = competitionEntry
        )

        analyticsTool.log(
            CreateGroupEvent(
                groupEntry = groupEntry,
                competitionEntry = competitionEntry
            )
        )
        return groupEntry
    }

    companion object {
        const val MEMBERSHIP_LIMIT = 10
    }

}

class MembershipLimitException(
    private val currentCount: Int
) : Exception("a user can't have more then $MEMBERSHIP_LIMIT memberships. Your current memberships is $currentCount"), Debuggable {
    override fun logProperties(): Map<String, Any> {
        return mapOf(
            "currentMemberships" to currentCount,
            "maxAllowed" to MEMBERSHIP_LIMIT
        )
    }
}

class NotAuthorizedForItException : Exception("The user have to be logged in with some provider to be able to create and join groups")