data class AocDay(val year: Int, val day: Int)

class AocInput {
    companion object Factory {
        fun fetch(year: Int, day: Int): ByteArray {
            return ByteArray(32);
        }
    }

}