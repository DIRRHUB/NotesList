package com.noteslist.models;

public class ConnectionModel {
    private final boolean isConnected;

    public ConnectionModel(boolean isConnected) {
        this.isConnected = isConnected;
    }

    public boolean getIsConnected() {
        return isConnected;
    }
}