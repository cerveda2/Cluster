/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.vse.dp.dc.ui;

import cz.vse.dp.dc.logic.*;
import cz.vse.dp.dc.logic.impl.ApacheSparkTestClass;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;

/**
 * @author David Červenka
 */
public class MainWindow {

    public static TextField sizeArea = new TextField();
    public static TextField countArea = new TextField();
    public static TextField perimeterArea = new TextField();
    public static TextField distanceArea = new TextField();
    public static TextField scaleArea = new TextField();
    public static TextField dimensionsArea = new TextField();
    public static TextArea centersArea = new TextArea();
    public static TextArea pointsArea = new TextArea();
    public static TextField iterationsArea = new TextField();
    static Label sizeLbl = new Label("Velikost shluku:");
    static Label countLbl = new Label("Počet shluků:");
    static Label perimeterLbl = new Label("Průměr shluku:");
    static Label distanceLbl = new Label("Vzdálenost:");
    static Label scaleLbl = new Label("Škála:");
    static Label dimensionsLbl = new Label("Dimenze:");
    static Label centersLbl = new Label("Středy:");
    static Label pointsLbl = new Label("Body:");
    static Label distributionLbl = new Label("Rozdělení:");
    static Label outputLbl = new Label("Výstupní soubor:");
    static Label iterationsLbl = new Label("Počet iterací:");
    static Canvas canvas = new Canvas();

    public static void openWindow(Stage stage) {

        GridPane outerGrid = new GridPane();

        ColumnConstraints column1 = new ColumnConstraints(320);
        ColumnConstraints column2 = new ColumnConstraints();
        outerGrid.getColumnConstraints().addAll(column1, column2);

        canvas.setWidth(630);
        canvas.setHeight(630);

        outerGrid.add(canvas, 1, 0);

        //drawing components to the inner grid
        setInnerGrid(outerGrid);

        StackPane root = new StackPane();
        root.getChildren().add(outerGrid);

        Scene scene = new Scene(root, 950, 630);

        stage.setMinWidth(967);
        stage.setMinHeight(670);

        stage.setTitle("Generátor shluků");
        stage.setScene(scene);
        stage.show();
    }

    private static void setInnerGrid(GridPane grid) {
        GridPane innerGrid = new GridPane();

        innerGrid.setHgap(10);
        innerGrid.setVgap(10);
        innerGrid.setPadding(new Insets(25, 25, 25, 25));

        ColumnConstraints column1 = new ColumnConstraints(150);
        ColumnConstraints column2 = new ColumnConstraints();
        innerGrid.getColumnConstraints().addAll(column1, column2);

        final ToggleGroup group1 = new ToggleGroup();

        RadioButton rb1 = new RadioButton("Normální");
        rb1.setToggleGroup(group1);
        rb1.setSelected(true);

        RadioButton rb2 = new RadioButton("Rovnoměrné");
        rb2.setToggleGroup(group1);

        final ToggleGroup group2 = new ToggleGroup();

        RadioButton rb3 = new RadioButton("TXT");
        rb3.setToggleGroup(group2);
        rb3.setSelected(true);

        RadioButton rb4 = new RadioButton("CSV");
        rb4.setToggleGroup(group2);

        innerGrid.add(sizeLbl, 0, 0);
        innerGrid.add(countLbl, 0, 1);
        innerGrid.add(perimeterLbl, 0, 2);
        innerGrid.add(distanceLbl, 0, 3);
        innerGrid.add(scaleLbl, 0, 4);
        innerGrid.add(dimensionsLbl, 0, 5);
        innerGrid.add(distributionLbl, 0, 6, 1, 2);
        innerGrid.add(outputLbl, 0, 8, 1, 2);
        innerGrid.add(iterationsLbl, 0, 10);
        innerGrid.add(centersLbl, 0, 12);
        innerGrid.add(pointsLbl, 0, 14);

        innerGrid.add(sizeArea, 1, 0);
        innerGrid.add(countArea, 1, 1);
        innerGrid.add(perimeterArea, 1, 2);
        innerGrid.add(distanceArea, 1, 3);
        innerGrid.add(scaleArea, 1, 4);
        innerGrid.add(dimensionsArea, 1, 5);
        innerGrid.add(iterationsArea, 1, 10);
        innerGrid.add(centersArea, 0, 13, 2, 1);
        innerGrid.add(pointsArea, 0, 15, 2, 1);

        innerGrid.add(rb1, 1, 6);
        innerGrid.add(rb2, 1, 7);
        innerGrid.add(rb3, 1, 8);
        innerGrid.add(rb4, 1, 9);

        centersArea.setEditable(false);
        pointsArea.setEditable(false);

        Button generateBtn = new Button("Generovat");
        generateBtn.setPrefWidth(270);
        innerGrid.add(generateBtn, 0, 11, 2, 1);

        sizeArea.setText("200");
        countArea.setText("2");
        perimeterArea.setText("200");
        distanceArea.setText("500");
        scaleArea.setText("1000");
        dimensionsArea.setText("2");
        iterationsArea.setText("1");

        // force the field to be numeric only

        addListener(sizeArea, countArea, perimeterArea, distanceArea, scaleArea, dimensionsArea, iterationsArea);

        generateBtn.setOnAction(event -> {

            if (distanceArea.getText().equals("") || scaleArea.getText().equals("") || perimeterArea.getText().equals("") ||
                    countArea.getText().equals("") || sizeArea.getText().equals("") || dimensionsArea.getText().equals("") ||
                    iterationsArea.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Musíte vyplnit všechny potřebné parametry.");
                return;
            }

            double distance = Double.parseDouble(distanceArea.getText());
            double scale = Double.parseDouble(scaleArea.getText());
            double perimeter = Double.parseDouble(perimeterArea.getText());
            int count = Integer.parseInt(countArea.getText());
            int size = Integer.parseInt(sizeArea.getText());
            int dimensions = Integer.parseInt(dimensionsArea.getText());
            RadioButton selectedToggle = (RadioButton) group1.getSelectedToggle();
            String distributionText = selectedToggle.getText();
            DistributionType distributionType = DistributionType.valueOf(distributionText.toUpperCase());

            RadioButton selectedToggle1 = (RadioButton) group2.getSelectedToggle();
            String outputFile = selectedToggle1.getText().toLowerCase();

            ClusterConfig config = null;

            for (int i = 0; i < Integer.parseInt(iterationsArea.getText()); i++) {
                StringBuilder sb = new StringBuilder();
                config = new ClusterConfig(size, count, perimeter, distance, scale, dimensions, distributionType);
                try {
                    config.generate();
                } catch (IllegalArgumentException e) {
                    JOptionPane.showMessageDialog(null, "Nepodařilo se vygenerovat shluky s touto konfigurací.",
                            "Chyba", JOptionPane.ERROR_MESSAGE);
                }

                /*for (PointEx center : config.getCenters()) {
                    sb.append(center.toString()).append("\r\n");
                }*/

                for (Cluster cluster : config.getClusters()) {
                    for (PointEx point : cluster.getPoints()) {
                        sb.append(point.toString()).append("\r\n");
                    }
                }

                String result = sb.toString();
                try {
                    Tools.saveToFile(result, outputFile, i + 1);
                } catch (FileNotFoundException ex) {
                    System.out.print("Exception: " + ex);
                } catch (IOException ex) {
                    System.out.print("Exception: " + ex);
                }

            }

            if (config != null) {
                drawGraph(config);
            }

            centersArea.clear();
            for (PointEx center : config.getCenters()) {
                centersArea.appendText(center.toString() + "\n");
            }
            String centers = centersArea.getText();
            int centersCount = centersArea.getLength();
            centersArea.setText(centers.substring(0, centersCount - 2));

            pointsArea.clear();
            for (Cluster cluster : config.getClusters()) {
                for (PointEx point : cluster.getPoints()) {
                    pointsArea.appendText(point.toString() + "\n");
                }
            }
            String points = pointsArea.getText();
            int pointsCount = pointsArea.getLength();
            pointsArea.setText(points.substring(0, pointsCount - 2));
        });
        grid.add(innerGrid, 0, 0);
    }

