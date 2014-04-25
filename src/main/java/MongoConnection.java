import java.net.UnknownHostException;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

public class MongoConnection{
	
	public static void main() throws UnknownHostException{

		
	    MongoClientURI uri = new MongoClientURI(textUri);
		MongoClient m = new MongoClient(uri);
		System.out.println("Connected!!");
	}
}