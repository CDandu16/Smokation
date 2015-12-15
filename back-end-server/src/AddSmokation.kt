import java.awt.List
import java.io.*
import java.net.*
import java.util.ArrayList

object AddSmokation {
    private val serialVersionUID = 8664875232659988799L

    @Throws(ClassNotFoundException::class)
    fun main(args: Array<String>) {
        try {
            val url = URL("http://45.55.156.205:8000")
            val connection = url.openConnection() as HttpURLConnection
            connection.setRequestProperty("RequestType", "addSmokation")
            connection.addRequestProperty("Latitude", "42.575452")
            connection.addRequestProperty("Longitude", "-83.1394288")
            connection.connect()
            val input = ObjectInputStream(connection.getInputStream())
            System.out.println(input.readUTF())
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    @Throws(IOException::class, ClassNotFoundException::class)
    fun deserialize(string: ByteArray): Object {
        val b = ByteArrayInputStream(string)
        val o = ObjectInputStream(b)
        return o.readObject()
    }
}
