package org.grandtestauto.maths.monoid

import java.util.HashSet

/**
 * A generalised generation algorithm

 * @author Tim Lavers
 */
internal class Generation<T>(private val generators: Set<T>, private val creator: Generation.CreateNew<T>) {
    interface CreateNew<T> {
        open fun createNew(t: T): T? {
            return null
        }

        fun createNew(s: T, t: T): T?
    }

    fun generate(): Set<T> {
        var generated: Set<T> = HashSet(generators)
        val nextGeneration = HashSet<T>()
        var newElementFound: Boolean
        do {
            newElementFound = false
            for (g in generated) {
                nextGeneration.add(g)
                val fromSelf = creator.createNew(g)
                if (fromSelf != null) {
                    newElementFound = newElementFound or nextGeneration.add(fromSelf)
                }
                for (h in generated) {
                    val product = creator.createNew(g, h)
                    if (product != null) {
                        newElementFound = newElementFound or nextGeneration.add(product)
                    }
                }
            }
            generated = HashSet(nextGeneration)
        } while (newElementFound)
        return generated
    }
}