    private static void addListener (final TextField... textFields) {
        if (textFields == null || textFields.length == 0) {
            return;
        }

        for (TextField tf : textFields) {
            tf.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
                validate(tf, newValue);
            });
        }
    }

    private static void validate(TextField textField, String newValue) {
        if (!newValue.matches("\\d*")) {
            textField.setText(newValue.replaceAll("[^\\d]", ""));
        }
        if (newValue.length() > 0 && newValue.startsWith("0")) {
            textField.setText(newValue.replaceFirst("0", ""));
        }
    }

    private static void drawGraph(ClusterConfig config) {
        double middlePoint = canvas.getWidth() / 2;
        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        gc.setFill(javafx.scene.paint.Color.WHITE);
        gc.setStroke(javafx.scene.paint.Color.BLACK);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.strokeLine(0, 0, 630, 0);
        gc.strokeLine(630, 0, 630, 630);
        gc.strokeLine(630, 630, 0, 630);
        gc.strokeLine(0, 630, 0, 0);

        gc.strokeLine(middlePoint, 30, middlePoint, 600);
        gc.strokeLine(30, middlePoint, 600, middlePoint);

        double scale = config.scale + 2 * config.clusterPerimeter;
        double dx = config.clusterPerimeter;

        for (Cluster cluster : config.getClusters()) {

            Random rand = new Random();
            float r = rand.nextFloat();
            float g = rand.nextFloat();
            float b = rand.nextFloat();

            Color randomColor = new Color(r, g, b, 1);

            gc.setStroke(randomColor);

            ApacheSparkTestClass apacheSpark = new ApacheSparkTestClass();
            Cluster pcaCluster = apacheSpark.reduceDimensions(cluster);

            for (PointEx point : pcaCluster.getPoints()) {
                double pointX = (dx + point.getCords().get(0) + config.scale / 2) * canvas.getWidth() / scale;
                double pointY = canvas.getHeight() - (dx + point.getCords().get(1) + config.scale / 2) * canvas.getHeight() / scale;
                System.out.println("PointX " + pointX + ", PointY " + pointY);
                gc.strokeOval(pointX, pointY, 2, 2);
            }
        }
    }
}
