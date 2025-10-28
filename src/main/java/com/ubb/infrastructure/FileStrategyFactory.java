package com.ubb.infrastructure;
import com.ubb.domain.Duck;
import com.ubb.domain.Friendship;
import com.ubb.domain.Person;

public class FileStrategyFactory implements DTStrategyFactory {
    @Override
    public DataTransferStrategy<Long> createStrategy(Object data) {
        if(data instanceof Person)
            return new FDTPerson();
        else if(data instanceof Duck)
            return new FDTDuck();
        else if(data instanceof Friendship)
            return new FDTFriendship();
        return null;
    }
}
