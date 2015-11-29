import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;

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

	@Override
    public void handle(HttpExchange t) throws IOException 
	{
        String response = "Smokation\n";
        t.sendResponseHeaders(200, response.length());
        OutputStream os = t.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
