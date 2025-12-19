package utils

import fr.amanin.aoc2025.utils.Grid
import fr.amanin.aoc2025.utils.textGrid
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

private typealias Frame = Grid<Char>

//private enum class AnsiCode(val code: String) {
//    SAVE_CURSOR_POSITION("\u001b[s"),
//    RESTORE_LAST_SAVED_POSITION("\u001b[u");
//}

//private val ANIMATION = newSingleThreadContext("AsciiAnimator")

class AsciiAnimator {

    private val frames: MutableList<Frame> = mutableListOf()

//    suspend fun play(out : PrintStream = System.out, framerate: UInt = 2u, repeat: UInt = 0u) = withContext(ANIMATION) {
//        require(framerate in 1u..120u) { "Framerate must be between 1 and 120 frames per second" }
//        val frameDelayMilli = (1f/framerate.toFloat()*1e3).toLong()
//        ANIMATION.cancelChildren(CancellationException("START NEW ANIMATION"))
//        launch {
//            print(AnsiCode.SAVE_CURSOR_POSITION.code)
//            for (n in 0u..repeat) {
//                for (frame in frames) {
//                    val startTime = System.nanoTime()
//                    println(frame)
//                    print(AnsiCode.RESTORE_LAST_SAVED_POSITION.code)
//                    val drawingFrameTook = System.nanoTime() - startTime
//                    delay(frameDelayMilli - drawingFrameTook)
//                }
//            }
//        }
//    }

    fun exportSvg(framerate: UInt = 1u, debug: Boolean = true) : String {
        require(framerate in 1u..120u) { "Framerate must be between 1 and 120 frames per second" }
        val frameDelayMilli = (1f/framerate.toFloat()*1e3).toLong()

        val svgWidth = frames.maxOf { frame -> frame.width } * 0.5
        val svgHeight = frames.maxOf { frame -> frame.height }
        val cssAnim = createCssAnim(frames.size, frameDelayMilli.milliseconds)
        var header =
            """
            <?xml version="1.0" encoding="UTF-8" standalone="no"?>
            <!DOCTYPE svg PUBLIC "-//W3C//DTD SVG 1.1//EN" "http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd">
            <svg xmlns="http://www.w3.org/2000/svg" width="${svgWidth}em" height="${svgHeight}em">
            """.trimIndent()
        val style =
            """
            <style type="text/css">
            text {
                font-family: monospace;
                visibility: hidden;
            }
            $cssAnim
            </style>
            """.trimIndent()
        header += "\n$style\n"
        if (debug) header += "<!-- DEBUG --><rect width=\"100%\" height=\"100%\" fill=\"none\" stroke=\"red\" stroke-width=\"0.5em\"/>\n"
        val footer = "\n</svg>"
        return frames
            .mapIndexed { i, frame ->
                val tspans = (0..<frame.height).map { rowIdx ->
                    val line = frame.copy(0, rowIdx, frame.width, 1).toString()
                    """<tspan x="0" dy="1em">$line</tspan>"""
                }
                tspans.joinToString("\n", """<text id="frame$i">"""+"\n", "\n</text>")
            }
            .joinToString("\n", prefix=header, postfix=footer)
    }

    fun add(frame: Grid<Char>) = frames.add(frame)
    fun addAll(vararg frames: String) = this.frames.addAll(frames.map { textGrid(it.split("\n")) })
    fun addAll(vararg frames: Grid<Char>) = this.frames.addAll(frames)
    fun addAll(frames: List<String>) = this.frames.addAll(frames.map { textGrid(it.split("\n")) })
}

private fun createCssAnim(frameCount: Int, timePerFrame: Duration) : String {
    val totalTime = timePerFrame * frameCount
    val transitionPercent = 100f / frameCount.toFloat()
    val keyframes =
    """
    @keyframes displayframe {
        0% { visibility: visible; }
        $transitionPercent% { visibility: hidden; }
    }
    """.trimIndent()
    return (0..<frameCount).asSequence()
        .map { i -> "#frame$i { animation: displayframe ${totalTime.inWholeMilliseconds}ms linear infinite ${(timePerFrame*i).inWholeMilliseconds}ms; }" }
        .joinToString("\n", keyframes + "\n", "\n")
}
