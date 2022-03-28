package hu.vm.view;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum HeadPosition {

    NORMAL(54.0, 83.0, "icons/normalPointer.png", "head normal-head"),
    HIGHER(19.0, 51.0, "icons/higherPointer.png", "head higher-head");

    private final Double headPositionY;
    private final Double pointerPositionY;
    private final String pointerImgUrl;
    private final String styleClass;

}
