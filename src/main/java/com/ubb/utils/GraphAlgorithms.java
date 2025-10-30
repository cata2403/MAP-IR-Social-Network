package com.ubb.utils;

import java.util.*;

public class GraphAlgorithms {
    static public List<Long> BFS(Map<Long, List<Long>> graf, int[] parcurs, Long start){
        Queue<Long> queue = new LinkedList<>();
        queue.add(start);
        parcurs[Math.toIntExact(start)] = 1;
        List<Long> nodes = new ArrayList<>();
        while (!queue.isEmpty()){
            Long node = queue.poll();
            nodes.add(node);
            List<Long> list = graf.getOrDefault(node, Collections.emptyList());
            for(Long neighbour : list){
                if(parcurs[Math.toIntExact(neighbour)] == 0){
                    parcurs[Math.toIntExact(neighbour)] = parcurs[Math.toIntExact(node)] + 1;
                    queue.add(neighbour);
                }
            }
        }
        return nodes;
    }
}
