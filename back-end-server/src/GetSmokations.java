import java.awt.List;
import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class GetSmokations {

	static ArrayList<Smokation> smokers = new ArrayList<Smokation>();
	
	public static void main(String [] args) throws ClassNotFoundException{
		try {
			URL url = new URL("http://localhost:8000/");
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setRequestProperty("RequestType", "getSmokations");
			connection.connect();
			ObjectInputStream input = new ObjectInputStream(connection.getInputStream());
			Smokation smoke;
			int counter = input.readInt();
			for(int i=0;i<counter;i++){
				smoke = (Smokation)input.readObject();
				smokers.add(smoke);
			}
		}catch(MalformedURLException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
		//prints out smoker locations
		System.out.println(smokers.toString());
		
	}
	
	public static Object deserialize(byte[] string) throws IOException, ClassNotFoundException {
        ByteArrayInputStream b = new ByteArrayInputStream(string);
        ObjectInputStream o = new ObjectInputStream(b);
        return o.readObject();
 }
}
