package lesson07.Homework07

import org.hamcrest.Description
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.anyOf
import org.hamcrest.TypeSafeDiagnosingMatcher

enum class Color {
    RED,
    GREEN,
    YELLOW,
    BLUE,
}

data class Shape(
    val sideLength: Float,
    val numberOfSides: Int,
    val color: Color
)

// проверка длины сторон в диапазоне
class SideLengthMatcher(
    private val min: Float,
    private val max: Float
) : TypeSafeDiagnosingMatcher<Shape>() {

    override fun describeTo(description: Description) {
        description.appendText("side length between $min and $max")
    }

    override fun matchesSafely(
        item: Shape,
        mismatchDescription: Description
    ): Boolean {
        val side = item.sideLength

        if (side < min || side > max) {
            mismatchDescription.appendText(
                "side length was $side, expected between $min and $max"
            )
            return false
        }
        return true
    }
}

// проверка корректного количества сторон (>= 1)
class NumberOfSidesMatcher : TypeSafeDiagnosingMatcher<Shape>() {

    override fun describeTo(description: Description) {
        description.appendText("shape with number of sides >= 1")
    }

    override fun matchesSafely(
        item: Shape,
        mismatchDescription: Description
    ): Boolean {
        val sides = item.numberOfSides

        if (sides < 1) {
            mismatchDescription.appendText(
                "number of sides was $sides, expected >= 1"
            )
            return false
        }
        return true
    }
}

// четное количество сторон
class EvenNumberOfSidesMatcher : TypeSafeDiagnosingMatcher<Shape>() {

    override fun describeTo(description: Description) {
        description.appendText("shape with even number of sides")
    }

    override fun matchesSafely(
        item: Shape,
        mismatchDescription: Description
    ): Boolean {
        val sides = item.numberOfSides

        if (sides % 2 != 0) {
            mismatchDescription.appendText(
                "number of sides was $sides, expected even number"
            )
            return false
        }
        return true
    }

    // проверка отрицательной длины
class NegativeSideLengthMatcher : TypeSafeDiagnosingMatcher<Shape>() {

    override fun describeTo(description: Description) {
    description.appendText("shape with negative side length")
    }

    override fun matchesSafely(
    item: Shape,
    mismatchDescription: Description
    ): Boolean {
 if (item.sideLength >= 0) {
    mismatchDescription.appendText(
    "side length was ${item.sideLength}, expected negative value"
    )
    return false
    }
    return true
    }
}
    // проверка отрицательного количества сторон
    class NegativeSidesMatcher : TypeSafeDiagnosingMatcher<Shape>() {

        override fun describeTo(description: Description) {
            description.appendText("shape with negative number of sides")
        }

        override fun matchesSafely(
            item: Shape,
            mismatchDescription: Description
        ): Boolean {
            if (item.numberOfSides >= 0) {
                mismatchDescription.appendText(
                    "number of sides was ${item.numberOfSides}, expected negative value"
                )
                return false
            }
            return true
        }
    }

    //проверка цвета
    class ColorMatcher(
        private val expectedColor: Color
    ) : TypeSafeDiagnosingMatcher<Shape>() {

        override fun describeTo(description: Description) {
            description.appendText("shape with color $expectedColor")
        }

        override fun matchesSafely(
            item: Shape,
            mismatchDescription: Description
        ): Boolean {
            if (item.color != expectedColor) {
                mismatchDescription.appendText(
                    "color was ${item.color}, expected $expectedColor"
                )
                return false
            }
            return true
        }
    }

}

fun main() {

    val shape1 = Shape(10f, 3, Color.RED)
    val shape2 = Shape(5f, 4, Color.BLUE)
    val shape3 = Shape(7f, 2, Color.GREEN)
    val shape4 = Shape(-3f, 5, Color.YELLOW)

    val shapes = listOf(shape1, shape2, shape3, shape4)

    // матчеры
    val validLength = SideLengthMatcher(0.1f, 100f)
    val validSides = NumberOfSidesMatcher()
    val isEven = EvenNumberOfSidesMatcher()
    val isBlue = EvenNumberOfSidesMatcher.ColorMatcher(Color.BLUE)

    val validShapeMatcher = allOf(validLength, validSides)

    val validShapes = shapes.filter { validShapeMatcher.matches(it) }
    println("Valid shapes: $validShapes")

    val blueOrEvenMatcher = anyOf(isBlue, isEven)

    val filtered = shapes.filter { blueOrEvenMatcher.matches(it) }
    println("Blue OR even shapes: $filtered")

    assertThat(shape2, validShapeMatcher)
}
