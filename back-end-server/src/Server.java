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
	public ArrayList<Smokation> smokations;
	
	public void printSmokations()
	{
		System.out.println("Smokations!!!");
		for(Smokation smokation : smokations)
		{
			System.out.println("Lat : " + smokation.getLatitude() + ", Long: " + smokation.getLongitude());
		}
		System.out.println();
	}
	
	Server()
	{
		smokations = new ArrayList<Smokation>();
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
				exchange.sendResponseHeaders(200, 0);
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
		//serialize smokations and sends it to client
		ObjectOutputStream o = new ObjectOutputStream(exchange.getResponseBody());
		//sends size of arraylist being sent
		o.writeInt(smokations.size());
		//starts "new line"
		o.reset();
		for(int i=0;i<smokations.size();i++){
			//write arraylist object by object
			o.writeObject(smokations.get(i));
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

	//a request to add a point
	public void handleAddRequest(HttpExchange exchange, Headers requestHeaders) throws IOException
	{		
		List<String> latitudes = requestHeaders.get("Latitude");
		List<String> longitudes = requestHeaders.get("Longitude");
		
		//add all of the latitudes and longitudes to the smokations array
		if(latitudes.size() == longitudes.size())
		{
			for(int i = 0; i < latitudes.size(); i++)
			{
				Double latitude = Double.parseDouble(latitudes.get(i));
				Double longitude = Double.parseDouble(longitudes.get(i));
				Smokation smokation = new Smokation(latitude,longitude);
				
				smokations.add(smokation);
			}
		}
		
		
		String response = "Thank you for that new Smokation\n";
		/*exchange.sendResponseHeaders(200, response.length());
		OutputStream os = exchange.getResponseBody();
		os.write(response.getBytes());
		os.close();*/
		
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		ObjectOutputStream o = new ObjectOutputStream(b);
		o.writeObject(response);
		o.close();
		//printSmokations();
	}
}