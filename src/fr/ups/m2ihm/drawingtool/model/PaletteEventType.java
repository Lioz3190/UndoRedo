package fr.ups.m2ihm.drawingtool.model;

public enum PaletteEventType {
    DRAW_LINE("drawLineProperty"),
    DRAW_RECTANGLE("drawRectangleProperty"),
    DRAW_MACRO("drawMacroProperty"),
    DRAW_REGIONAL("drawMacroProperty");

    private final String propertyName;

    private PaletteEventType(String propertyName) {
        this.propertyName = propertyName;
    }


    public String getPropertyName() {
        return propertyName;
    }
}
