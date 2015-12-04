import java.io.*;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

import com.sun.net.httpserver.*;

/**
 * @author Cole Hudson
 */

public class Server implements HttpHandler
{
	//points in the form (lat, long)
	public ArrayList<Double[]> smokations;
	
	public void printSmokations()
	{
		System.out.println("Smokations!!!");
		for(Double[] smokation : smokations)
		{
			System.out.println("Lat : " + smokation[0] + ", Long: " + smokation[1]);
		}
		System.out.println();
	}
	
	Server()
	{
		smokations = new ArrayList<Double[]>();
	}
	
	public static void main(String[] args) throws Exception 
	{
		HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
		
		server.createContext("/", new Server());
		server.setExecutor(null); // creates a default executor
		server.start();
	}

	public void handle(HttpExchange exchange) throws IOException 
	{
		//request info
		Headers requestHeaders = exchange.getRequestHeaders();

		boolean getSmokations = requestHeaders.get("RequestType").get(0).equals("getSmokations");
		boolean addSmokation = requestHeaders.get("RequestType").get(0).equals("addSmokation");

		if(getSmokations)
		{
			try {
				handleGetSmokationsRequest(exchange);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if(addSmokation)
		{
			handleAddRequest(exchange, requestHeaders);
		}
	}

	//handle a request to get all points
	public void handleGetSmokationsRequest(HttpExchange exchange) throws IOException, ClassNotFoundException
	{
		System.out.println("Get Smokations");
		
		String response = "Here's all of my Smokations\n";
		
		//serialize smokations
		byte[] serialized = serialize(smokations);
		
		exchange.sendResponseHeaders(200, serialized.length);
		OutputStream os = exchange.getResponseBody();
		os.write(serialized);
		os.close();
		
		printSmokations();
	}
	
	public static byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        ObjectOutputStream o = new ObjectOutputStream(b);
        o.writeObject(obj);
        return b.toByteArray();
    }
	
	public static Object deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream b = new ByteArrayInputStream(bytes);
        ObjectInputStream o = new ObjectInputStream(b);
        return o.readObject();
    }

	//a request to add a point
	public void handleAddRequest(HttpExchange exchange, Headers requestHeaders) throws IOException
	{
		System.out.println("Add Smokations");
		
		List<String> latitudes = requestHeaders.get("Latitude");
		List<String> longitudes = requestHeaders.get("Longitude");
		
		//add all of the latitudes and longitudes to the smokations array
		if(latitudes.size() == longitudes.size())
		{
			for(int i = 0; i < latitudes.size(); i++)
			{
				Double latitude = Double.parseDouble(latitudes.get(i));
				Double longitude = Double.parseDouble(longitudes.get(i));
				Double[] smokation = {latitude, longitude};
				
				smokations.add(smokation);
			}
		}
		
		
		String response = "Thank you for that new Smokation\n";
		exchange.sendResponseHeaders(200, response.length());
		OutputStream os = exchange.getResponseBody();
		os.write(response.getBytes());
		os.close();
		
		printSmokations();
	}
}