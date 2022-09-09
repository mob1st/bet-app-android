package br.com.mob1st.bet.features.competitions.domain

class GetDefaultCompetitionException(
    cause: Throwable
) : Exception("unable to get the default competition", cause)