/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.vse.dp.cluster.dc.ui;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 *
 * @author David Červenka
 */
public class Main extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        MainWindow.openWindow(primaryStage);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
