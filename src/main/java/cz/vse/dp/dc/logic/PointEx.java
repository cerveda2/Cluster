/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.vse.dp.dc.logic;

import cz.vse.dp.dc.logic.impl.EuclideanDistance;
import cz.vse.dp.dc.logic.intf.IDistance;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * @author David Červenka
 */
public class PointEx implements Iterable<Double> {

    private static final long seedUniquifier = 8682522807148012L;
    private final List<Double> coordinates;

    public PointEx(int dimension, List<Double> coordinates) {

        if (coordinates.size() != dimension) {
            throw new IllegalArgumentException("Dimension must equal to coordinates size.");
        }

        this.coordinates = new ArrayList<>(coordinates);
    }

    private PointEx(int dimension) {
        this(dimension, false);
    }

    private PointEx(int dimension, boolean isEmptyPoint) {

        if (dimension <= 0) {
            throw new IllegalArgumentException("Dimension must be greater than 0.");
        }

        this.coordinates = new ArrayList<>(dimension);
        if (isEmptyPoint) {
            for (int i = 0; i < dimension; i++) {
                this.coordinates.add(0d);
            }
        }
    }

    private PointEx(PointEx point) {
        this.coordinates = new ArrayList<>(point.getCoordinates());
    }

    public static PointEx createRandom(int dimensions, DistributionType distributionType) {
        PointEx result = new PointEx(dimensions);

        Random rnd = new Random(seedUniquifier + System.nanoTime());
        for (int i = 0; i < dimensions; i++) {
            double randomValue;
            switch (distributionType) {
                case NORMÁLNÍ:
                    // Tato funkce muze vratit teoreticky cokoliv je treba ji osekat. V 70% pripadu vrati hodnotu od -1 do 1 osekame vydelenim 2 a pak cyklem
                    do {
                        randomValue = rnd.nextGaussian() / 2d;
                    } while (Math.abs(randomValue) > 0.5);
                    break;
                case ROVNOMĚRNÉ:
                    randomValue = rnd.nextDouble() - 0.5;
                    break;
                default:
                    throw new IllegalArgumentException("Unknown enum value " + distributionType);

            }
            result.getCoordinates().add(randomValue);
        }

        return result;
    }

    public static PointEx createRandomSpherical(int dimensions, DistributionType distributionType) {
        PointEx candidate;
        // Je treba vytvorit bod ve kterem jsou sourdanice na 0,0 (Stred souradnic)
        PointEx emptyPoint = new PointEx(dimensions, true);
        IDistance norm = new EuclideanDistance();

        do {
            candidate = createRandom(dimensions, distributionType);
        } while (norm.calculateDistance(candidate, emptyPoint) > 0.5);

        return candidate;
    }

    @Override
    @Nonnull
    public Iterator<Double> iterator() {
        if (coordinates != null) {
            return coordinates.iterator();
        } else {
            throw new IllegalArgumentException("Coordinates must not be null");
        }
    }

    public List<Double> getCoordinates() {
        return coordinates;
    }

    public int getDimension() {
        return coordinates.size();
    }

    public PointEx multiply(double scale) {

        List<Double> multipliedNumbers = getCoordinates()
                .stream()
                .map(c -> c * scale)
                .collect(Collectors.toList());

        return new PointEx(this.getDimension(), multipliedNumbers);
    }

    public PointEx plus(PointEx right) {
        PointEx result = new PointEx(this);

        for (int i = 0; i < this.getDimension(); i++) {
            result.getCoordinates().set(i, result.getCoordinates().get(i) + right.getCoordinates().get(i));
        }

        return result;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        coordinates.forEach(t -> builder.append(t).append("; "));

        return builder.toString().replaceAll("\\.", ",");
    }


}
