package com.epam.jwd_critics.controller.command;

@FunctionalInterface
public interface CommandRequest {
    Object getAttribute(String name);
}
