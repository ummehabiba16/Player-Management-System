package com.uhl.playerdb.DTO;

import com.uhl.playerdb.model.Player;

import java.io.Serializable;
import java.util.List;

public class UpdateClubDTO implements Serializable {
    private String clubName;
    private List<Player> pl;
    private boolean status;

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    public List<Player> getPl() {
        return pl;
    }

    public void setPl(List<Player> pl) {
        this.pl = pl;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public UpdateClubDTO() {
        status = false;
    }

}
