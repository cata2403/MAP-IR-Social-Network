package com.ubb.infrastructure;

import com.ubb.domain.entities.SwimMasters;

public class FlockFileSavingStrategy implements DataTransferStrategy<Long, SwimMasters>{
    @Override
    public String serialize(SwimMasters flock) {
        return flock.getId() + "," +flock.getFlockName();
    }

    @Override
    public SwimMasters deserialize(String fileLine) {
        String[] fields = fileLine.split(",");

        return new SwimMasters( Long.parseLong(fields[0]), fields[1] );
    }
}
