/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.vse.dp.dc.ui;

import cz.vse.dp.dc.logic.impl.ApacheSparkTestClass;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * @author David Červenka
 */
public class Main extends Application {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        MainWindow.openWindow(primaryStage);
        ApacheSparkTestClass apacheSparkTestClass = new ApacheSparkTestClass();
        apacheSparkTestClass.init();
    }

}
