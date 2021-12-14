package util

object IntUtils {
    /**
     * Allows an [Int] progression in either a positive or negative direction.
     *
     * Usage:
     *
     *      - `5 towards 10` // 5, 6, 7, 8, 9, 10
     *      - `5 towards 0` // 5, 4, 3, 2, 1, 0
     */
    infix fun Int.toward(to: Int): IntProgression {
        val step = if (this > to) -1 else 1
        return IntProgression.fromClosedRange(this, to, step)
    }
}