/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webLogic;

import java.util.ArrayList;

/**
 *
 * @author user
 */
public class WebManager {
    
    private boolean inGame;
    private boolean inGameSettings;
    private boolean waitingForPlayers;
    private boolean allPlayersEnterd;
    private int numberOfTotalPlayers;
    private int numberOfColorsForEachPlayer;
    private ArrayList<String> playersNames;
    //TODO: maybe add another bool for if in main menu
    private int numberOfPlayersToJoin;
    private int playersJoined;
    private boolean gameWasLoaded;

    public void resetWebManager() {
        inGame = false;
        inGameSettings = false;
        waitingForPlayers = false;
        allPlayersEnterd = false;
        playersNames.clear();
        numberOfTotalPlayers = 0;
        numberOfColorsForEachPlayer = 0;
        playersJoined = 0;
        numberOfPlayersToJoin = 0;
        gameWasLoaded = false;
    }

    public void setNumberOfTotalPlayers(int numberOfTotalPlayers) {
        this.numberOfTotalPlayers = numberOfTotalPlayers;
    }

    public void setNumberOfColorsForEachPlayer(int numberOfColorsForEachPlayer) {
        this.numberOfColorsForEachPlayer = numberOfColorsForEachPlayer;
    }

    public void setGameWasLoaded(boolean gameWasLoaded) {
        this.gameWasLoaded = gameWasLoaded;
    }

    public boolean isGameWasLoaded() {
        return gameWasLoaded;
    }
    
    public void setGameOver() {
        inGame = false;
        inGameSettings = false;
    }

    public int getPlayersJoined() {
        return playersJoined;
    }

    public void setNumberOfPlayersToJoin(int numberOfPlayersToJoin) {
        this.numberOfPlayersToJoin = numberOfPlayersToJoin;
    }

    public int getNumberOfPlayersToJoin() {
        return numberOfPlayersToJoin;
    }

    public void setAllPlayersEnterd(boolean allPlayersEnterd) {
        this.allPlayersEnterd = allPlayersEnterd;
    }

    public boolean isAllPlayersEnterd() {
        return allPlayersEnterd;
    }
    
    public boolean isPlayerInGame(String name) {
        if(playersNames.contains(name)) {
            return true;
        }
        
        else {
            playersNames.add(name);
            return false;
        }
            
   }
    
    public WebManager() {
        inGame = false;
        waitingForPlayers = false;
        allPlayersEnterd = false;
        playersNames = new ArrayList<>();
        playersJoined = 0;
    }

    public void setWaitingForPlayers(boolean waitingForPlayers) {
        this.waitingForPlayers = waitingForPlayers;
    }

    public boolean isWaitingForPlayers() {
        return waitingForPlayers;
    }

    public void setInGame(boolean inGame) {
        this.inGame = inGame;
    }

    public void setInGameSettings(boolean inGameSettings) {
        this.inGameSettings = inGameSettings;
    }

    public boolean isInGame() {
        return inGame;
    }

    public boolean isInGameSettings() {
        return inGameSettings;
    }

    public void setPlayersJoined(int num) {
        playersJoined = num;
    }

    public boolean gameIsReady() {
        if(playersNames.size() == numberOfPlayersToJoin) {
            return true;
        }
        return false;
    }
    
}
