package com.ubb.utils.events_strategies;

import com.ubb.domain.entities.Duck;

import java.util.List;

public interface SolvingStrategy {
    public Double calculateSolution(List<Duck> ducks, List<Double> lanes);
}
