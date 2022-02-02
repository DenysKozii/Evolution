package evolution.services;


import evolution.dto.GameDto;
import evolution.entity.Unit;
import evolution.entity.User;

public interface GameService {

    GameDto startNewGame(User user);

    void freeData();

    GameDto getGame(User user);

     Unit newUnit(Double x, Double y, Double bodyRadius, Integer hp, Integer damage, Integer speed, Double detectRadius);

}
