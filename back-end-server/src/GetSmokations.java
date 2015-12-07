import java.awt.List;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import com.example.SmokationModel.*;

public class GetSmokations {

	static ArrayList<Smokation> smokers = new ArrayList<Smokation>();

	public static void main(String[] args) throws ClassNotFoundException {
		try {
			URL url = new URL("http://45.55.156.205:8000");
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestProperty("RequestType", "getSmokations");
			connection.connect();
			ObjectInputStream input = new ObjectInputStream(connection.getInputStream());
			double latitude;
			double longitude;
			int counter = input.readInt();
			for (int i = 0; i < counter; i++) {
				latitude = input.readDouble();
				longitude = input.readDouble();
				Smokation smoke = new Smokation(latitude,longitude);
				smokers.add(smoke);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// prints out smoker locations
		System.out.println(smokers.toString());

	}

	public static Object deserialize(byte[] string) throws IOException, ClassNotFoundException {
		ByteArrayInputStream b = new ByteArrayInputStream(string);
		ObjectInputStream o = new ObjectInputStream(b);
		return o.readObject();
	}
}
