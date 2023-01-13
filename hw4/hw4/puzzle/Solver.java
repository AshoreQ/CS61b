package hw4.puzzle;

import edu.princeton.cs.algs4.MinPQ;

import java.util.Map;
import java.util.List;
import java.util.LinkedList;
import java.util.HashMap;

public class Solver {
    private class Node implements Comparable<Node> {
        private WorldState world;
        private int move;
        private int prirority;
        private Node prev;

        Node(WorldState w, int m, int p, Node n) {
            this.world = w;
            this.move = m;
            this.prirority = p;
            this.prev = n;
        }

        @Override
        public int compareTo(Node obj) {
            return this.prirority - obj.prirority;
        }
    }

    MinPQ<Node> minPQ = new MinPQ<>();
    Map<WorldState, Integer> map = new HashMap<>();
    Node finalNode;
    boolean isGoal = false;

    public Solver(WorldState initial) {
        Node intialNode = new Node(initial, 0, 0, null);
        minPQ.insert(intialNode);

        while (!isGoal) {
            Node node = minPQ.delMin();
            if (node.world.isGoal()) {
                finalNode = node;
                isGoal = true;
                break;
            }
            for (WorldState w : node.world.neighbors()) {
                if (node.prev != null && w.equals(node.prev.world)) {
                    continue;
                }
                int estimate;
                int movement = node.move + 1;
                if (map.containsKey(w)) {
                    estimate = map.get(w);
                } else {
                    estimate = w.estimatedDistanceToGoal();
                    map.put(w, estimate);
                }
                minPQ.insert(new Node(w, movement, movement + estimate, node));
            }
        }
    }

    public int moves() {
        return finalNode.move;
    }

    public Iterable<WorldState> solution() {
        List<WorldState> sol = new LinkedList<>();
        Node node = finalNode;
        while (node != null) {
            sol.add(0, node.world);
            node = node.prev;
        }
        return sol;
    }
}
