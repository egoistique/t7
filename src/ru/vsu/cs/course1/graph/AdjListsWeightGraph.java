package ru.vsu.cs.course1.graph;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class AdjListsWeightGraph implements WeightedGraph {

    private class WeightedEdgeTo implements WeightedGraph.WeightedEdgeTo {
        int to;
        double weight;

        public WeightedEdgeTo(int to, double weight) {
            this.to = to;
            this.weight = weight;
        }

        @Override
        public int to() {
            return to;
        }

        @Override
        public double weight() {
            return weight;
        }
    }
    private List<List<WeightedEdgeTo>> vEdjLists = new ArrayList<>();
    private int vCount = 0;
    private int eCount = 0;

    private static Iterable<WeightedEdgeTo> nullIterable = new Iterable<>() {
        @Override
        public Iterator<WeightedEdgeTo> iterator() {
            return new Iterator<WeightedEdgeTo>() {
                @Override
                public boolean hasNext() {
                    return false;
                }

                @Override
                public WeightedEdgeTo next() {
                    return null;
                }
            };
        }
    };

    @Override
    public int vertexCount() {
        return vCount;
    }

    @Override
    public int edgeCount() {
        return eCount;
    }

    @Override
    public void addAdge(int v1, int v2, double weight) {
        int maxV = Math.max(v1, v2);
        // добавляем вершин в список списков связности
        for (; vCount <= maxV; vCount++) {
            vEdjLists.add(null);
        }
        if (!isAdj(v1, v2)) {
            if (vEdjLists.get(v1) == null) {
                vEdjLists.set(v1, new LinkedList<>());
            }
            vEdjLists.get(v1).add(new WeightedEdgeTo(v2, weight));
            eCount++;
            // для наследников
            if (!(this instanceof Digraph)) {
                if (vEdjLists.get(v2) == null) {
                    vEdjLists.set(v2, new LinkedList<>());
                }
                vEdjLists.get(v2).add(new WeightedEdgeTo(v1, weight));
            }
        }
    }

    @Override
    public void addAdge(int v1, int v2) {
        addAdge(v1, v2, 0);
    }

    private int countingRemove(List<WeightedEdgeTo> list, int v) {
        int count = 0;
        if (list != null) {
            for (Iterator<WeightedEdgeTo> it = list.iterator(); it.hasNext(); ) {
                if (it.next().to == v) {
                    it.remove();
                    count++;
                }
            }
        }
        return count;
    }

    @Override
    public void removeAdge(int v1, int v2) {
        eCount -= countingRemove(vEdjLists.get(v1), v2);
        if (!(this instanceof Digraph)) {
            eCount -= countingRemove(vEdjLists.get(v2), v1);
        }
    }

    @Override
    public Iterable<WeightedEdgeTo> adjacenciesWithWeights(int v) {
        return vEdjLists.get(v) == null ? nullIterable : vEdjLists.get(v);
    }

    @Override
    public Iterable<Integer> adjacencies(int v) {
        return new Iterable<Integer>() {
            @Override
            public Iterator<Integer> iterator() {
                Iterator<WeightedEdgeTo> wIter = AdjListsWeightGraph.this.adjacenciesWithWeights(v).iterator();

                return new Iterator<Integer>() {
                    @Override
                    public boolean hasNext() {
                        return wIter.hasNext();
                    }

                    @Override
                    public Integer next() {
                        return wIter.next().to;
                    }
                };
            }
        };
    }
}
