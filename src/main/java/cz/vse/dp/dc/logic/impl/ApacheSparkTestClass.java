package cz.vse.dp.dc.logic.impl;

import cz.vse.dp.dc.logic.Cluster;
import cz.vse.dp.dc.logic.PointEx;
import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.linalg.Matrix;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.linalg.distributed.RowMatrix;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ApacheSparkTestClass {

    public void init() {
        // Initialize Spark
        SparkConf conf = new SparkConf().setAppName("PCA Example").setMaster("local[2]").set("spark.executor.memory", "1g");
        SparkContext sc = new SparkContext(conf);
        JavaSparkContext jsc = JavaSparkContext.fromSparkContext(sc);
        List<Vector> data = Arrays.asList(
                Vectors.sparse(5, new int[]{1, 3}, new double[]{1.0, 7.0}),
                Vectors.dense(2.0, 0.0, 3.0, 4.0, 5.0),
                Vectors.dense(4.0, 0.0, 0.0, 6.0, 7.0)
        );

        JavaRDD<Vector> rows = jsc.parallelize(data);

        // Create a RowMatrix from JavaRDD<Vector>.
        RowMatrix mat = new RowMatrix(rows.rdd());

        // Compute the top 4 principal components.
        // Principal components are stored in a local dense matrix.
        Matrix pc = mat.computePrincipalComponents(4);

        // Project the rows to the linear space spanned by the top 4 principal components.
        RowMatrix projected = mat.multiply(pc);
        Vector[] collectPartitions = (Vector[]) projected.rows().collect();

        jsc.stop();
    }

    /**
     * Uses Apache Spark to perform PCA (Principle component analysis)
     * to reduce dimensions without data compression
     *
     * @param cluster starting set of coordinates
     * @return transformed set of coordinates
     */

    public Cluster reduceDimensions(Cluster cluster) {
        // if dimension is 2D or less, do not use PCA
        int DIMENSION_COUNT = 2;
        if (cluster.getPoints().get(0).getDimension() <= DIMENSION_COUNT) {
            return cluster;
        }

        // Initialize Spark
        SparkConf conf = new SparkConf().setAppName("PCA Example").setMaster("local[2]").set("spark.executor.memory", "1g");
        SparkContext sc = new SparkContext(conf);
        JavaSparkContext jsc = JavaSparkContext.fromSparkContext(sc);

        // Convert cluster data to vectors
        List<Vector> data = new ArrayList<>();
        for (int i = 0; i < cluster.getPoints().size(); i++) {
            List<Double> listCoords = cluster.getPoints().get(i).getCoordinates();
            double[] arrayCoords = listCoords.stream().mapToDouble(value -> value).toArray();
            data.add(Vectors.dense(arrayCoords));
        }

        JavaRDD<Vector> rows = jsc.parallelize(data);

        // Create a RowMatrix from JavaRDD<Vector>.
        RowMatrix mat = new RowMatrix(rows.rdd());

        // Compute the top 2 principal components.
        // Principal components are stored in a local dense matrix.
        Matrix pc = mat.computePrincipalComponents(DIMENSION_COUNT);

        // Project the rows to the linear space spanned by the top 2 principal components.
        RowMatrix projected = mat.multiply(pc);


        Vector[] collectPartitions = (Vector[]) projected.rows().collect();
        Cluster pcaCluster = new Cluster();
        List<PointEx> points = new ArrayList<>();
        System.out.println("Projected vector of principal component:");
        for (Vector vector : collectPartitions) {
            // Log transformed coordinates
            System.out.println("\t" + vector);

            // Create new transformed cluster with correct dimensions
            double[] vectorArray = vector.toArray();
            List<Double> doubleList = new ArrayList<>();
            for (double vectorElement : vectorArray) {
                doubleList.add(vectorElement);
            }
            PointEx pointEx = new PointEx(DIMENSION_COUNT, doubleList);
            points.add(pointEx);
        }
        pcaCluster.setPoints(points);
        jsc.stop();
        return pcaCluster;
    }
}
