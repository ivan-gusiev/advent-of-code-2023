import java.io.File

data class AocDay(val year: Int, val day: Int)

class AocInput {
    companion object Factory {
        fun path(day: AocDay): String {
            return "${System.getProperty("user.home")}/aoc/${day.year}/${day.day}/input.txt";
        }

        fun bytes(day: AocDay): ByteArray {
            val path = path(day);
            return File(path).readBytes();
        }

        fun string(day: AocDay): String {

        }
    }

}