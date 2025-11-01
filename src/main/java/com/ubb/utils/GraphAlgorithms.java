package com.ubb.utils;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.ArrayList;
import java.util.Collections;


public final class GraphAlgorithms {

    static public List<Long> bfs(Map<Long, List<Long>> graph, int[] distances, Long start){

        Queue<Long> queue = new LinkedList<>();
        queue.add(start);
        distances[Math.toIntExact(start)] = 1;
        List<Long> visitedNodes = new ArrayList<>();

        while (!queue.isEmpty()){
            Long node = queue.poll();
            visitedNodes.add(node);
            List<Long> list = graph.getOrDefault(node, Collections.emptyList());

            for(Long neighbour : list){
                if( distances[Math.toIntExact(neighbour)] == 0 ){
                    distances[Math.toIntExact(neighbour)] = distances[Math.toIntExact(node)] + 1;
                    queue.add(neighbour);
                }
            }
        }
        return visitedNodes;
    }

    static public long findBiggestNode(Map<Long, List<Long>> graph){

        long maxId = 0;
        for( Long key : graph.keySet() ){
            if( key > maxId ){
                maxId = key;
            }
        }
        return maxId;
    }
}
