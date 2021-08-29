package controller.run;

public enum SpecialRunControlKey {
    ANY("ANY", "ANY"),
    SPACE("SP", " "),
    LEFT_SQUARE_BRACKET("LSB", "["),
    RIGHT_SQUARE_BRACKET("RSB", "]"),
    SEMICOLON("SC", ";"),
    COLON("COL", ",");

    private String readValue;
    private String writeValue;

    SpecialRunControlKey(String readValue, String writeValue) {
        this.readValue = readValue;
        this.writeValue = writeValue;
    }

    public String getReadValue() {
        return readValue;
    }

    public String getWriteValue() {
        return writeValue;
    }
}
