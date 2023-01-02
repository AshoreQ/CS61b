package hw2;

import edu.princeton.cs.introcs.StdRandom;
import edu.princeton.cs.introcs.StdStats;

public class PercolationStats {
    private double[] thresholds;
    private int T;


    public PercolationStats(int N, int T, PercolationFactory pf) {
        if (N <= 0 || T <= 0) {
            throw new IllegalArgumentException("Please enter valid arguments !");
        }

        this.T = T;
        this.thresholds = new double[T];
        int x, y;

        for (int i = 0; i < T; ++i) {
            Percolation p = pf.make(N);
            while (!p.percolates()) {
                x = StdRandom.uniform(N);
                y = StdRandom.uniform(N);
                if (!p.isOpen(x, y)) {
                    p.open(x, y);
                }
            }
            this.thresholds[i] = (double) p.numberOfOpenSites() / (N * N);
        }
    }

    public double mean() {
        return StdStats.mean(thresholds);
    }

    public double stddev() {
        return StdStats.stddev(thresholds);
    }

    public double confidenceLow() {
        return mean() - 1.96 * stddev() / Math.sqrt(T);
    }

    public double confidenceHigh() {
        return mean() + 1.96 * stddev() / Math.sqrt(T);
    }

}
