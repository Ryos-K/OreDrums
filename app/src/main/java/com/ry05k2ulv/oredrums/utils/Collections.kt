package com.ry05k2ulv.oredrums.utils

fun <T> Set<T>.flip(element: T): Set<T> =
    if (element in this) this - element else this + element