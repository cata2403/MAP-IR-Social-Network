package com.ubb.infrastructure_layer.utils.event_strategies;
import com.ubb.domain_layer.entities.Duck;

import java.util.List;

public class DynamicProgrammingStrategy implements SolvingStrategy {

    @Override
    public Double calculateSolution(List<Duck> ducks, List<Double> lanes) {

        int N = ducks.size(), M = lanes.size(), cnt = 0;
        double[] mins = new double[N-M+1];

        for (Double lane : lanes) {

            double min = mins[0];
            for (int k = 0, ind = cnt; k < N - M + 1; k++, ind++) {

                if ( mins[k] < min )
                    min = mins[k];

                double nr = (double) lane * 2 / ducks.get(ind).getSpeed();
                mins[k] = Math.max(min, nr);

            }
        }

        double min = mins[0];
        for (double v : mins) {
            if ( v < min ) {
                min = v;
            }
        }

        return (double)min;
    }
}


