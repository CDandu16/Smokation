import java.io.*
import java.net.InetSocketAddress
import java.util.ArrayList

import com.sun.net.httpserver.*

//mongo

import java.text.DateFormat
import java.text.SimpleDateFormat

//import org.bson.Document;

//import com.mongodb.Block;
//import com.mongodb.client.FindIterable;

//import static com.mongodb.client.model.Filters.*;
//import static com.mongodb.client.model.Sorts.ascending;
import java.util.Arrays.asList


//import com.mongodb.MongoClient;
//import com.mongodb.client.MongoDatabase;

/**
 * @author Cole Hudson
 */

class Server// mongo

//public MongoClient mongoClient;
//public MongoDatabase db;

internal constructor() : HttpHandler {

    var smokations = ArrayList<Smokation>()

    init {
        smokations = ArrayList<Smokation>()

        //mongoClient = new MongoClient();
        //db = mongoClient.getDatabase("test");

    }

    /*public void mongoTest() {
		db.getCollection("smokations").insertOne(new Document().append("latitude", 666).append("longitude", 666));
		FindIterable<Document> iterable = db.getCollection("smokations").find();
		iterable.forEach(new Block<Document>() {
			public void apply(final Document document) {
				System.out.println(document);
			}
		});
	}*/

    fun printSmokations() {
        System.out.println("Smokations!!!")
        for (smokation in smokations) {
            System.out.println("Lat : " + smokation.getLatitude() + ", Long: " + smokation.getLongitude())
        }
        System.out.println()
    }

    @Throws(IOException::class)
    fun handle(exchange: HttpExchange) {
        // request info
        val requestHeaders = exchange.getRequestHeaders()

        val getSmokations = requestHeaders.get("RequestType").get(0).equals("getSmokations")
        val addSmokation = requestHeaders.get("RequestType").get(0).equals("addSmokation")

        if (getSmokations) {
            try {
                exchange.sendResponseHeaders(200, 0)
                handleGetSmokationsRequest(exchange)
            } catch (e: ClassNotFoundException) {
                // TODO Auto-generated catch block
                e.printStackTrace()
            }

        }

        if (addSmokation) {
            exchange.sendResponseHeaders(200, 0)
            handleAddRequest(exchange, requestHeaders)
        }
    }

    // handle a request to get all points
    @Throws(IOException::class, ClassNotFoundException::class)
    fun handleGetSmokationsRequest(exchange: HttpExchange) {
        System.out.println("Get Smokations")

        val response = "Here's all of my Smokations\n"
        // serialize smokations and sends it to client
        val o = ObjectOutputStream(exchange.getResponseBody())
        // sends size of arraylist being sent
        o.writeInt(smokations.size())
        // starts "new line"
        o.reset()
        for (i in 0..smokations.size() - 1) {
            // write arraylist object by object
            o.writeDouble(smokations.get(i).getLatitude())
            o.reset()
            o.writeDouble(smokations.get(i).getLongitude())
            o.reset()
        }
        o.close()
        printSmokations()
    }

    // a request to add a point
    @Throws(IOException::class)
    fun handleAddRequest(exchange: HttpExchange, requestHeaders: Headers) {
        val latitudes = requestHeaders.get("Latitude")
        val longitudes = requestHeaders.get("Longitude")

        // add all of the latitudes and longitudes to the smokations array
        if (latitudes.size() === longitudes.size()) {
            for (i in 0..latitudes.size() - 1) {
                val latitude = Double.parseDouble(latitudes.get(i))
                val longitude = Double.parseDouble(longitudes.get(i))
                val smokation = Smokation(latitude, longitude)

                smokations.add(smokation)
            }
        }
        //mongoTest();

        val response = "Thank you for that new Smokation\n"
        val o = ObjectOutputStream(exchange.getResponseBody())
        // sends size of arraylist being sent
        o.writeUTF(response)
        o.close()
    }

    companion object {

        @Throws(Exception::class)
        fun main(args: Array<String>) {
            val server = HttpServer.create(InetSocketAddress(8000), 0)

            server.createContext("/", Server())
            server.setExecutor(null) // creates a default executor
            server.start()

            System.out.println("Smokations Server Running")
        }

        @Throws(IOException::class, ClassNotFoundException::class)
        fun deserialize(bytes: ByteArray): Object {
            val b = ByteArrayInputStream(bytes)
            val o = ObjectInputStream(b)
            return o.readObject()
        }
    }
}