package br.com.mob1st.processor.utils

import com.tschuchort.compiletesting.KotlinCompilation
import io.kotest.matchers.Matcher
import io.kotest.matchers.MatcherResult
import io.kotest.matchers.should

fun exitWith(result: KotlinCompilation.ExitCode) = Matcher<KotlinCompilation.ExitCode> { value ->
    MatcherResult(
        value == result,
        { "" },
        { "" }
    )
}

fun KotlinCompilation.Result.shouldExitOk() =
    exitCode should exitWith(KotlinCompilation.ExitCode.OK)

fun KotlinCompilation.Result.shouldExitWithCompilationError() =
    exitCode should exitWith(KotlinCompilation.ExitCode.COMPILATION_ERROR)
