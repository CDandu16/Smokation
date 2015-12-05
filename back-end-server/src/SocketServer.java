//Cole Hudson

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
	}
}
