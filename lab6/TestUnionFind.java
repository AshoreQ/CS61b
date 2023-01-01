public class TestUnionFind {
    public static void main(String[] args) {
        UnionFind u1 = new UnionFind(4);
        u1.union(0, 1);
        u1.union(0, 2);
        u1.union(1, 3);
        System.out.println(u1.find(3));
        System.out.println(u1.find(0));
        System.out.println(u1.find(1));
        System.out.println(u1.sizeOf(0));
        UnionFind u2 = new UnionFind(10);
        u2.union(0, 1);
        u2.union(0, 2);
        u2.union(6, 7);
        u2.union(0, 6);
        System.out.println(u2.sizeOf(0));
    }
}
