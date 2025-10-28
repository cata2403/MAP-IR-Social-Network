package com.ubb.infrastructure;

public interface DTStrategyFactory {
    public DataTransferStrategy<?> createStrategy(Object data);
}
