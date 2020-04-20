/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.vse.dp.cluster.dc.logic;

import cz.vse.dp.cluster.dc.logic.impl.EuclideanDistance;
import cz.vse.dp.cluster.dc.logic.intf.IDistance;

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
    public IDistance metric;
    public double clusterPerimeter;
    public List<Cluster> clusters;
    public int clusterSize;
    public DistributionType distributionType;

    public ClusterConfig(int clusterSize, int clusterCount, double clusterPerimeter, double distance, double scale, int dimensions, DistributionType distributionType) {
        this.clusterCount = clusterCount;
        this.dimensions = dimensions;
        this.scale = scale;
        this.distance = distance;
        this.clusterPerimeter = clusterPerimeter;
        this.clusterSize = clusterSize;
        this.distributionType = distributionType;

        centers = new ArrayList<>();
        metric = new EuclideanDistance();

    }

    public void generate() {

        centers = new ArrayList<>();
        final PointEx[] candidate = new PointEx[1];

        int attempts = 0;

        for (int i = 0; i < clusterCount; i++) {
            do {
                candidate[0] = PointEx
                        .createRandom(dimensions, distributionType)
                        .multiply(scale);
            } while (attempts++ < 1000 && !centers
                    .stream()
                    .allMatch(getMatch(candidate)));

            if (attempts >= 1000) {
                throw new IllegalArgumentException("Unable to find candidate after 1000 attempts.");
            }
            centers.add(candidate[0]);
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

    private Predicate<PointEx> getMatch(final PointEx[] candidate) {
        return p -> metric.calculateDistance(p, candidate[0]) > distance;
    }

    public int getClusterCount() {
        return clusterCount;
    }

    public void setClusterCount(int clusterCount) {
        this.clusterCount = clusterCount;
    }

    public int getDimensions() {
        return dimensions;
    }

    public void setDimensions(int dimensions) {
        this.dimensions = dimensions;
    }

    public double getScale() {
        return scale;
    }

    public void setScale(double scale) {
        this.scale = scale;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public List<PointEx> getCenters() {
        return centers;
    }

    public void setCenters(List<PointEx> centers) {
        this.centers = centers;
    }

    public IDistance getMetric() {
        return metric;
    }

    public void setMetric(IDistance metric) {
        this.metric = metric;
    }

    public double getClusterPerimeter() {
        return clusterPerimeter;
    }

    public void setClusterPerimeter(double clusterPerimeter) {
        this.clusterPerimeter = clusterPerimeter;
    }

    public List<Cluster> getClusters() {
        return clusters;
    }

    public void setClusters(List<Cluster> clusters) {
        this.clusters = clusters;
    }

    public int getClusterSize() {
        return clusterSize;
    }

    public void setClusterSize(int clusterSize) {
        this.clusterSize = clusterSize;
    }


}
