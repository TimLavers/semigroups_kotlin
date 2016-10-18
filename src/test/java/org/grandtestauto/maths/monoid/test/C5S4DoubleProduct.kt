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
    //The generator of c5
    private val gamma = Transformation(intArrayOf(2,3,4,5,1))
    private val s4: Semigroup<Transformation>

    private val actionOfSymOnCyc: ((Transformation) -> ((Transformation) -> (Transformation)))
    private val actionOfCycOnSym: ((Transformation) -> ((Transformation) -> (Transformation)))
    private val product : Semigroup<Tuple<Transformation, Transformation>>

    init {
        //We want S4 embedded in S5.
        val elements = mutableSetOf<Transformation>()
        symmetricGroup(4).forEach{s -> elements.add(s.embed(5))}
        s4 = Semigroup(elements, TransformationComposition)

        //s4 acts on c5 as follows:
        actionOfSymOnCyc = { beta -> { sigma -> c5.powerOf(gamma, beta.apply(sigma.apply(5)))}}

        //c5 acts on s4 as follows:
        actionOfCycOnSym = { theta -> { beta -> theta * beta * (c5.powerOf(gamma, 5 - beta.apply(theta.apply(5))))}}

        product = doubleProduct(s4, c5, actionOfSymOnCyc, actionOfCycOnSym)
    }

    @Test
    fun decompositionTest() {
        //This is a warm-up to check that for all beta in S4, for all theta in C5,
        //theta beta = theta beta gamma^(n - n theta beta) gamma^(n theta beta)
        //This decomposition is the basis of the double product decomposition.
        for (beta in s4) {
            for (theta in c5) {
                val left = theta * beta
                val n_theta_beta = beta.apply(theta.apply(5))
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
                val tuple = Tuple(s, c)
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