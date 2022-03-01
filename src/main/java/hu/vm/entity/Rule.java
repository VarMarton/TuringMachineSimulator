package hu.vm.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class Rule {

    private boolean isValid;

    private String currentState;
    private ArrayList<String> currentSymbols;

    private String nextState;
    private ArrayList<String> symbolsToWrite;
    private ArrayList<String> movements;

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            stringBuilder.append("[").append(currentState);
            for (String symbol : currentSymbols) {
                stringBuilder.append(";").append(symbol);
            }
            stringBuilder.append("]->[").append(nextState);
            for (String symbol : symbolsToWrite) {
                stringBuilder.append(";").append(symbol);
            }
            for (String movement : movements) {
                stringBuilder.append(";").append(movement);
            }
            stringBuilder.append("]");
        } catch (NullPointerException e) {
            return super.toString();
        }

        return stringBuilder.toString();
    }
}
