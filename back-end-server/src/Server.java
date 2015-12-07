
import java.io.*;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

import com.sun.net.httpserver.*;

//mongo

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.bson.Document;

import com.mongodb.Block;
import com.mongodb.client.FindIterable;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Sorts.ascending;
import static java.util.Arrays.asList;
import com.example.SmokationModel.*;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

/**
 * @author Cole Hudson
 */

public class Server implements HttpHandler {
	
	public ArrayList<Smokation> smokations = new ArrayList<Smokation>(); 

	// mongo

	//public MongoClient mongoClient;
	//public MongoDatabase db;

	Server() {
		smokations = new ArrayList<Smokation>();

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

	public void printSmokations() {
		System.out.println("Smokations!!!");
		for (Smokation smokation : smokations) {
			System.out.println("Lat : " + smokation.getLatitude() + ", Long: " + smokation.getLongitude());
		}
		System.out.println();
	}

	public static void main(String[] args) throws Exception {
		HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);

		server.createContext("/", new Server());
		server.setExecutor(null); // creates a default executor
		server.start();

		System.out.println("Smokations Server Running");
	}

	public void handle(HttpExchange exchange) throws IOException {
		// request info
		Headers requestHeaders = exchange.getRequestHeaders();

		boolean getSmokations = requestHeaders.get("RequestType").get(0).equals("getSmokations");
		boolean addSmokation = requestHeaders.get("RequestType").get(0).equals("addSmokation");

		if (getSmokations) {
			try {
				exchange.sendResponseHeaders(200, 0);
				handleGetSmokationsRequest(exchange);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (addSmokation) {
			exchange.sendResponseHeaders(200, 0);
			handleAddRequest(exchange, requestHeaders);
		}
	}

	// handle a request to get all points
	public void handleGetSmokationsRequest(HttpExchange exchange) throws IOException, ClassNotFoundException {
		System.out.println("Get Smokations");

		String response = "Here's all of my Smokations\n";
		// serialize smokations and sends it to client
		ObjectOutputStream o = new ObjectOutputStream(exchange.getResponseBody());
		// sends size of arraylist being sent
		o.writeInt(smokations.size());
		// starts "new line"
		o.reset();
		for (int i = 0; i < smokations.size(); i++) {
			// write arraylist object by object
			o.writeDouble(smokations.get(i).getLatitude());
			o.reset();
			o.writeDouble(smokations.get(i).getLongitude());
			o.reset();
		}
		o.close();
		printSmokations();
	}

	public static Object deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
		ByteArrayInputStream b = new ByteArrayInputStream(bytes);
		ObjectInputStream o = new ObjectInputStream(b);
		return o.readObject();
	}

	// a request to add a point
	public void handleAddRequest(HttpExchange exchange, Headers requestHeaders) throws IOException {
		List<String> latitudes = requestHeaders.get("Latitude");
		List<String> longitudes = requestHeaders.get("Longitude");

		// add all of the latitudes and longitudes to the smokations array
		if (latitudes.size() == longitudes.size()) {
			for (int i = 0; i < latitudes.size(); i++) {
				Double latitude = Double.parseDouble(latitudes.get(i));
				Double longitude = Double.parseDouble(longitudes.get(i));
				Smokation smokation = new Smokation(latitude, longitude);

				smokations.add(smokation);
			}
		}
		//mongoTest();

		String response = "Thank you for that new Smokation\n";
		ObjectOutputStream o = new ObjectOutputStream(exchange.getResponseBody());
		// sends size of arraylist being sent
		o.writeUTF(response);
		o.close();
	}
}