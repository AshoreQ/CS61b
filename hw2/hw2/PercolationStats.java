package hw2;

import edu.princeton.cs.introcs.StdRandom;
import edu.princeton.cs.introcs.StdStats;

public class PercolationStats {
    private double[] thresholds;
    private int T;


    public PercolationStats(int N, int T, PercolationFactory pf) {
        this.T = T;
        if (N <= 0 || T <= 0) {
            throw new IllegalArgumentException("Please enter valid arguments !");
        }

        thresholds = new double[N];
        int x, y;

        for (int i = 0; i < T; ++i) {
            Percolation p = pf.make(N);
            while (!p.percolates()) {
                x = StdRandom.uniform(0, N);
                y = StdRandom.uniform(0, N);
                if (!p.isOpen(x, y)) {
                    p.open(x, y);
                }
            }
            thresholds[i] = (double) p.numberOfOpenSites() / (N * N);
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
