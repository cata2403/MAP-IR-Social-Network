package com.ubb.infrastructure;

import com.ubb.domain.entity_types.DuckType;
import com.ubb.domain.entities.SwimmingDuck;
import com.ubb.domain.entities.FlyingSwimmingDuck;
import com.ubb.domain.entities.FlyingDuck;
import com.ubb.domain.entities.Duck;

public class DuckFileSavingStrategy implements DataTransferStrategy<Long, Duck> {

    @Override
    public String serialize(Duck duck) {

        return String.join(",",
                String.valueOf(duck.getId()),
                duck.getUsername(),
                duck.getPassword(),
                duck.getEmail(),
                duck.getDuckType().name(),
                String.valueOf(duck.getResistance()),
                String.valueOf(duck.getSpeed()),
                String.valueOf(duck.getFlockId()));
    }

    @Override
    public Duck deserialize(String fileLine) {

        String[] duckData = fileLine.split(",");

        Duck duck;
        DuckType duckType = DuckType.valueOf(duckData[4]);

        if (duckType == DuckType.SWIMMING) {
            duck = new SwimmingDuck(
                    Long.parseLong(duckData[0]), duckData[1], duckData[2], duckData[3]
            );
        }

        else if (duckType == DuckType.FLYING_AND_SWIMMING) {
            duck = new FlyingDuck(
                    Long.parseLong(duckData[0]), duckData[1], duckData[2], duckData[3]
            );
        }

        else duck = new FlyingSwimmingDuck(
                Long.parseLong(duckData[0]), duckData[1], duckData[2], duckData[3]
            );

        duck = duck.setDuckType( DuckType.valueOf(duckData[4]) ).
                    setResistance( Double.parseDouble(duckData[5]) ).
                    setSpeed( Double.parseDouble(duckData[6]) ).
                    setFlockId( Long.parseLong(duckData[7]) );

        return duck;
    }
}
