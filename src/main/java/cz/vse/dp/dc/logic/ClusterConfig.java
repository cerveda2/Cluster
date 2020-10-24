/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.vse.dp.dc.logic;

import cz.vse.dp.dc.logic.impl.Calculation;
import cz.vse.dp.dc.logic.intf.ICalculation;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.IntStream;

/**
 * @author David Červenka
 */
public class ClusterConfig {

    public int clusterCount;
    public int dimensions;
    public double scale;
    public double distance;
    public List<PointEx> centers;
    public List<PointEx> prevCenters;
    public List<PointEx> prevPrevCenters;
    public ICalculation metric;
    public double clusterPerimeter;
    public List<Cluster> clusters;
    public int clusterSize;
    public DistributionType distributionType;
    private Double distanceToPreviousPoint;

    public ClusterConfig(int clusterSize, int clusterCount, double clusterPerimeter, double distance, double scale, int dimensions, DistributionType distributionType) {
        this.clusterCount = clusterCount;
        this.dimensions = dimensions;
        this.scale = scale;
        this.distance = distance;
        this.clusterPerimeter = clusterPerimeter;
        this.clusterSize = clusterSize;
        this.distributionType = distributionType;

        centers = new ArrayList<>();
        prevCenters = new ArrayList<>();
        prevPrevCenters = new ArrayList<>();
        metric = new Calculation();
    }

    public void generate() {

        centers = new ArrayList<>();
        prevPrevCenters = new ArrayList<>();
        prevCenters = new ArrayList<>();
        final PointEx[] candidate = new PointEx[1];

        int attempts = 0;

        // Calculate distance of a third side of a triangle
        distanceToPreviousPoint = clusterCount > 2 ? metric.distanceToPreviousPoint(getActualDistance(), metric.calculateAngle(clusterCount)) : null;

        for (int i = 0; i < clusterCount; i++) {
            do {
                candidate[0] = PointEx
                        .createRandom(dimensions, distributionType)
                        .multiply(scale);
            } while (attempts++ < 1000000
                    && !(prevCenters.stream().allMatch(getMatch(candidate))
                        && prevPrevCenters.stream().allMatch(getMatch(candidate, i))));

            if (attempts >= 1000000) {
                System.out.println("Attempts: " + attempts);
                throw new IllegalArgumentException("Unable to find candidate after 1000 attempts.");
            }
            attempts = 0;
            centers.add(candidate[0]);

            // Update list of previous centers
            prevCenters.clear();
            prevCenters.add(candidate[0]);

            // Update list of previous previous centers
            prevPrevCenters.clear();
            if (i >= 1) {
                prevPrevCenters.add(centers.get(centers.size() - 2));
            } else {
                prevPrevCenters.add(prevCenters.get(0));
            }
        }

        clusters = new ArrayList<>();

        IntStream.range(0, clusterCount)
                .parallel()
                .forEach(i -> clusters.add(generateCluster(centers.get(i))));
    }


    private Cluster generateCluster(PointEx center) {
        Cluster result = new Cluster();
        result.setPoints(new ArrayList<>());


        for (int i = 0; i < clusterSize; i++) {
            double clusterScale = 2 * clusterPerimeter;
            PointEx multiply = PointEx.createRandomSpherical(dimensions, distributionType).multiply(clusterScale);
            PointEx plus = multiply.plus(center);
            result.getPoints().add(plus);
        }

        return result;
    }

    /**
     * Method to determine whether two points next to each other are at a given distance.
     * @param candidate comparing point
     * @return if the two points are at given distance
     */
    private Predicate<PointEx> getMatch(final PointEx[] candidate) {
        return doubles -> metric.calculateEuclideanDistance(doubles, candidate[0]) > getActualDistance() - 1 && metric.calculateEuclideanDistance(doubles, candidate[0]) < getActualDistance() + 1;
    }

    /**
     * Method to determine whether two points with one in between them are at a given distance.
     * They should make a triangle from a calculated distance by a calculated angle depending on a number of clusters.
     * @param candidate comparing point
     * @param numberOfCluster where we are in the loop
     * @return if the two points are at given distance
     */
    private Predicate<PointEx> getMatch(final PointEx[] candidate, int numberOfCluster) {
        double usedPoint = numberOfCluster > 1 && distanceToPreviousPoint != null ? distanceToPreviousPoint : getActualDistance();
        return doubles -> metric.calculateEuclideanDistance(doubles, candidate[0]) > usedPoint - 1 && metric.calculateEuclideanDistance(doubles, candidate[0]) < usedPoint + 1;
    }

    /**
     * Calculate distance between two centers from twice the perimeter plus distance given.
     * @return distance between two centers
     */
    private double getActualDistance() {
        return distance + clusterPerimeter * 2;
    }

    public List<PointEx> getCenters() {
        return centers;
    }

    public List<Cluster> getClusters() {
        return clusters;
    }


}
