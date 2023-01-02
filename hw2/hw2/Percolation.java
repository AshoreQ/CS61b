package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private int n;
    private int size;
    private boolean[][] sites;
    private WeightedQuickUnionUF set;

    private int xyToX(int x, int y) {
        return x * n + y;
    }

    public Percolation(int N) {
        if (N <= 0) {
            throw new IllegalArgumentException("N is illegal, please input again !");
        }

        n = N;
        set = new WeightedQuickUnionUF(n * n + 2);
        sites = new boolean[n][n];

        for (int i = 0; i < n; ++i) {
            set.union(0, i + 1);
            for (int j = 0; j < n; ++j) {
                sites[i][j] = false;
                if (i == n - 1) {
                    set.union(n * n + 1, xyToX(i, j));
                }
            }
        }
    }

    public void open(int row, int col) {
        if (row < 0 || col < 0 || row >= n || col >= n) {
            throw new IndexOutOfBoundsException("Please enter valid arguments !");
        }

        size += 1;
        sites[row][col] = true;

        if (row >= 1 && row <= n - 2) {
            if (sites[row - 1][col]) {
                set.union(xyToX(row, col), xyToX(row - 1, col));
            }
            if (sites[row + 1][col]) {
                set.union(xyToX(row, col), xyToX(row + 1, col));
            }
        } else {
            if (row == 0 && sites[row + 1][col]) {
                set.union(xyToX(row, col), xyToX(row + 1, col));
            }
            if (row == n - 1 && sites[row - 1][col]) {
                set.union(xyToX(row, col), xyToX(row - 1, col));
            }
        }

        if (col >= 1 && col <= n - 2) {
            if (sites[row][col - 1]) {
                set.union(xyToX(row, col), xyToX(row, col - 1));
            }
            if (sites[row][col + 1]) {
                set.union(xyToX(row, col), xyToX(row, col + 1));
            }
        } else {
            if (col == 0 && sites[row][col + 1]) {
                set.union(xyToX(row, col), xyToX(row, col + 1));
            }
            if (col == n - 1 && sites[row][col - 1]) {
                set.union(xyToX(row, col), xyToX(row, col - 1));
            }
        }
    }

    public boolean isOpen(int row, int col) {
        if (row < 0 || col < 0 || row >= n || col >= n) {
            throw new IndexOutOfBoundsException("Please enter valid arguments !");
        }
        return sites[row][col];
    }

    public boolean isFull(int row, int col) {
        int pos = xyToX(row, col);
        if (set.find(pos) == n * n + 1 && !set.connected(pos, 0)) {
            return false;
        }
        return set.connected(set.find(pos), 0);
    }

    public int numberOfOpenSites() {
        return size;
    }

    public boolean percolates() {
        return set.connected(0, n * n + 1);
    }

    public static void main(String[] args) {
        PercolationFactory pf = new PercolationFactory();
        PercolationStats test = new PercolationStats(200, 100, pf);
        double low = test.confidenceLow(), hight = test.confidenceHight();
        System.out.println("[" + low + ", " + hight + "]");

    }
}
