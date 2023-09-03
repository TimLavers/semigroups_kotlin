package org.grandtestauto.maths.monoid.test

import org.grandtestauto.maths.monoid.*
import org.junit.Assert
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * @author Tim Lavers
 */
class C5S4DoubleProduct {
    private val c5 = cyclicGroup(5)
    // A generator of C5
    private val gamma = Transformation(intArrayOf(2,3,4,5,1))
    // We want S4 embedded in S5.
    private val s4 = Semigroup(symmetricGroup(4).map {it.embed(5)}.toSet(), TransformationComposition)
    // S4 acts on C5 as follows:
    private val actionOfSymOnCyc: ((Transformation) -> ((Transformation) -> (Transformation))) =
        { beta -> { sigma -> c5.powerOf(gamma, beta(sigma(5)))}}
    // C5 acts on S4 as follows:
    private val actionOfCycOnSym: ((Transformation) -> ((Transformation) -> (Transformation))) =
        { theta -> { beta -> theta * beta * (c5.powerOf(gamma, 5 - beta(theta(5))))}}
    private val product : Semigroup<Pair<Transformation, Transformation>> =
        doubleProduct(s4, c5, actionOfSymOnCyc, actionOfCycOnSym)

    @Test
    fun decompositionTest() {
        //This is a warm-up to check that for all beta in S4, for all theta in C5,
        //theta beta = theta beta gamma^(n - n theta beta) gamma^(n theta beta)
        //This decomposition is the basis of the double product decomposition.
        for (beta in s4) {
            for (theta in c5) {
                val left = theta * beta
                val n_theta_beta = beta(theta(5))
                val gamma_inv = c5.powerOf(gamma,5 - n_theta_beta)
                val gamma_pow = c5.powerOf(gamma, n_theta_beta)
                val right = theta * (beta * gamma_inv * gamma_pow)
                Assert.assertEquals(left, right)
            }
        }
    }

    @Test
    fun elementsTest() {
        Assert.assertEquals(24 * 5, product.size)
        for (s in s4) {
            for (c in c5) {
                val tuple = Pair(s, c)
                assertTrue(product.contains(tuple))
            }
        }
    }

    @Test
    fun compositionTest() {
        for (m in product) {
            for (n in product) {
                val prod_eval = (m.left() * m.right()) * (n.left() * n.right())
                val tuple = product.composition(m , n)
                val tuple_eval = tuple.left() * tuple.right()
                Assert.assertEquals(prod_eval, tuple_eval)
            }
        }
    }
}