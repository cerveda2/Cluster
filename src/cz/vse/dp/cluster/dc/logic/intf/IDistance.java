/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.vse.dp.cluster.dc.logic.intf;

import cz.vse.dp.cluster.dc.logic.PointEx;

/**
 * @author David Červenka
 */
public interface IDistance {

    double calculateDistance(PointEx x, PointEx y);

}
