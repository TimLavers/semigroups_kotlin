package org.grandtestauto.maths.monoid.test

import io.kotest.matchers.shouldBe
import org.grandtestauto.maths.monoid.*
import org.junit.Test

/**
 * @author Tim Lavers
 */
class OnDecomposition {
    private val ups = monoidOfNonDecreasingTransformations(3)
    private val downs = monoidOfNonIncreasingTransformations(3)

    @Test
    fun elements() {
        val elementToDecomposition = mutableMapOf<Transformation, MutableSet<Pair<Transformation, Transformation>>>()
        ups.forEach { u ->
            downs.forEach { d ->
                val element = u.compose(d)
                elementToDecomposition.computeIfAbsent(element){mutableSetOf(Pair(u, d))}
            }
        }
        elementToDecomposition.keys shouldBe orderPreservingTransformationMonoid(3)
    }
 }