package br.com.mob1st.bet.features.groups.domain

import br.com.mob1st.bet.features.profile.domain.UserRepository
import org.koin.core.annotation.Factory

@Factory
class GetGroupsUseCase(
    private val repository: GroupRepository,
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(): List<GroupEntry> {
        return repository.getGroups(userRepository.get())
    }

}