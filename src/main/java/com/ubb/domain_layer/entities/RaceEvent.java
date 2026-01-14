package com.ubb.domain_layer.entities;

import com.ubb.exceptions.DomainException;
import com.ubb.infrastructure_layer.utils.event_strategies.SolvingStrategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RaceEvent extends Event {

    List<Double> lanes;
    List<Duck> ducks =  new ArrayList<>();
    SolvingStrategy solvingStrategy;

    public RaceEvent(Long id, String eventName, SolvingStrategy solvingStrategy) {
        super(id, eventName);

        lanes = new ArrayList<>();
        this.solvingStrategy = solvingStrategy;
    }

    public void signAsParticipant(Duck duck){
        ducks.add(duck);
    }

    public void addLane(Double lane) {
        this.lanes.add(lane);
        setMinFlockSize(getMinFlockSize() + 1);
    }

    public List<Duck> getParticipants() {
        return ducks;
    }

    public List<Double> getLanes() {
        return lanes;
    }

    public void removeAllLanes() {
        this.lanes.clear();
        setMinFlockSize(0);
    }

    @Override
    public Double startEvent() {

        if(ducks.size()<lanes.size()){
            throw new DomainException("There are not enough participants");
        }
        Collections.sort( ducks );
        Collections.sort( lanes );
        Double bestTime = solvingStrategy.calculateSolution( ducks, lanes );
        return bestTime;
        //buildDuckTeam( bestTime );
    }

    private void buildDuckTeam(Double bestTime)
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

    public String toString()
    {
        return getEventName();
    }
}


