import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoCommandException;
import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.util.ArrayList;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;


public class MongoDB extends TransactionLog {

    /**
     * Create connection to db
     */

    public MongoDatabase CreateTransConnection() {
        MongoClient client = MongoClients.create(Source.URI.getSource());
        MongoDatabase database = client.getDatabase(Source.DATABASE.getSource());
        TransactionLog log = new TransactionLog();
        log.insertLog("Monitor thread successfully connected to server" + Source.URI.getSource(), 1);
        return database;
    }

    /**
     * Insert data  to db
     */

    public void InsertCollection(MongoDatabase database, ArrayList<Document> docs) {
        TransactionLog log = new TransactionLog();
        try {
            database.getCollection(Source.TRANSACTION.getSource()).drop();
            log.insertLog("transaction_db_raw.transaction successful dropped", 0);
        } catch (MongoCommandException e) {
            log.insertLog(e.getErrorMessage(), 0);
        }

        try {
            database.createCollection(Source.TRANSACTION.getSource());
            log.insertLog("Collection successful created transaction_db_raw.transaction", 1);
            try {
                MongoCollection<Document> collection = database.getCollection(Source.TRANSACTION.getSource());
                collection.insertMany(docs);
                log.insertLog("Insert row transaction_db_raw.transaction", 0);
            } catch (MongoCommandException e) {
                log.insertLog(e.getErrorMessage(), 0);
            }
        } catch (MongoCommandException e) {
            log.insertLog(e.getErrorMessage(), 0);
        }
    }

    public void DeleteRandomValue(MongoDatabase database, String filterVal) {
        TransactionLog log = new TransactionLog();
        try {
            MongoCollection<Document> collection = database.getCollection(Source.TRANSACTION.getSource());
            long beforeDrop = collection.countDocuments();
            Bson filter = eq(ColumnName.RANDOM_INT.getColumns(), filterVal);
            collection.deleteMany(filter);
            long afterDrop = collection.countDocuments();
            var countRow = beforeDrop - afterDrop;
            log.insertLog("Removed ".concat(String.valueOf(countRow).concat(" fields")), 0);
        } catch (MongoCommandException e) {
            log.insertLog(e.getErrorMessage(), 0);
        }
    }

    /**
     * Insert data  to db
     */
    public void UpdateRandomValue(MongoDatabase database, String RandIntFilterVal, String colName, String Val) {
        TransactionLog log = new TransactionLog();
        try {
            Bson filter = eq(ColumnName.RANDOM_INT.getColumns(), RandIntFilterVal);
            MongoCollection<Document> collection = database.getCollection(Source.TRANSACTION.getSource());
            Bson updateOperation = set(colName, Val);
            UpdateResult updateResult = collection.updateMany(filter, updateOperation);
            log.insertLog(updateResult.toString(), 0);
        } catch (MongoCommandException e) {
            log.insertLog(e.getErrorMessage(), 0);
        }
    }

    public void Recompile() throws IOException, InterruptedException {
            var command1="db.transaction.aggregate([{$lookup:{from: transact_tmp,\n" +
                    "                localField: _id,\n" +
                    "                foreignField: _id,\n" +
                    "                as: JoinData}}]).forEach((Mydoc) => \n" +
                    "                {db.getCollection(test).insertOne({ _id: Mydoc._id }); });";
        String command ="sh mongo";
        Process proc = Runtime.getRuntime().exec(command);
        Process proc1 = Runtime.getRuntime().exec(command1);

        BufferedReader reader =
                new BufferedReader(new InputStreamReader(proc.getInputStream()));
        String line = "";
        while((line = reader.readLine()) != null) {
            System.out.print(line + "\n");
        }
        proc.waitFor();

        BufferedReader reader1 =
                new BufferedReader(new InputStreamReader(proc.getInputStream()));
        String line1 = "";
        while((line1 = reader1.readLine()) != null) {
            System.out.print(line1 + "\n");
        }
        proc1.waitFor();

    }
    }

