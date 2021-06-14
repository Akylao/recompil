import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.mongodb.MongoCommandException;
import com.mongodb.client.MongoCollection;
import com.opencsv.CSVWriter;
import org.bson.Document;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;


public class CsvFile extends TransactionLog {

    public void CsVcreated(List<String[]> list) throws IOException {
        FileWriter file = new FileWriter(Source.WORKING_DIR.getSource());
        TransactionLog log = new TransactionLog();
        try (CSVWriter fw = new CSVWriter(file)) {
            fw.writeAll(list);
            log.insertLog("Data loaded into file ", 0);
        } catch (IOException e) {
            log.insertLog(e.getMessage(), 0);
        }
    }

    public void CreateCsvToCollection() throws InterruptedException, IOException {
        StringBuilder command = new StringBuilder("sh mongoimport --db ").append(Source.DATABASE.getSource()).append(" --collection ").append(Source.TRANSACTION_TMP.getSource()).append(" --type csv --headerline --file ").append(Source.WORKING_DIR.getSource()).append(";");
        System.out.println(command);
        Process process = Runtime.getRuntime().exec(String.valueOf(command));
        process.waitFor();
        if (process.exitValue() != 0) {
            System.out.println();
            System.out.println("Command: " + command);
            InputStream errorStream = process.getErrorStream();
            int c = 0;
            while ((c = errorStream.read()) != -1) {
                System.out.print((char) c);
            }

        }
    }

    public void MongoImport(){
        MongoDB db = new MongoDB();
        TransactionLog log = new TransactionLog();
        try {
            db.CreateTransConnection().getCollection(Source.TRANSACTION_TMP.getSource()).drop();
            log.insertLog("transaction_db_raw.transaction_tmp successful dropped", 0);
        } catch (MongoCommandException e) {
            log.insertLog(e.getErrorMessage(), 0);
        }
        db.CreateTransConnection().createCollection(Source.TRANSACTION_TMP.getSource());
        log.insertLog("transaction_db_raw.transaction_tmp successful created", 0);
        MongoCollection<Document> collection =db.CreateTransConnection().getCollection(Source.TRANSACTION_TMP.getSource());
        File input = new File(Source.WORKING_DIR.getSource());
        try {
            CsvSchema csv = CsvSchema.emptySchema().withHeader();
            CsvMapper csvMapper = new CsvMapper();
            MappingIterator<Map<?, ?>> mappingIterator =  csvMapper.reader().forType(Map.class).with(csv).readValues(input);
            List<Map<?, ?>> list = mappingIterator.readAll();
            int counter = 0;
            for( Map o : list ) {
                counter ++;
                Document tmpDocument = new Document();
                o.forEach((k, v) ->
                        tmpDocument.put(k.toString(), v));
                collection.insertOne(tmpDocument);
            }
            log.insertLog(counter +" documents insert into the collection transaction_db_raw.transaction_tmp", 0);
        } catch(Exception e) {
            log.insertLog(e.getStackTrace().toString(), 0);
        }
    }



}
