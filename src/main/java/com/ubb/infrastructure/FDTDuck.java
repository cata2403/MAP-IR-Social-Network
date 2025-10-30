package com.ubb.infrastructure;

import com.ubb.domain.Duck;
import com.ubb.domain.DuckType;
import com.ubb.domain.Entity;

public class FDTDuck implements DataTransferStrategy<Long, Duck> {

    @Override
    public String serialization(Duck o) {
        String fileLine = ((Duck)o).getId().toString() + ',' + ((Duck) o).getUsername() + ',' + ((Duck) o).getPassword() + ',' + ((Duck) o).getEmail();
        fileLine += ',' + ((Duck) o).getDuckType().name() + ',' + ((Duck) o).getResistance().toString() + ',';
        fileLine += ((Duck) o).getSpeed().toString() + ',' + ((Duck) o).getFlockId().toString();
        return fileLine;
    }

    @Override
    public Duck deserialization(String s) {
        String[] split = s.split(",");
        Duck user = new Duck(Long.parseLong(split[0]),split[1],split[2],split[3]);
        user = user.setDuckType(DuckType.valueOf(split[4])).setResistance(Double.parseDouble(split[5])).setSpeed(Double.parseDouble(split[6])).setFlockId(Long.parseLong(split[7]));
        return user;
    }
}
