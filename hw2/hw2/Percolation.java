package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private int n;
    private int size = 0;
    private boolean[][] sites;
    private WeightedQuickUnionUF set;

    private int xyTo1D(int x, int y) {
        return x * n + y;
    }

    public Percolation(int N) {
        if (N <= 0) {
            throw new IllegalArgumentException("N < 0");
        }

        n = N;
        set = new WeightedQuickUnionUF(n * n + 2);
        sites = new boolean[n][n];

        for (int i = 0; i < n; ++i) {
            set.union(n * n, i);
            for (int j = 0; j < n; ++j) {
                sites[i][j] = false;
                if (i == n - 1) {
                    set.union(n * n + 1, xyTo1D(i, j));
                }
            }
        }
    }

    public void open(int row, int col) {
        if (row < 0 || col < 0 || row >= n || col >= n) {
            throw new IndexOutOfBoundsException("row:" + row + " col:" + col);
        }

        if (row >= 1 && row <= n - 2) {
            if (sites[row - 1][col]) {
                set.union(xyTo1D(row, col), xyTo1D(row - 1, col));
            }
            if (sites[row + 1][col]) {
                set.union(xyTo1D(row, col), xyTo1D(row + 1, col));
            }
        } else {
            if (row == 0 && (row + 1 <= n - 1) && sites[row + 1][col]) {
                set.union(xyTo1D(row, col), xyTo1D(row + 1, col));
            }
            if (row == n - 1 && (row - 1 >= 0) && sites[row - 1][col]) {
                set.union(xyTo1D(row, col), xyTo1D(row - 1, col));
            }
        }

        if (col >= 1 && col <= n - 2) {
            if (sites[row][col - 1]) {
                set.union(xyTo1D(row, col), xyTo1D(row, col - 1));
            }
            if (sites[row][col + 1]) {
                set.union(xyTo1D(row, col), xyTo1D(row, col + 1));
            }
        } else {
            if (col == 0 && (col + 1 <= n - 1) && sites[row][col + 1]) {
                set.union(xyTo1D(row, col), xyTo1D(row, col + 1));
            }
            if (col == n - 1 && (col - 1 >= 0) && sites[row][col - 1]) {
                set.union(xyTo1D(row, col), xyTo1D(row, col - 1));
            }
        }
        if (!isOpen(row, col)) {
            sites[row][col] = true;
            size += 1;
        }
    }

    public boolean isOpen(int row, int col) {
        if (row < 0 || col < 0 || row >= n || col >= n) {
            throw new IndexOutOfBoundsException("row:" + row + " col:" + col);
        }
        return sites[row][col];
    }

    public boolean isFull(int row, int col) {
        if (row < 0 || col < 0 || row >= n || col >= n) {
            throw new IndexOutOfBoundsException("row:" + row + " col:" + col);
        }
        int pos = xyTo1D(row, col);
        return sites[row][col] && set.connected(pos, n * n);
    }

    public int numberOfOpenSites() {
        return size;
    }

    public boolean percolates() {
        if (n == 1) {
            return false;
        }
        return set.connected(n * n, n * n + 1);
    }

    public static void main(String[] args) {
        /*
        PercolationFactory pf = new PercolationFactory();
        PercolationStats test = new PercolationStats(200, 100, pf);
        double low = test.confidenceLow(), hight = test.confidenceHigh();
        System.out.println("[" + low + ", " + hight + "]");
        System.out.println(test.stddev()); */
    }
}
