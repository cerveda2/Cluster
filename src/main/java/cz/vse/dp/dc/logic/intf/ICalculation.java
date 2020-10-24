/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.vse.dp.dc.logic.intf;

import cz.vse.dp.dc.logic.PointEx;

/**
 * @author David Červenka
 */
public interface ICalculation {

    double calculateEuclideanDistance(PointEx x, PointEx y);

    int calculateAngle(int clusterCount);

    double distanceToPreviousPoint(double distance, int angle);

}
