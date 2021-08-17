package controller.message;

public enum MessageType {
    ERROR("ERROR"),
    INFO("INFO"),
    WARNING("WARNING");

    private String value;

    MessageType(String value) {
        this.value = value;
    }

    public String getAsString() {
        return value;
    }
}
