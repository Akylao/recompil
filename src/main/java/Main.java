import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.MongoCommandException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Aggregates;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.apache.commons.io.IOUtils;
import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.conversions.Bson;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;


public class Main {

    public static void main(String[] args) throws IOException, InterruptedException, CsvValidationException {
        /**
         * Create transaction log data
         */
        TransactionLog newLog = new TransactionLog();
        newLog.CreateTransactionLog();
        /**
         * Create CSV file and insert data MongoDB Transaction
         */
        List<String[]> list = new ArrayList<>();
        String[] headerRow = {ColumnName.FIRST_NAME.getColumns(),ColumnName.LAST_NAME.getColumns(),ColumnName.ADDRESS.getColumns(),ColumnName.UUID.getColumns(),ColumnName.START.getColumns(),ColumnName.END.getColumns(),ColumnName.RANDOM_INT.getColumns()};
        list.add(headerRow);
        ArrayList<Document> docs = new ArrayList<Document>();
        for (int i = 0; i <= 10; i++) {
            GenerateRandomData rowVal = new GenerateRandomData();
            list.add(rowVal.GenerateScvData());
            docs.add(rowVal.GenerateDocsTableData());
         }
         CsvFile file = new CsvFile();
         file.CsVcreated(list);
        MongoDB db = new MongoDB();
        db.InsertCollection(db.CreateTransConnection(), docs);
        /**
         * Removed fields in Collection
         */
        GenerateRandomData deleteRowVal = new GenerateRandomData();
        MongoDB dbs = new MongoDB();
        dbs.DeleteRandomValue(dbs.CreateTransConnection(), deleteRowVal.getRandomIntVal().toString());
        /**
         * Update random data in Collection
         */
        GenerateRandomData updateRowVal = new GenerateRandomData();
        MongoDB dbl = new MongoDB();
        dbl.UpdateRandomValue(dbl.CreateTransConnection(), updateRowVal.getRandomIntVal().toString(), ColumnName.ADDRESS.getColumns(), updateRowVal.getAddressVal());
        /**
         * Get Csv into Mongo DB Collection
         */
         CsvFile importDataToDb = new CsvFile();
        importDataToDb.MongoImport();
        /**
         * Reconciliation
          */
        MongoDB ds = new MongoDB();
        ds.Recompile();
    }
}

