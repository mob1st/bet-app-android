package br.com.mob1st.bet.features.groups.domain

import br.com.mob1st.bet.core.localization.default
import br.com.mob1st.bet.features.competitions.domain.CompetitionEntry
import java.util.Date

/**
 * A group of friends betting in a competition to see who have the best guesses
 */
data class Group(
    val id: String = "",
    val name: String,
    val description: String,
    val competition: CompetitionEntry,
    val createdAt: Date = Date(),
    val updatedAt: Date = createdAt,
    val imageUrl: String? = null,
    val memberCount: Int = 0,
) {
    fun toEntry() = GroupEntry(id, name, imageUrl)
}

/**
 * A small piece of the [Group] entity to used as reference in other entities
 */
data class GroupEntry(
    val id: String,
    val name: String,
    val imageUrl: String? = null,
)

fun Pair<GroupEntry, CompetitionEntry>.toLogMap() =
    mapOf(
        "groupId" to first.id,
        "groupName" to first.name,
        "competitionId" to second.id,
        "competitionName" to second.name.default,
        "competitionType" to second.type.name.lowercase(),
    )
