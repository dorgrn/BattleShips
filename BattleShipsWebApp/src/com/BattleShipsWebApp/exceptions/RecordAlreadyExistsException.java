package com.BattleShipsWebApp.exceptions;

public class RecordAlreadyExistsException extends Exception {
    public RecordAlreadyExistsException(final String message){
        super("Record already exists in table " + message);
    }
}
