package com.epam.jwd_critics.controller.command;

@FunctionalInterface
public interface CommandResponse {
    default TransferType getTransferType() {
        return TransferType.FORWARD;
    }

    Destination getDestination();
}
