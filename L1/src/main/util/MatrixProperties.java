package main.util;

/**
 * Created by a1 on 01.10.16.
 */
public enum MatrixProperties {

    ROWS("Rows"),
    COLUMNS("Columns");

    private final String property;

    MatrixProperties(String name) {
        this.property = name;
    }

    public String getProperty() {
        return property;
    }

}
