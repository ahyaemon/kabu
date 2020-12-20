package com.ahyaemon.kabu.extensions

import arrow.core.Either

fun <A> exceptionToLeft(fn: () -> A): Either<Throwable, A> {
    return try {
        Either.right(fn())
    } catch (e: Exception) {
        Either.left(e)
    }
}
