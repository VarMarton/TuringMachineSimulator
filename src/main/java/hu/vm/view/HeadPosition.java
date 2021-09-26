package hu.vm.view;

public enum HeadPosition {

    NORMAL(54.0, 83.0, "icons/normalPointer.png", "head normal-head"),
    HIGHER(19.0, 51.0, "icons/higherPointer.png", "head higher-head");

    private final Double headPositionY;
    private final Double pointerPositionY;
    private final String pointerImgUrl;
    private final String styleClass;

    HeadPosition(Double headPositionY, Double pointerPositionY, String pointerImgUrl, String styleClass) {
        this.headPositionY = headPositionY;
        this.pointerPositionY = pointerPositionY;
        this.pointerImgUrl = pointerImgUrl;
        this.styleClass = styleClass;
    }

    public Double getHeadPositionY() {
        return headPositionY;
    }

    public Double getPointerPositionY() {
        return pointerPositionY;
    }

    public String getPointerImgUrl() {
        return pointerImgUrl;
    }

    public String getStyleClass() {
        return styleClass;
    }
}
