package io.github.excu101.filesystem.fs.attr.time

class Instant private constructor(
    val seconds: Long,
    val nanos: Int
) {
    companion object {

        fun of(seconds: Long, nanos: Long): Instant {
            val secs = Math.addExact(seconds, Math.floorDiv(nanos, 1000_000_000L))
            val nos = Math.floorMod(nanos, 1000_000_000L)
            return create(secs, nos.toInt())
        }

        private fun create(seconds: Long, nanos: Int): Instant {
            return Instant(seconds, nanos)
        }
    }


}