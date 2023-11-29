import java.io.File

data class AocDay(val year: Int, val day: Int)

class AocInput {
    companion object Factory {
        fun path(day: AocDay): String {
            return "${System.getProperty("user.home")}/aoc/${day.year}/${day.day}/input.txt";
        }

        fun file(day: AocDay): File {
            return File(path(day))
        }

        fun bytes(day: AocDay): ByteArray {
            return File(path(day)).readBytes();
        }

        fun string(day: AocDay): String {
            return File(path(day)).readText();
        }

        fun lines(day: AocDay): List<String> {
            return File(path(day)).readLines();
        }
    }

}