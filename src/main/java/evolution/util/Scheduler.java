package evolution.util;

import evolution.entity.*;
import evolution.enums.AbilityType;
import evolution.repositories.AbilityRepository;
import evolution.repositories.GameRepository;
import evolution.repositories.LobbyRepository;
import evolution.repositories.UnitRepository;
import evolution.services.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@EnableScheduling
public class Scheduler {
    private final GameRepository gameRepository;
    private final LobbyRepository lobbyRepository;
    private final UnitRepository unitRepository;
    private final AbilityRepository abilityRepository;
    private final GameService gameService;

    @Value("${timer}")
    private Integer timer;

    @Scheduled(fixedRate = 1000)
    public void updateGames() {
        List<Lobby> lobbies = lobbyRepository.findAll();
        for (Lobby lobby : lobbies) {
            if (lobby.getGame() != null) {
                for (User user : lobby.getUsers()) {
                    for (Unit unit : user.getUnits()) {
                        if (unit.getHp() > 0) {
                            detectEnemy(unit, user, lobby.getUsers());
                            action(unit, user, lobby.getGame());
                        }
                    }
                }
                lobby.getGame().setTimer(lobby.getGame().getTimer() + 1);
                gameRepository.save(lobby.getGame());
            }
        }
    }

    // todo compare to the most close enemy
    private void detectEnemy(Unit currentUnit, User currentUser, List<User> users) {
        for (User user : users) {
            if (!user.equals(currentUser)) {
                for (Unit unit : user.getUnits()) {
                    double distance = distance(unit, currentUnit);
                    if (distance <= currentUnit.getDetectRadius() && unit.getHp() > 0 && currentUnit.getHp() > 0) {
                        currentUnit.setEnemy(unit);
                    }
                }
            }
        }
    }

    // todo move to the enemy before fight
    private void action(Unit unit, User user, Game game) {
        unit.setAge(unit.getAge() + 1);
        if (unit.getAge() == 10) {
            unit.setHp(unit.getHp() + 100);
            unit.setBodyRadius(2.0);
        } else if (unit.getAge() == 20) {
//            unitRepository.delete(unit);
            unit.setHp(0);
            unit.setEnemy(null);
            unitRepository.save(unit);
            return;
        }
        if (divideCondition(user, game)) {
            List<Ability> mutatedAbilities = abilityRepository.findAllByMutatedUsers(user);
            Optional<Ability> fang = mutatedAbilities.stream()
                    .filter(o -> o.getType().equals(AbilityType.FANG))
                    .findFirst();
            Optional<Ability> shield = mutatedAbilities.stream()
                    .filter(o -> o.getType().equals(AbilityType.SHIELD))
                    .findFirst();
            Optional<Ability> hunting = mutatedAbilities.stream()
                    .filter(o -> o.getType().equals(AbilityType.HUNTING))
                    .findFirst();
            Optional<Ability> doubleDivision = mutatedAbilities.stream()
                    .filter(o -> o.getType().equals(AbilityType.DOUBLE_DIVISION))
                    .findFirst();
            List<Unit> newUnits = new ArrayList<>();
            Unit newUnit = gameService.newUnit(unit.getX(), unit.getY(), 1.0, 100, 50, 10, 40.0);
            newUnits.add(newUnit);
            if (doubleDivision.isPresent()){
                Unit newUnit2 = gameService.newUnit(unit.getX(), unit.getY(), 1.0, 100, 50, 10, 40.0);
                newUnits.add(newUnit2);
            }
            if (fang.isPresent()){
                newUnits.forEach(o->o.setDamage(80));
            }
            if (shield.isPresent()){
                newUnits.forEach(o->o.setHp(150));
            }
            if (hunting.isPresent()){
                newUnits.forEach(o->o.setDetectRadius(70.0));
            }
            newUnits.forEach(o->o.setUser(user));
            unitRepository.saveAll(newUnits);
        } else if (unit.getEnemy() == null) {
            double angle = Math.random() * 360;
            double sin = Math.sin(angle);
            double cos = Math.cos(angle);
            double newX = unit.getX() + Math.random() * unit.getSpeed();
            double newY = unit.getY() + Math.random() * unit.getSpeed();
            newX = Math.max(cos * (newX - unit.getX()) - sin * (newY - unit.getY()) + unit.getX(), -100);
            newX = Math.min(newX, 100);
            newY = Math.max(sin * (newX - unit.getX()) + cos * (newY - unit.getY()) + unit.getY(), -100);
            newY = Math.min(newY, 100);
            unit.setX(newX);
            unit.setY(newY);
        } else {
            Unit enemy = unit.getEnemy();
            double distance = distance(unit, enemy);
            double coefficient = Math.min(unit.getSpeed() / distance, distance - 1);
            double newX = (unit.getX() + coefficient * enemy.getX()) / (1 + coefficient);
            double newY = (unit.getY() + coefficient * enemy.getY()) / (1 + coefficient);
            Unit newUnit = new Unit();
            newUnit.setX(newX);
            newUnit.setY(newY);
            if (distance(newUnit, unit) < 5){
                enemy.setHp(unit.getEnemy().getHp() - unit.getDamage());
            } else {
                unit.setX(newX);
                unit.setY(newY);
            }
            if (enemy.getHp() <= 0) {
//                unit.setEnemy(null);
//                enemy.setUser(null);
//                unitRepository.save(enemy);
//                unitRepository.delete(enemy);
                enemy.setHp(0);
                unit.setEnemy(null);
                unitRepository.save(enemy);
            } else {
                unitRepository.save(enemy);
            }
        }
        unitRepository.save(unit);
    }

    private boolean divideCondition(User user, Game game) {
        Optional<Ability> ability = abilityRepository.findAllByMutatedUsers(user).stream()
                .filter(o -> o.getType().equals(AbilityType.DIVISION))
                .findFirst();
//        return (ability.isPresent() && timer % 6 == 0) || timer % 12 == 0;
        return (ability.isPresent() && game.getTimer() % 6 == 0) || game.getTimer() % 12 == 0;
    }

    private double distance(Unit first, Unit second) {
        double deltaX = (first.getX() - second.getX());
        double deltaY = (first.getY() - second.getY());
        return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }
}
