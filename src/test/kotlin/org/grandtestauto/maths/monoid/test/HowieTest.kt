package org.grandtestauto.maths.monoid.test

import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.grandtestauto.maths.monoid.*
import org.junit.Test

/**
 * @author Tim Lavers
 */
class HowieTest : TestBase() {

    @Test
    fun `theorem 1 1 3`() {
        // Define s as the direct product of a left and right zero semigroup.
        val r5 = rightZeroSemigroup(5)
        val l6 = leftZeroSemigroup(6)
        val unit: ((String) -> ((String) -> String)) = { _ -> { s -> s } }
        val s = doubleProduct(l6, r5, unit, unit)

        // Then s is a rectangular band
        s.forEach {  a ->
            s.forEach { b ->
                s.composition(s.composition(a, b), a) shouldBe a
            }
        }

        // For all elements idempotent and abc = ac for all a, b, c
        s.forEach {  a ->
            s.composition(a, a) shouldBe a
            s.forEach { b ->
                s.forEach { c ->
                    s.composition(s.composition(a, b), c) shouldBe s.composition(a, c)
                }
            }
        }
    }

    @Test
    fun `proposition 1 2 3`() {
        val t6 = transformationMonoid(6)
        t6.forEach { t ->
            val allPowers = allPowers(t)
            val idempotent = allPowers.find { it * it == it }
            idempotent shouldNotBe null
        }
    }

    private fun allPowers(t: Transformation): Set<Transformation> {
        val result = mutableSetOf<Transformation>()
        var currentPower = t
        while(!result.contains(currentPower)) {
            result.add(currentPower)
            currentPower = t * currentPower
        }
        return result
    }
}