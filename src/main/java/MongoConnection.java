import java.net.UnknownHostException;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

public class MongoConnection{
	
	public static void main() throws UnknownHostException{

		String textUri = "mongodb://pawans55:_technical55@ds053658.mongolab.com:53658/dropbox";
	    MongoClientURI uri = new MongoClientURI(textUri);
		MongoClient m = new MongoClient(uri);
		System.out.println("Connected!!");
	}
}