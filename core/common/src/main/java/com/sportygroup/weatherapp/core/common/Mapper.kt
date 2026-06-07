package com.sportygroup.weatherapp.core.common

/** Generic one-directional mapper used between architectural layers. */
fun interface Mapper<in I, out O> {
    fun map(input: I): O
}

fun <I, O> Mapper<I, O>.mapList(input: List<I>): List<O> = input.map { map(it) }