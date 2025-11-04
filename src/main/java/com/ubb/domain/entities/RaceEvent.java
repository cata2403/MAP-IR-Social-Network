package com.ubb.domain.entities;

import com.ubb.utils.events_strategies.SolvingStrategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

public class RaceEvent extends Event {

    List<Double> lanes;
    SolvingStrategy solvingStrategy;

    public RaceEvent(Long id, String eventName, SolvingStrategy solvingStrategy) {
        super(id, eventName);

        lanes = new ArrayList<>();
        this.solvingStrategy = solvingStrategy;
    }

    public void addLane(Double lane) {
        this.lanes.add(lane);
        setMinFlockSize(getMinFlockSize() + 1);
    }

    public void removeAllLanes() {
        this.lanes.clear();
        setMinFlockSize(0);
    }

    @Override
    public void startEvent(List<Duck> ducks) {

        Collections.sort( ducks );
        Collections.sort( lanes );
        Double bestTime = solvingStrategy.calculateSolution( ducks, lanes );

        buildDuckTeam( bestTime, ducks );
    }

    private void buildDuckTeam(Double bestTime,  List<Duck> ducks)
    {
        int laneInd = 0, duckInd = 0;
        List<Duck> bestTeam =  new ArrayList<Duck>();
        while( laneInd < lanes.size() && duckInd < ducks.size() ) {
            if((double) lanes.get(laneInd)*2 / ducks.get(duckInd).getSpeed() <= bestTime)
            {
                bestTeam.add( ducks.get(duckInd) );
                laneInd++;
            }
            duckInd++;
        }

        showResults(bestTeam, bestTime);
    }

    private void showResults(List<Duck> bestTeam, double bestTime)
    {
        System.out.println("Siiii echipa cu timpul cel mai bun este....");
        for (Duck duck : bestTeam) {
            System.out.print(duck.getUsername() + "   ");
        }
        System.out.println("\nSi au obtinut timpu: " + bestTime);

        notifyObservers();
    }
}
