/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.vse.dp.dc.logic.impl;

import cz.vse.dp.dc.logic.PointEx;
import cz.vse.dp.dc.logic.intf.IDistance;

/**
 * @author David Červenka
 */
public class EuclideanDistance implements IDistance {

    @Override
    public double calculateDistance(PointEx x, PointEx y) {

        int minSize = Math.min(x.getDimension(), y.getDimension());

        double diff_square_sum = 0.0;
        for (int i = 0; i < minSize; i++) {

            diff_square_sum += Math.pow(x.getCoordinates().get(i) - y.getCoordinates().get(i), 2);
        }
        return Math.sqrt(diff_square_sum);

        /*int minSize = Math.min(x.getDimension(), y.getDimension());


        return IntStream.range(0, minSize)
                .mapToDouble(i -> {
                    Double pointX = x.getCoordinates().get(i);
                    Double pointY = y.getCoordinates().get(i);
                    return Math.pow(pointX - pointY, 2);
                }).sum();*/
    }

}
