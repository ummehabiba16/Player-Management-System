package com.uhl.playerdb.DTO;

import java.io.Serializable;

public class DisconnectDTO implements Serializable {
    private String clientUsername;

    public DisconnectDTO(String clientUsername) {
        this.clientUsername = clientUsername;
    }

    public String getClientUsername() {
        return clientUsername;
    }

    public void setClientUsername(String clientUsername) {
        this.clientUsername = clientUsername;
    }
}

