public enum ColumnName {
    FIRST_NAME ("FIRST_NAME"),
    LAST_NAME ("LAST_NAME"),
    ADDRESS ("ADDRESS"),
    UUID ("UUID"),
    START ("START"),
    END ("END"),
    RANDOM_INT ("RANDOM_INT");

 private final String columns;

    ColumnName(String columns){
        this.columns=columns;
    }
    public  String getColumns(){
        return columns;
    }

}
