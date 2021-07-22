package controller;

public enum ShiftingKey {
    LEFT (-1),
    RIGHT (1);

    private int value;

    private ShiftingKey(int value){
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
