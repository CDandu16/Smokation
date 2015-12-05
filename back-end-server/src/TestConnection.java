import java.awt.List;
import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class TestConnection {
	private static final long serialVersionUID = 	8664875232659988799L;

	public static void main(String [] args) throws ClassNotFoundException{
		try {
			URL url = new URL("http://localhost:8000/");
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setRequestProperty("RequestType", "getSmokations");
			connection.connect();
			
			
			BufferedReader in = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			String inputLine;
			in.close();
			
			ArrayList<Double[]> returned = (ArrayList<Double[]>)(deserialize(in.readLine().getBytes()));
			
		}catch(MalformedURLException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public static Object deserialize(byte[] string) throws IOException, ClassNotFoundException {
        ByteArrayInputStream b = new ByteArrayInputStream(string);
        ObjectInputStream o = new ObjectInputStream(b);
        return o.readObject();
 }
}