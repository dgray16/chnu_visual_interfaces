package main.controller;

import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.util.MatrixProperties;

import java.util.*;

/**
 * Created by a1 on 29.09.16.
 */
public class MatrixController {

    private Set<TextField> textFields = new LinkedHashSet<>();

    private AnchorPane matrixMainPane;
    
    private AnchorPane matrixResultPane;

    private TabPane tabPane;

    private GridPane matrix;

    private GridPane resultMatrix;

    private TextField rowsTextField;

    private TextField columnsTextField;

    private Alert alert = new Alert(Alert.AlertType.ERROR);

    private List<Label> rowValuesListLabelsOnMainMatrixPane = new ArrayList<>();

    private List<Label> rowValuesListLabelsOnResultMatrixPane = new ArrayList<>();

    private boolean isSDI = true;


    public void createSettingsComponents(AnchorPane anchorPane, boolean isSDI) {
        this.isSDI = isSDI;
        this.matrixMainPane = anchorPane;
        if ( !isSDI ) {
            this.tabPane = (TabPane) matrixMainPane.getParent().getParent();
        }

        HBox generalHBox = new HBox();
        generalHBox.getChildren().addAll(
                appendSection(MatrixProperties.ROWS),
                appendSection(MatrixProperties.COLUMNS),
                appendGenerateButton(),
                appendClearButton(),
                appendRandomFillButton(),
                appendProcessButton());

        if ( !isSDI ) {
            generalHBox.setLayoutX(20);
            generalHBox.setLayoutY(1);
        }

        anchorPane.getChildren().add(generalHBox);
    }

    private VBox appendSection(MatrixProperties labelValue) {
        VBox vBox = new VBox();

        Label rowsLabel = new Label(labelValue.getProperty());

        TextField textField = new TextField();

        if ( labelValue.equals(MatrixProperties.ROWS) ) {
            this.rowsTextField = textField;
            textField.setId(MatrixProperties.ROWS.getProperty());
        } else {
            textField.setId(MatrixProperties.COLUMNS.getProperty());
            this.columnsTextField = textField;
        }

        textField.setMaxWidth(60);

        vBox.getChildren().addAll(rowsLabel, textField);
        vBox.setMargin(rowsLabel, new Insets(5));
        vBox.setMargin(textField, new Insets(5));

        textFields.add(textField);

        return vBox;
    }

    private VBox appendGenerateButton() {
        Button button = new Button("Generate");

        button.setOnAction(event -> {

            if ( matrix == null ) {
                int rows = 0;
                int columns = 0;

                for (TextField item : textFields) {

                    String value = item.getText();

                    if ( !value.isEmpty() ) {
                        if ( item.getId().equals(MatrixProperties.ROWS.getProperty()) ) {
                            rows = Integer.valueOf(value);
                        } else if ( item.getId().equals(MatrixProperties.COLUMNS.getProperty()) ) {
                            columns = Integer.valueOf(item.getText());
                        }
                    }
                }

                if ( rows > 20 || columns > 20 ) {
                    generateErrorAlert("No more than 20");
                } else if (rows > 0 && columns > 0  ) {
                    matrix = generateMatrix(rows, columns);
                    matrixMainPane.getChildren().add(matrix);
                } else {
                    generateErrorAlert("Rows and columns should be more than 0");
                }

            } else {
                generateErrorAlert("Please, clear existing matrix");
            }
        });

        VBox vbox = new VBox();
        vbox.getChildren().addAll(button);
        vbox.setMargin(button, new Insets(32, 0, 0, 10));

        return vbox;
    }

    private VBox appendClearButton() {
        Button button = new Button("Clear");

        button.setOnAction(event -> {
            if ( matrix != null ) {
                matrixMainPane.getChildren().remove(matrix);
                matrix = null;
            }

            if ( resultMatrix != null ) {
                matrixResultPane.getChildren().remove(resultMatrix);
                resultMatrix = null;
            }

            if ( !rowValuesListLabelsOnMainMatrixPane.isEmpty() ) {
                for (Label item : rowValuesListLabelsOnMainMatrixPane) {
                    matrixMainPane.getChildren().remove(item);
                }

                rowValuesListLabelsOnMainMatrixPane.clear();
            }

            if ( !rowValuesListLabelsOnResultMatrixPane.isEmpty() ) {
                for (Label item : rowValuesListLabelsOnResultMatrixPane) {
                    matrixResultPane.getChildren().remove(item);
                }

                rowValuesListLabelsOnResultMatrixPane.clear();
            }
        });

        VBox vbox = new VBox();
        vbox.getChildren().addAll(button);
        vbox.setMargin(button, new Insets(32, 0, 0, 10));

        return vbox;
    }

