package com.sportygroup.weatherapp.core.common

/**
 * Resources repository: resolves Android string resources for non-UI code (domain/data and
 * presentation mappers) so they don't depend on a `Context` directly. Composables should use
 * `stringResource(...)` instead; this is for everything that cannot.
 */
interface StringResources {
    fun getString(resId: Int): String
    fun getString(resId: Int, vararg formatArgs: Any): String
}