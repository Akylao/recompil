import com.mongodb.MongoCommandException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;


public class TransactionLog {
    /**
     * create  action to insert logs
     */
    public void insertLog(String l, int stat) {
        String uri = Source.URI.getSource();
        MongoClient client = MongoClients.create(uri);
        MongoDatabase database = client.getDatabase(Source.DATABASE.getSource());
        var docs = new ArrayList<Document>();
        MongoCollection<Document> collection = database.getCollection(Source.COLLECTION_LOG.getSource());
        var value = new Document();
        value.append("Date", LocalDateTime.now());
        value.append("logValue", l);
        docs.add(value);
        collection.insertMany(docs);
        if (stat == 0) {
            client.close();
        }
    }

    /**
     * Create Log table
     */
    public void CreateTransactionLog() {
        String l = "";
        MongoClient client = MongoClients.create(Source.URI.getSource());
        MongoDatabase database = client.getDatabase(Source.DATABASE.getSource());
        try {
            database.createCollection(Source.COLLECTION_LOG.getSource());
            l = "Transaction_log successful created";
            insertLog(l, 0);
        } catch (MongoCommandException e) {
            insertLog(e.getErrorMessage(), 0);
        }
    }

}