    private VBox appendRandomFillButton() {
        Button button = new Button("Random numbers");

        button.setOnAction(event -> {
            if ( matrix != null && !matrix.getChildren().isEmpty() ) {
                fillRandomNumbers(matrix);
                createRowValuesLabels(true);
            }
        });

        VBox vbox = new VBox();
        vbox.getChildren().addAll(button);
        vbox.setMargin(button, new Insets(32, 0, 0, 10));

        return vbox;
    }

    private VBox appendProcessButton() {
        Button button = new Button("Process");

        button.setOnAction(event -> {
            if ( !matrix.getChildren().isEmpty() ) {
                if ( isSDI ) {
                    AnchorPane anchorPane = new AnchorPane();
                    Scene scene = new Scene(anchorPane, 1070, 560);
                    Stage stage = new Stage();
                    stage.setScene(scene);
                    stage.setTitle("Matrix result");
                    matrixResultPane = anchorPane;
                    proceedMatrixResultPane(anchorPane);
                    createRowValuesLabels(false);
                    stage.showAndWait();
                } else {
                    this.matrixResultPane = (AnchorPane) tabPane.getTabs().get(1).getContent();
                    proceedMatrixResultPane(matrixResultPane);
                    createRowValuesLabels(false);
                    generateInfoAlert("Complete");
                }
            }
        });

        VBox vBox = new VBox();
        vBox.getChildren().addAll(button);
        vBox.setMargin(button, new Insets(32, 0, 0, 10));

        return vBox;
    }

    private void countRowValues(boolean isMainMatrixPane) {
        List<Integer> values = getValuesListOfRows();

        if ( isMainMatrixPane ) {
            for (int i = 0; i < rowValuesListLabelsOnMainMatrixPane.size(); i++) {
                rowValuesListLabelsOnMainMatrixPane.get(i).setText(values.get(i).toString());
            }
        } else {
            List<Integer> valuesInReverseOrder = sortValues(values);
            for (int i = 0; i < rowValuesListLabelsOnResultMatrixPane.size(); i++) {
                rowValuesListLabelsOnResultMatrixPane.get(i).setText(valuesInReverseOrder.get(i).toString());
            }
        }

    }

    private void createRowValuesLabels(boolean isMainMatrixPane) {
        if ( isMainMatrixPane ) {
            if ( !rowValuesListLabelsOnMainMatrixPane.isEmpty() ) {
                countRowValues(true);
            } else {

                int x = Double.valueOf(matrix.getLayoutX()).intValue() - 40;
                int y = Double.valueOf(matrix.getLayoutY()).intValue() + 2;


                for (int i = 0; i < Integer.valueOf(rowsTextField.getText()); i++) {
                    Label label = createEmptyLabel();

                    label.setLayoutX(x);
                    label.setLayoutY(y);

                    y += 27;
                    rowValuesListLabelsOnMainMatrixPane.add(label);
                    matrixMainPane.getChildren().add(label);
                }

                countRowValues(true);
            }
        } else {
            if ( !rowValuesListLabelsOnResultMatrixPane.isEmpty() ) {
                countRowValues(false);
            } else {

                int x = Double.valueOf(resultMatrix.getLayoutX()).intValue() + 5;
                int y = Double.valueOf(resultMatrix.getLayoutY()).intValue() + 11;


                for (int i = 0; i < Integer.valueOf(rowsTextField.getText()); i++) {
                    Label label = createEmptyLabel();

                    label.setLayoutX(x);
                    label.setLayoutY(y);

                    y += 27;
                    rowValuesListLabelsOnResultMatrixPane.add(label);
                    matrixResultPane.getChildren().add(label);
                }

                countRowValues(false);
            }
        }
    }

    private void proceedMatrixResultPane(AnchorPane matrixResultPane) {
        /*
         * Key == row index of matrix grid.
         * Value == summary of all elements in row.
         */
        Map<Integer, Integer> rowValueMap = getValuesOfRows();

        /* Draw new matrix with new data about rows values */
        matrixResultPane.getChildren().add(getResultMatrix(sortValues(rowValueMap)));
    }

