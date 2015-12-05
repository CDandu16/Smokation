//Cole Hudson

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.bson.Document;

import com.mongodb.Block;
import com.mongodb.client.FindIterable;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Sorts.ascending;
import static java.util.Arrays.asList;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

public class SocketServer 
{
	public static void main(String[] args)
	{
		MongoClient mongoClient = new MongoClient();
		MongoDatabase db = mongoClient.getDatabase("test");
		
		db.getCollection("smokations").insertOne(new Document().append("latitude", 666).append("longitude", 666));
		
		FindIterable<Document> iterable = db.getCollection("smokations").find();
		
		iterable.forEach(new Block<Document>() {
		    public void apply(final Document document) {
		        System.out.println(document);
		    }
		});
	}
}
