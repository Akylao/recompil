import org.bson.Document;
import org.fluttercode.datafactory.impl.DataFactory;

import java.util.Date;
import java.util.UUID;

public class GenerateRandomData {
    private String firstNameVal;
    private String lastNameVal;
    private String addressVal;
    private UUID uuidVal;
    private Date startVal;
    private Date endVal;
    private Integer randomIntVal;

    public GenerateRandomData() {
        DataFactory df = new DataFactory();
        setFirstName(df.getFirstName());
        setLastName(df.getLastName());
        setAddressVal(df.getAddress());
        setUuidVal(UUID.randomUUID());
        int min = 1;
        int max = 5;
        Date minDate = df.getDate(2021, 1, 1);
        Date maxDate = new Date();
        setStartVal(df.getDateBetween(minDate, maxDate));
        setEndVal(df.getDateBetween(startVal, maxDate));
        setRandomIntVal((int) Math.floor(Math.random() * (max - min + 1) + min));
    }


    public String getFirstNameVal() {
        return firstNameVal;
    }

    public String getLastNameVal() {
        return lastNameVal;
    }

    public String getAddressVal() {
        return addressVal;
    }

    public UUID getUuidVal() {
        return uuidVal;
    }

    public Date getStartVal() {
        return startVal;
    }

    public Date getEndVal() {
        return endVal;
    }

    public Integer getRandomIntVal() {
        return randomIntVal;
    }

    public void setFirstName(String firstNameVal) {
        this.firstNameVal = firstNameVal;
    }

    public void setLastName(String lastNameVal) {
        this.lastNameVal = lastNameVal;
    }

    public void setAddressVal(String addressVal) {
        this.addressVal = addressVal;
    }

    public void setUuidVal(UUID uuid) {
        this.uuidVal = uuid;
    }

    public void setStartVal(Date start) {
        this.startVal = start;
    }

    public void setEndVal(Date end) {
        this.endVal = end;
    }

    public void setRandomIntVal(Integer randomIntVal) {
        this.randomIntVal = randomIntVal;
    }


    public String[] GenerateScvData() {
        String[] data = {getFirstNameVal(), getLastNameVal(),
                getAddressVal(), getUuidVal().toString(), getStartVal().toString(),
                getEndVal().toString(), String.valueOf(getRandomIntVal())};
        return data;


    }


    public Document GenerateDocsTableData() {

        Document value = new Document();
        value.append(ColumnName.FIRST_NAME.getColumns(), getFirstNameVal());
        value.append(ColumnName.LAST_NAME.getColumns(), getLastNameVal());
        value.append(ColumnName.ADDRESS.getColumns(), getAddressVal());
        value.append(ColumnName.UUID.getColumns(), getUuidVal().toString());
        value.append(ColumnName.START.getColumns(), getStartVal().toString());
        value.append(ColumnName.END.getColumns(), getEndVal().toString());
        value.append(ColumnName.RANDOM_INT.getColumns(), String.valueOf(getRandomIntVal()));
        return value;
    }
}
