package hu.vm.controller.gui.tape;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ShiftingKey {
    LEFT(-1),
    RIGHT(1);

    private final int value;

}
