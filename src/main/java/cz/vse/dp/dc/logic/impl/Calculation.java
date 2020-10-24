/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.vse.dp.dc.logic.impl;

import cz.vse.dp.dc.logic.PointEx;
import cz.vse.dp.dc.logic.intf.ICalculation;

/**
 * @author David Červenka
 */
public class Calculation implements ICalculation {

    /**
     * Method to calculate euclidean distance between two points with variable dimensions
     * @param x Point one
     * @param y Point two
     * @return Euclidean distance of two points
     */
    @Override
    public double calculateEuclideanDistance(PointEx x, PointEx y) {

        int minSize = Math.min(x.getDimension(), y.getDimension());

        double diff_square_sum = 0.0;
        for (int i = 0; i < minSize; i++) {

            diff_square_sum += Math.pow(x.getCoordinates().get(i) - y.getCoordinates().get(i), 2);
        }
        return Math.sqrt(diff_square_sum);
    }

    /**
     * Calculates interior angle from a number of sides
     * @param clusterCount number of sides of a polygon
     * @return angle
     */
    @Override
    public int calculateAngle(int clusterCount) {
        return (int) ((1 - 2 / (double) clusterCount) * 180);
    }

    /**
     * Calculates distance in a triangle of a third side
     * @param distance distance of two same sides
     * @param angle angle needed to determine the length of the third side
     * @return third side of a triangle
     */
    @Override
    public double distanceToPreviousPoint(double distance, int angle) {
        return Math.sqrt((Math.pow(distance, 2) + Math.pow(distance, 2)) - (2 * distance * distance * Math.cos(Math.toRadians(angle))));
    }

    /**
     * Method to round calculated distance.
     * @param value actual value
     * @param places number of decimal points
     * @return rounded number
     */
    private double round(double value, int places) {
        double scale = Math.pow(10, places);
        return Math.round(value * scale) / scale;
    }
}
