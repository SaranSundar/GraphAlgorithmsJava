package ShortestPath;

import java.util.*;

public class Graph {

    public HashMap<Integer, Node> nodes;

    public Graph() {
        this.nodes = new HashMap<>();
    }

    @Override
    public String toString() {
        String top = "Nodes: (Name, Weight)\n";
        StringBuilder bottom = new StringBuilder();
        for (Node n : nodes.values()) {
            bottom.append(n.toString());
        }
        return top + bottom;
    }

    public void addNode(int a, int b, int weight) {
        Node nodeA;
        Node nodeB;
        nodeA = nodes.getOrDefault(a, new Node(a));
        nodeB = nodes.getOrDefault(b, new Node(b));
        nodeA.addNeighbor(nodeB, weight);
        nodes.put(a, nodeA);
        nodes.put(b, nodeB);
    }

    public void computeDijkstras(Node start) {
        Map<Node, Integer> totalCosts = new HashMap<>(); // Shows running calculation of path weights
        Map<Node, Node> prevNodes = new HashMap<>(); // Shows the previous node taken for each node in the path
        PriorityQueue<Node> minWeightNodes = new PriorityQueue<>(new NodeComparator()); // Used to get next smallest weight node
        Set<Node> visited = new HashSet<>();

        totalCosts.put(start, 0); // Set start node to cost of 0
        start.weight = 0;
        minWeightNodes.add(start);

        // Set all the other nodes initial weight to infinity
        for (Node node : nodes.values()) {
            if (node != start) {
                totalCosts.put(node, Integer.MAX_VALUE);
                node.weight = Integer.MAX_VALUE;
            }
        }

        // While Nodes to visit
        while (!minWeightNodes.isEmpty()) {
            // Get smallest weighted node each time
            Node minWeight = minWeightNodes.poll();
            // Visit all neighbors of each node
            for (Node neighbor : minWeight.neighbors.keySet()) {
                if (!visited.contains(neighbor)) {
                    // Calculate path weight for each node
                    int currentWeight = totalCosts.get(minWeight);
                    int neighborWeight = minWeight.neighbors.get(neighbor);
                    int path = currentWeight + neighborWeight;
                    // If path weight less then what's there, replace it.
                    if (path < totalCosts.get(neighbor)) {
                        totalCosts.put(neighbor, path);
                        prevNodes.put(neighbor, minWeight);
                        minWeightNodes.remove(neighbor);
                        neighbor.weight = path;
                        minWeightNodes.add(neighbor);
                    }
                }
            }
            visited.add(minWeight);
        }

        //You have totalCosts, and prevNodes;
        for (Map.Entry<Node, Integer> pair : totalCosts.entrySet()) {
            System.out.println("Key: " + pair.getKey().key + ", Weight: " + pair.getValue());
        }

    }

    static class NodeComparator implements Comparator<Node> {

        @Override
        public int compare(Node o1, Node o2) {
            return o2.weight - o1.weight;
        }
    }

    static class Node {
        Map<Node, Integer> neighbors;
        int key;
        int weight;

        public Node(int key) {
            this.key = key;
            neighbors = new HashMap<>();
        }

        public void addNeighbor(Node b, int weight) {
            neighbors.put(b, weight);
        }

        @Override
        public String toString() {
            String top = "Key: " + key + "\nNeighbors: ";
            StringBuilder bottom = new StringBuilder();
            for (Map.Entry<Node, Integer> pair : neighbors.entrySet()) {
                bottom.append("(").append(pair.getKey().key).append(",").append(pair.getValue()).append("), ");
            }
            return top + bottom + "\n";
        }
    }

}
