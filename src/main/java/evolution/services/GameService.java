package evolution.services;


import evolution.dto.GameDto;
import evolution.entity.Unit;

public interface GameService {

    GameDto startNewGame(Long lobbyId);

    void freeData();

    GameDto getGame();

     Unit newUnit(Double x, Double y, Double bodyRadius, Integer hp, Integer damage, Integer speed, Double detectRadius);

}
