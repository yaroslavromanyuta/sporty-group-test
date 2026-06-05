package com.sportygroup.weatherapp.core.common

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 * Resources repository: resolves Android string resources for non-UI code (domain/data and
 * presentation mappers) so they don't depend on a `Context` directly. Composables should use
 * `stringResource(...)` instead; this is for everything that cannot.
 */
interface StringResources {
    fun getString(resId: Int): String
    fun getString(resId: Int, vararg formatArgs: Any): String
}

class AndroidStringResources @Inject constructor(
    @ApplicationContext private val context: Context,
) : StringResources {
    override fun getString(resId: Int): String = context.getString(resId)
    override fun getString(resId: Int, vararg formatArgs: Any): String =
        context.getString(resId, *formatArgs)
}