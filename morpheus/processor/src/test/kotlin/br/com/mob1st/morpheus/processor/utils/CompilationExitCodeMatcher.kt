package br.com.mob1st.morpheus.processor.utils

import com.tschuchort.compiletesting.KotlinCompilation
import io.kotest.matchers.Matcher
import io.kotest.matchers.MatcherResult
import io.kotest.matchers.should

fun exitWith(result: KotlinCompilation.ExitCode) = Matcher<KotlinCompilation.ExitCode> { value ->
    MatcherResult(
        passed = value == result,
        { "Expected exit code $result but was $value" },
        { "Expected exit code $result but was $value" }
    )
}

fun KotlinCompilation.Result.shouldExitOk() =
    exitCode should exitWith(KotlinCompilation.ExitCode.OK)

fun KotlinCompilation.Result.shouldExitWithCompilationError() =
    exitCode should exitWith(KotlinCompilation.ExitCode.COMPILATION_ERROR)
