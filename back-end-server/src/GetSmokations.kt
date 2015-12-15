import java.awt.List
import java.io.*
import java.net.*
import java.util.ArrayList


object GetSmokations {

    internal var smokers = ArrayList<Smokation>()

    @Throws(ClassNotFoundException::class)
    fun main(args: Array<String>) {
        try {
            val url = URL("http://45.55.156.205:8000")
            val connection = url.openConnection() as HttpURLConnection
            connection.setRequestProperty("RequestType", "getSmokations")
            connection.connect()
            val input = ObjectInputStream(connection.getInputStream())
            var smoke: Smokation
            val counter = input.readInt()
            for (i in 0..counter - 1) {
                smoke = Smokation(input.readDouble(), input.readDouble())
                smokers.add(smoke)
            }
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        // prints out smoker locations
        System.out.println(smokers.toString())

    }

    @Throws(IOException::class, ClassNotFoundException::class)
    fun deserialize(string: ByteArray): Object {
        val b = ByteArrayInputStream(string)
        val o = ObjectInputStream(b)
        return o.readObject()
    }
}
