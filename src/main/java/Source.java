public enum Source {
    URI("mongodb://localhost:27017"),
    DATABASE("transaction_db_raw"),
    COLLECTION_LOG("transaction_log"),
    TRANSACTION("transaction"),
    WORKING_DIR("/Users/akylai/IdeaProjects/file/tru.csv"),
    TRANSACTION_TMP("transact_tmp");

    private final String source;

    Source(String source) {
        this.source = source;
    }

    public String getSource() {
        return source;
    }

}
