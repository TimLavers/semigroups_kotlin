package org.grandtestauto.maths.monoid.test

import org.grandtestauto.maths.monoid.*
import org.grandtestauto.test.tools.Waiting
import org.junit.Assert
import org.junit.Test

/**


 * @author Tim Lavers
 */
class SnPownActionsTest : TestBase() {

    private val n = 3//5: <1s, 6: 45s
    private val symn_n = symmetricGroup(n)
    private val pow_n = powerSetIntersection(n)
    private val rho: (Transformation) -> ((IntSet) -> (IntSet)) = {
        transformation -> { intSet -> intSet.transform(transformation) }
    }
    private var errorsFound = false

    @Test
    fun checkLambdaTest() {
        init()
        //a.compose(b) applied to i = b applied to a applied to i
        //ab(i) = i a b
        //for a set I, for transformations x and y, xy(i) = i x y = y(x(i)) for all i in I

        symn_n.forEach { x ->
            symn_n.forEach { y ->
                pow_n.forEach { i ->
                    val xy = x.compose(y)
                    val xy_i = rho(xy)(i)
                    val x_i = rho(x)(i)
                    val x_y_i = rho(y)(x_i)
                    if (xy_i != x_y_i) {
                        println("x = " + x)
                        println("y = " + y)
                        println("i = " + i)
                        println("xy_i = " + xy_i)
                        println("x_y_i = " + x_y_i)
                        errorsFound = true
                        println("Error found, as shown.")
                    }
                }
            }
        }
        Assert.assertFalse(errorsFound)
    }

//    fun lClassesTest(): Boolean {
//        init()
//
//        val product = DoubleProduct(pow_n, symn_n, { intSet -> { transformation -> transformation } }, rho)
//        val sdp = Semigroup(product.elements(), product.composition())
//
//        val t1 = Tuple<S, T>(i(1), t(3, 2, 1))
//        val ideal = rightIdeal(sdp, t1)
//        Waiting.pause(2000)
//        println("ideal = " + ideal)
//
//        Waiting.pause(2000)
//        /*
//        System.out.println("For n = " + n);
//        System.out.println("Number of elements in product = " + product.elements().size());
//        Stopwatch timer = new Stopwatch();
//        timer.start();
//        Assert.azzert(Semigroup.isAssociative(sdp.composition(), sdp.elements()));
//        timer.stop();
//        System.out.println("Time to check associativity: " + timer.times().get(0));
//        timer = new Stopwatch();
//        timer.start();
//        Assert.azzert(Semigroup.isClosedUnderComposition(sdp.elements(), sdp.composition()));
//        timer.stop();
//        System.out.println("Time to check commutativity: " + timer.times().get(0));
//        GreensRelations<Tuple<IntSet, Transformation>> greens = new GreensRelations<>(sdp);
//        SetPartition<Tuple<IntSet, Transformation>> lClasses = greens.lClasses();
//        Waiting.pause(2000);
//        System.out.println("Number of lClasses = " + lClasses.subsets().size());
//        lClasses.subsets().forEach(lClass -> {
//            System.out.println();
//            System.out.println("lClass with size: " + lClass.size());
//            lClass.forEach(tuple -> System.out.println(tuple.toString()));
//        });
//
//        Waiting.pause(2000);
//        SetPartition<Tuple<IntSet, Transformation>> rClasses = greens.rClasses();
//        System.out.println("NUmber of rClasses = " + rClasses.subsets().size());
//        rClasses.subsets().forEach(rClass -> {
//            System.out.println();
//            System.out.println("rClass with size: " + rClass.size());
//            rClass.forEach(tuple -> System.out.println(tuple.toString()));
//        });
//        Waiting.pause(2000);
//*/
//        return true
//    }

    private fun init() {
//        errorsFound = false
//        symn_n = symmetricGroup(n)
//        pow_n = powerSetIntersection(n)
//        rho = { transformation -> { intSet -> intSet.transform(transformation) } }
    }
}
