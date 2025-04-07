package com.orebit.mod;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Set;

public class AStarPathfinding {

    static class Node {
        int x, y;
        double gCost, hCost, fCost;
        Node parent;

        public Node(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public void calculateCosts(Node target, double gCostFromStart) {
            this.gCost = gCostFromStart;
            this.hCost = Math.sqrt(Math.pow(target.x - this.x, 2) + Math.pow(target.y - this.y, 2));
            this.fCost = this.gCost + this.hCost;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Node node = (Node) obj;
            return x == node.x && y == node.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

    public List<Node> findPath(int[][] grid, Node start, Node target) {
        PriorityQueue<Node> openSet = new PriorityQueue<>(Comparator.comparingDouble(n -> n.fCost));
        Set<Node> closedSet = new HashSet<>();

        start.calculateCosts(target, 0);
        openSet.add(start);

        while (!openSet.isEmpty()) {
            Node current = openSet.poll();

            if (current.equals(target)) {
                return reconstructPath(current);
            }

            closedSet.add(current);

            for (Node neighbor : getNeighbors(grid, current)) {
                if (closedSet.contains(neighbor)) continue;

                double tentativeGCost = current.gCost + 1; // Assuming uniform cost for simplicity
                if (!openSet.contains(neighbor) || tentativeGCost < neighbor.gCost) {
                    neighbor.calculateCosts(target, tentativeGCost);
                    neighbor.parent = current;

                    if (!openSet.contains(neighbor)) {
                        openSet.add(neighbor);
                    }
                }
            }
        }

        return Collections.emptyList(); // No path found
    }

    private List<Node> getNeighbors(int[][] grid, Node node) {
        List<Node> neighbors = new ArrayList<>();
        int[] dx = {-1, 1, 0, 0};
        int[] dy = {0, 0, -1, 1};

        for (int i = 0; i < 4; i++) {
            int newX = node.x + dx[i];
            int newY = node.y + dy[i];

            if (newX >= 0 && newX < grid.length && newY >= 0 && newY < grid[0].length && grid[newX][newY] == 0) {
                neighbors.add(new Node(newX, newY));
            }
        }

        return neighbors;
    }

    private List<Node> reconstructPath(Node target) {
        List<Node> path = new ArrayList<>();
        Node current = target;

        while (current != null) {
            path.add(current);
            current = current.parent;
        }

        Collections.reverse(path);
        return path;
    }
}