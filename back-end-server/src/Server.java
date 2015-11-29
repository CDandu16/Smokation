import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

/**
 * @author Cole Hudson
 */

public class Server implements HttpHandler
{
	public static void main(String[] args) throws Exception 
	{
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/", new Server());
        server.setExecutor(null); // creates a default executor
        server.start();
    }

    public void handle(HttpExchange t) throws IOException 
	{
    	//request info
    	Headers requestHeaders = t.getRequestHeaders();
    	
    	boolean getSmokations = requestHeaders.get("RequestType").get(0).equals("getSmokations");
    	boolean addSmokation = requestHeaders.get("RequestType").get(0).equals("addSmokation");
    	
    	if(getSmokations)
    	{
    		System.out.println("Get Smokations");
    	}
    	
    	if(addSmokation)
    	{
    		System.out.println("Add Smokations");
    	}
    	
        String response = "Smokation\n";
        t.sendResponseHeaders(200, response.length());
        OutputStream os = t.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
    
    //a request to add a point
    public void handleAddRequest()
    {
    	
    }
    
    //handle a request to get all points
    public void handleGetPointsRequest()
    {
    	
    }
}