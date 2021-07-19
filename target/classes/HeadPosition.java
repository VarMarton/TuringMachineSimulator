package view;

public enum HeadPosition {

    NORMAL(53.0, 83.0, "icons/normalPointer.png", "normalHeadBtn"),
    HIGHER(18.0, 51.0, "icons/higherPointerAlt.png", "higherHeadBtn");

    private final Double headPosition;
    private final Double pointerPosition;
    private final String pointerImgUrl;
    private final String styleClass;

    HeadPosition(Double headPosition, Double pointerPosition, String pointerImgUrl, String styleClass) {
        this.headPosition = headPosition;
        this.pointerPosition = pointerPosition;
        this.pointerImgUrl = pointerImgUrl;
        this.styleClass = styleClass;
    }

    public Double getHeadPosition() {
        return headPosition;
    }

    public Double getPointerPosition() {
        return pointerPosition;
    }

    public String getPointerImgUrl() {
        return pointerImgUrl;
    }

    public String getStyleClass() {
        return styleClass;
    }
}