    private VBox getResultMatrix(Map<Integer, Integer> map) {
        VBox vbox = new VBox();

        GridPane gridPane = new GridPane();

        Object[] keys = map.keySet().toArray();

        for (int rowIndex = 0; rowIndex < map.keySet().size(); rowIndex++) {
            List<TextField> textFieldsList = getRowByIndex(Integer.parseInt(keys[rowIndex].toString()));

            for (int column = 0; column < textFieldsList.size(); column++) {
                TextField textField = createEmptyTextField();
                textField.setText(textFieldsList.get(column).getText());

                gridPane.add(textField, column, rowIndex);
            }
        }

        gridPane.setGridLinesVisible(true);

        vbox.setMargin(gridPane, new Insets(10, 10, 10, 50));

        vbox.getChildren().add(gridPane);

        resultMatrix = gridPane;

        return vbox;
    }

    /**
     * Sorting values from highest to lowest.
     */
    private Map<Integer, Integer> sortValues(Map<Integer, Integer> messyMap) {
        /* Converting map to List */
        List<Map.Entry<Integer, Integer>> listToSort = new LinkedList<>(messyMap.entrySet());

        /* Sort List with custom Comparator */
        Collections.sort(listToSort, (o1, o2) -> o1.getValue().compareTo(o2.getValue()));

        /* Sort in reverse order */
        Collections.reverse(listToSort);

        /* Put sorted values to Map */
        Map<Integer, Integer> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<Integer, Integer> item : listToSort) {
            sortedMap.put(item.getKey(), item.getValue());
        }

        return sortedMap;
    }

    /**
     * It is almost the same implementation as {@link #sortValues(Map)}.
     * In some case I need {@link List} instead of {@link Map}.
     */
    private List<Integer> sortValues(List<Integer> messyList) {
        Collections.sort(messyList);
        Collections.reverse(messyList);

        return messyList;
    }

    private Map<Integer, Integer> getValuesOfRows() {
        Map<Integer, Integer> rowValuesMap = new HashMap<>();

        for (int i = 0; i < Integer.valueOf(rowsTextField.getText()); i++) {
            int rowValue = 0;

            for ( TextField item : getRowByIndex(i) ) {
                rowValue += Integer.valueOf(item.getText());
            }
            rowValuesMap.put(i, rowValue);
            rowValue = 0;
        }

        return rowValuesMap;
    }

    /**
     * It is almost the same method as {@link #getValuesOfRows()}.
     * In some case I need {@link List} instead of {@link Map}.
     */
    private List<Integer> getValuesListOfRows() {
        List<Integer> rowValues = new ArrayList<>();

        for (int i = 0; i < Integer.valueOf(rowsTextField.getText()); i++) {
            int rowValue = 0;

            for ( TextField item : getRowByIndex(i) ) {
                rowValue += Integer.valueOf(item.getText());
            }
            rowValues.add(rowValue);
        }

        return rowValues;
    }

    private List<TextField> getRowByIndex(int index) {
        List<TextField> listToReturn = new ArrayList<>();
        for ( Node item : matrix.getChildren() ) {

            if ( item instanceof TextField ) {
                int rowNumber = matrix.getRowIndex(item);

                if ( rowNumber == index ) {
                    listToReturn.add((TextField) item);
                }
            }
        }

        return listToReturn;
    }

    private void fillRandomNumbers(GridPane matrix) {
        if ( matrix != null ) {
            Random random = new Random();

            matrix.getChildren().forEach(item -> {
                if ( item instanceof TextField ) {
                    ((TextField) item).setText(String.valueOf(random.nextInt(99) - 25));
                }
            });
        }
    }

    private GridPane generateMatrix(int rows, int columns) {
        GridPane gridPane = new GridPane();
        gridPane.setGridLinesVisible(true);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                gridPane.add(createEmptyTextField(), j, i);
            }
        }

        /* Alternative of margin */
        gridPane.setLayoutX(50);
        gridPane.setLayoutY(columnsTextField.getLayoutY() + 70);

        return gridPane;
    }

    private TextField createEmptyTextField() {
        TextField textField = new TextField();
        textField.setMaxWidth(50);
        textField.setAlignment(Pos.BASELINE_CENTER);
        textField.setEditable(false);

        return textField;
    }

    private Label createEmptyLabel() {
        return new Label();
    }

    private void generateErrorAlert(String message) {
        alert.setAlertType(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void generateInfoAlert(String message) {
        alert.setAlertType(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.showAndWait();
    }
}