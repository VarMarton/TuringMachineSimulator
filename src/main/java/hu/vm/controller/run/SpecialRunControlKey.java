package hu.vm.controller.run;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SpecialRunControlKey {
    ANY("ANY", "ANY"),
    SPACE("SP", " "),
    LEFT_SQUARE_BRACKET("LSB", "["),
    RIGHT_SQUARE_BRACKET("RSB", "]"),
    SEMICOLON("SC", ";"),
    COLON("COL", ",");

    private final String readValue;
    private final String writeValue;

}
