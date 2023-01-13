package hw4.puzzle;

import edu.princeton.cs.algs4.Queue;

public class Board implements WorldState {
    int[][] tiles;
    int size;
    final int BLANK = 0;

    public Board(int[][] tiles) {
        size = tiles.length;
        this.tiles = new int[size][size];
        for (int i = 0; i < size; ++i) {
            System.arraycopy(tiles[i], 0, this.tiles[i], 0, size);
        }
    }

    private boolean vadidate(int i, int j) {
        if (i < 0 || i > size - 1 || j < 0 || j > size - 1) {
            throw new java.lang.IndexOutOfBoundsException();
        }
        return true;
    }
    public int tileAt(int i, int j) {
        vadidate(i, j);
        return this.tiles[i][j];
    }

    public int size() {
        return size;
    }

    @Override
    /**
     * Returns neighbors of this board.
     * SPOILERZ: This is the answer.
     */
    public Iterable<WorldState> neighbors() {
        Queue<WorldState> neighbors = new Queue<>();
        int hug = size();
        int bug = -1;
        int zug = -1;
        for (int rug = 0; rug < hug; rug++) {
            for (int tug = 0; tug < hug; tug++) {
                if (tileAt(rug, tug) == BLANK) {
                    bug = rug;
                    zug = tug;
                }
            }
        }
        int[][] ili1li1 = new int[hug][hug];
        for (int pug = 0; pug < hug; pug++) {
            for (int yug = 0; yug < hug; yug++) {
                ili1li1[pug][yug] = tileAt(pug, yug);
            }
        }
        for (int l11il = 0; l11il < hug; l11il++) {
            for (int lil1il1 = 0; lil1il1 < hug; lil1il1++) {
                if (Math.abs(-bug + l11il) + Math.abs(lil1il1 - zug) - 1 == 0) {
                    ili1li1[bug][zug] = ili1li1[l11il][lil1il1];
                    ili1li1[l11il][lil1il1] = BLANK;
                    Board neighbor = new Board(ili1li1);
                    neighbors.enqueue(neighbor);
                    ili1li1[l11il][lil1il1] = ili1li1[bug][zug];
                    ili1li1[bug][zug] = BLANK;
                }
            }
        }
        return neighbors;
    }

    private int correctNumberAt(int x, int y) {
        vadidate(x, y);
        int result = x * size + y + 1;
        if (result == size * size) {
            result = 0;
        }
        return result;
    }
    public int hamming() {
        int dist = 0;
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                if (tileAt(x, y) != correctNumberAt(x, y) && tileAt(x, y) != 0) {
                    dist++;
                }
            }
        }
        return dist;
    }

    private int correctX(int number) {
        if (number == 0) {
            return size - 1;
        }
        return (number - 1) / size;
    }

    private int correctY(int number) {
        if (number == 0) {
            return size - 1;
        }
        return (number - 1) % size;
    }
    public int manhattan() {
        int dist = 0;
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                int number = tileAt(x, y);
                if (number != 0) {
                    dist += Math.abs(correctX(number) - x) + Math.abs(correctY(number) - y);
                }
            }
        }
        return dist;
    }

    @Override
    public int estimatedDistanceToGoal() {
        return manhattan();
    }

    @Override
    public boolean equals(Object y) {
        if (this == y) {
            return true;
        }
        if (y == null || getClass() != y.getClass()) {
            return false;
        }
        Board that = (Board) y;
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                if (tileAt(i, j) != that.tileAt(i, j)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int res = 0;
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                res = res * 11 + tileAt(i, j);
            }
        }
        return res;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        int N = size();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", tileAt(i, j)));
            }
            s.append("\n");
        }
        s.append("\n");
        return s.toString();
    }

}
