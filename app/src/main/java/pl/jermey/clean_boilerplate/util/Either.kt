package pl.jermey.clean_boilerplate.util

import io.reactivex.Observable
import io.reactivex.Single

sealed class Either<out A> {

  companion object {
    fun <A> of(value: A): Either<A> = Right(value)
    fun <A> of(throwable: Throwable): Either<A> = Left(throwable)
  }

  abstract fun <R> map(mapper: (A) -> R): Either<R>

  open class Left<A>(val value: Throwable) : Either<A>() {
    override fun <R> map(mapper: (A) -> R) = Left<R>(value)
  }

  open class Right<A>(val value: A) : Either<A>() {
    override fun <R> map(mapper: (A) -> R) = Right(mapper(value))
  }
}

fun <T> Observable<T>.toEither(): Observable<Either<T>> = map { Either.of(it) }
  .onErrorReturn { Either.of(it) }

fun <T> Single<T>.toEither(): Single<Either<T>> = map { Either.of(it) }
  .onErrorReturn { Either.of(it) }
