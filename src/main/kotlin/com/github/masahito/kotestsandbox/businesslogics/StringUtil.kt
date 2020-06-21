package com.github.masahito.kotestsandbox.businesslogics


typealias Adder<T> = (T)->T
object ListUtil {

    fun <T: Number> T.toAdder(): Adder<T> {
        return when(this) {
            is Long -> {{it -> (this as Long + it as Long) as T}}
            is Int -> {{it -> (this as Int + it as Int) as T}}
            is Double -> {{it -> (this as Double + it as Double) as T}}
            else -> throw AssertionError()
        }
    }


    fun <T: Number> sum(zero: T, list : List<T>): T {
        return list.map { it.toAdder() }.fold(zero) { acc, func -> func(acc)  }
    }

}
