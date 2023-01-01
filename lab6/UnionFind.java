public class UnionFind {
    private int N;
    private int[] parent;

    public UnionFind(int n) {
        N = n;
        parent = new int[N];
        for (int i = 0; i < parent.length; i += 1) {
            parent[i] = -1;
        }
    }

    public void validate(int v1) {
        if (v1 >= N) {
            throw new IllegalArgumentException(v1 + "is not a valid index.");
        }
    }

    public int sizeOf(int v1) {
        validate(v1);
        return (-1) * parent[find(v1)];
    }

    public int parent(int v1) {
        validate(v1);
        return parent[v1];
    }

    public boolean connected(int v1, int v2) {
        validate(v1);
        validate(v2);
        return find(v1) == find(v2);
    }

    public void union(int v1, int v2) {
        validate(v1);
        validate(v2);
        int indexOne = find(v1), indexTwo = find(v2);
        if (sizeOf(v1) >= sizeOf(v2)) {
            parent[indexOne] = parent[indexTwo] + parent[indexOne];
            parent[indexTwo] = indexOne;
        } else {
            parent[indexTwo] = parent[indexTwo] + parent[indexOne];
            parent[indexOne] = indexTwo;
        }
    }

    public int find(int v1) {
        validate(v1);
        if (parent[v1] < 0) {
            return v1;
        } else {
            parent[v1] = find(parent[v1]);
            return parent[v1];
        }
    }
}
