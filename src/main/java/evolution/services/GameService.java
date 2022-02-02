package evolution.services;


import evolution.dto.GameDto;
import evolution.dto.LobbyDto;
import evolution.entity.Unit;
import evolution.entity.User;

public interface GameService {

    GameDto accept(User user);

    void reject(User user);

    void freeData();

    LobbyDto getCurrent(User user);

    Unit newUnit(Double x, Double y, Double bodyRadius, Integer hp, Integer damage, Integer speed, Double detectRadius);

}
