package com.epam.jwd_critics.controller.command;

public class CommandResponse {
    private Destination destination;
    private TransferType transferType;

    public CommandResponse(Destination destination, TransferType transferType) {
        this.destination = destination;
        this.transferType = transferType;
    }

    public static CommandResponse redirectToPreviousPageOr(ServletDestination destination, CommandRequest req) {
        CommandResponse resp = new CommandResponse(destination, TransferType.REDIRECT);
        String page = req.getParameter(Parameter.CURRENT_PAGE);
        if (page != null) {
            resp.setDestination(() -> page);
        }
        return resp;
    }

    public Destination getDestination() {
        return destination;
    }

    public void setDestination(Destination destination) {
        this.destination = destination;
    }

    public TransferType getTransferType() {
        return transferType;
    }

    public void setTransferType(TransferType transferType) {
        this.transferType = transferType;
    }
}
