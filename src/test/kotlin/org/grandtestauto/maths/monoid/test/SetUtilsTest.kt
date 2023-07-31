package org.grandtestauto.maths.monoid.test

import io.kotest.matchers.shouldBe
import org.grandtestauto.maths.monoid.intsFrom1To
import org.grandtestauto.maths.monoid.isASubsetOf
import org.grandtestauto.maths.monoid.powerSet
import org.grandtestauto.maths.monoid.subsetsSatisfying
import org.junit.Test

/**
 * @author Tim Lavers
 */
class SetUtilsTest : TestBase() {

    @Test
    fun subsetsSatisfyingTest() {
        with(intsFrom1To(1).subsetsSatisfying{ it.isNotEmpty() }) {
            size shouldBe 1
            contains(s(1)) shouldBe true
        }

        with(intsFrom1To(2).subsetsSatisfying{ it.isNotEmpty() }) {
            size shouldBe 3
            contains(s(1, 2)) shouldBe true
            contains(s(1)) shouldBe true
            contains(s(2)) shouldBe true
        }

        with(intsFrom1To(3).subsetsSatisfying{ it.size < 3 }) {
            size shouldBe 7
            contains(s(1, 2)) shouldBe true
            contains(s(1, 3)) shouldBe true
            contains(s(2, 3)) shouldBe true
            contains(s(1)) shouldBe true
            contains(s(2)) shouldBe true
            contains(s(3)) shouldBe true
            contains(s()) shouldBe true
        }
        with(intsFrom1To(4).subsetsSatisfying{ it.size < 3 }) {
            size shouldBe 11
            contains(s(1, 2)) shouldBe true
            contains(s(1, 3)) shouldBe true
            contains(s(1, 4)) shouldBe true
            contains(s(2, 3)) shouldBe true
            contains(s(2, 4)) shouldBe true
            contains(s(3, 4)) shouldBe true
            contains(s(1)) shouldBe true
            contains(s(2)) shouldBe true
            contains(s(3)) shouldBe true
            contains(s(4)) shouldBe true
            contains(s()) shouldBe true
        }
        with(intsFrom1To(4).subsetsSatisfying{ it.contains(3) }) {
            size shouldBe 8
            contains(s(1, 2, 3, 4)) shouldBe true
            contains(s(1, 2, 3)) shouldBe true
            contains(s(2, 3, 4)) shouldBe true
            contains(s(1, 3, 4)) shouldBe true
            contains(s(1, 3)) shouldBe true
            contains(s(2, 3)) shouldBe true
            contains(s(3, 4)) shouldBe true
            contains(s(3)) shouldBe true
        }
        with(intsFrom1To(5).subsetsSatisfying{ it.contains(1) || it.contains(2) }) {
            size shouldBe 24
            contains(s(1, 2, 3, 4, 5)) shouldBe true
            contains(s(1, 2, 3, 4)) shouldBe true
            contains(s(1, 2, 3, 5)) shouldBe true
            contains(s(1, 2, 4, 5)) shouldBe true
            contains(s(1, 3, 4, 5)) shouldBe true
            contains(s(2, 3, 4, 5)) shouldBe true
            contains(s(1, 2, 3)) shouldBe true
            contains(s(1, 2, 4)) shouldBe true
            contains(s(1, 2, 5)) shouldBe true
            contains(s(1, 3, 4)) shouldBe true
            contains(s(1, 3, 5)) shouldBe true
            contains(s(1, 4, 5)) shouldBe true
            contains(s(2, 3, 4)) shouldBe true
            contains(s(2, 3, 5)) shouldBe true
            contains(s(2, 4, 5)) shouldBe true
            contains(s(1, 2)) shouldBe true
            contains(s(1, 3)) shouldBe true
            contains(s(1, 4)) shouldBe true
            contains(s(1, 5)) shouldBe true
            contains(s(2, 3)) shouldBe true
            contains(s(2, 4)) shouldBe true
            contains(s(2, 5)) shouldBe true
            contains(s(1)) shouldBe true
            contains(s(2)) shouldBe true
        }
        with(intsFrom1To(5).subsetsSatisfying{ it.contains(1) && it.size > 2  }) {
            size shouldBe 11
            contains(s(1, 2, 3, 4, 5)) shouldBe true
            contains(s(1, 2, 3, 4)) shouldBe true
            contains(s(1, 2, 3, 5)) shouldBe true
            contains(s(1, 2, 4, 5)) shouldBe true
            contains(s(1, 3, 4, 5)) shouldBe true
            contains(s(1, 2, 3)) shouldBe true
            contains(s(1, 2, 4)) shouldBe true
            contains(s(1, 2, 5)) shouldBe true
            contains(s(1, 3, 4)) shouldBe true
            contains(s(1, 3, 5)) shouldBe true
            contains(s(1, 4, 5)) shouldBe true
        }
    }

    @Test
    fun powerSetTest() {
        with(intsFrom1To(1).powerSet()) {
            size shouldBe 2
            contains(s(1)) shouldBe true
            contains(s()) shouldBe true
        }

        with(intsFrom1To(2).powerSet()) {
            size shouldBe 4
            contains(s(1, 2)) shouldBe true
            contains(s(1)) shouldBe true
            contains(s(2)) shouldBe true
            contains(s()) shouldBe true
        }

        with(intsFrom1To(3).powerSet()) {
            size shouldBe 8
            contains(s(1, 2, 3)) shouldBe true
            contains(s(1, 3)) shouldBe true
            contains(s(2, 3)) shouldBe true
            contains(s(3)) shouldBe true
            contains(s(1, 2)) shouldBe true
            contains(s(1)) shouldBe true
            contains(s(2)) shouldBe true
            contains(s()) shouldBe true
        }

        with(intsFrom1To(4).powerSet()) {
            size shouldBe 16
            contains(s(1, 2, 3, 4)) shouldBe true
            contains(s(1, 3, 4)) shouldBe true
            contains(s(2, 3, 4)) shouldBe true
            contains(s(3, 4)) shouldBe true
            contains(s(1, 2, 4)) shouldBe true
            contains(s(1, 4)) shouldBe true
            contains(s(2, 4)) shouldBe true
            contains(s(4)) shouldBe true
            contains(s(1, 2, 3)) shouldBe true
            contains(s(1, 3)) shouldBe true
            contains(s(2, 3)) shouldBe true
            contains(s(3)) shouldBe true
            contains(s(1, 2)) shouldBe true
            contains(s(1)) shouldBe true
            contains(s(2)) shouldBe true
            contains(s()) shouldBe true
        }
    }

    @Test
    fun isASubsetOfTest() {
        s() isASubsetOf s() shouldBe true
        s() isASubsetOf s(1) shouldBe true
        s(1) isASubsetOf s() shouldBe false
        s(1, 3, 5, 7) isASubsetOf(s(1, 2, 3, 4, 5, 6, 7, 8)) shouldBe true
    }
}