package cc.telepath.rhizome

import java.io.IOException
import java.io.BufferedReader
import java.io.InputStreamReader


fun debuggerDetect() {

}

fun detectVM() {
        val r = Runtime.getRuntime()
        val p = r.exec("uname")
       Thread(Runnable {
        val input = BufferedReader(InputStreamReader(p.inputStream))
        var line: String? = null

        try {
            while ((line == input.readLine()) != null)
                println(line)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }).start()
        println(p.outputStream)
}
