package com.ubb.infrastructure_layer.utils.event_strategies;

import com.ubb.domain_layer.entities.Duck;

import java.util.List;

public interface SolvingStrategy {
    public Double calculateSolution(List<Duck> ducks, List<Double> lanes);
}

