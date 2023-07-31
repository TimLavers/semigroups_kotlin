package org.grandtestauto.maths.monoid.test

import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.shouldBe
import org.grandtestauto.maths.monoid.*
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * @author Tim Lavers
 */
class IntSetTest : TestBase() {

    @Test
    fun transformTest() {
        assertEquals(s(1, 3, 5), s(2, 4, 6) transform t(1, 1, 3, 3, 5, 5))
        assertEquals(s(1, 3), s(2, 4, 6) transform t(1, 1, 3, 3))
        assertEquals(s(1, 2, 3), s(1, 2, 3) transform t(2, 1, 3))
        assertEquals(s(1, 7), s(1, 2, 4) transform t(7, 1, 3, 7, 5, 5, 5))
    }

    @Test
    fun intsFrom1ToTest() {
        val ints0 = intsFrom1To(0)
        assert(ints0.isEmpty())

        val ints1 = intsFrom1To(1)
        assert(ints1.size == 1)
        assert(ints1.contains(1))

        val ints2 = intsFrom1To(2)
        assert(ints2.size == 2)
        assert(ints2.contains(1))
        assert(ints2.contains(2))
    }

    @Test
    fun unionCompositionTest() {
        val semigroup2 = Semigroup(intsets( s(1, 2), s(1,4), s(1, 2, 4)), UnionComposition)
        assertEquals(3, semigroup2.size)
    }

    @Test
    fun intersectionCompositionTest() {
        val semigroup2 = Semigroup(intsets( s(1), s(1, 2), s(1,4), s(1, 2, 4)), IntersectionComposition)
        assertEquals(4, semigroup2.size)
    }

    @Test
    fun allSubsetsTest() {
        with (intsFrom1To(3).allSubsets()) {
            size shouldBe 8
            this shouldContain setOf(1, 2, 3)
            this shouldContain setOf(1, 2)
            this shouldContain setOf(1, 3)
            this shouldContain setOf(2, 3)
            this shouldContain setOf(3)
            this shouldContain setOf(2)
            this shouldContain setOf(1)
            this shouldContain setOf()
        }
        intsFrom1To(6).allSubsets().size shouldBe 64
        intsFrom1To(8).allSubsets().size shouldBe 256
    }
}