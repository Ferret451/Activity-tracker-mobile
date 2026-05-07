package com.bsuir.activity_tracker_mobile.ui.common

import com.bsuir.activity_tracker_mobile.data.network.ApiException

object UiError {
    fun toUserMessage(prefix: String, throwable: Throwable?): String {
        if (throwable == null) return prefix

        val api = throwable as? ApiException
        val details = buildList<String> {
            val problem = api?.problem
            if (!problem?.detail.isNullOrBlank()) add(problem!!.detail!!)
            if (!problem?.title.isNullOrBlank() && problem.title != problem.detail) add(problem.title!!)
            problem?.errors?.values
                ?.filter { it.isNotBlank() }
                ?.forEach { add(it) }
        }
            .distinct()

        if (details.isEmpty()) return prefix
        return buildString {
            append(prefix)
            append("\n")
            details.forEach { line ->
                append("- ")
                append(line)
                append("\n")
            }
        }.trimEnd()
    }
}

