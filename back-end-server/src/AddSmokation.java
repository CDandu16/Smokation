import java.awt.List;
import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class AddSmokation {
	private static final long serialVersionUID = 8664875232659988799L;

	public static void main(String[] args) throws ClassNotFoundException {
		try {
			URL url = new URL("http://127.0.0.1:8000/");
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestProperty("RequestType", "addSmokation");
			connection.addRequestProperty("Latitude", "600");
			connection.addRequestProperty("Longitude", "500");
			connection.connect();
			ObjectInputStream input = new ObjectInputStream(connection.getInputStream());
			System.out.println(input.readUTF());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Object deserialize(byte[] string) throws IOException, ClassNotFoundException {
		ByteArrayInputStream b = new ByteArrayInputStream(string);
		ObjectInputStream o = new ObjectInputStream(b);
		return o.readObject();
	}
}
