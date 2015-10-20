/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manager;

import logic.*;
import java.awt.Point;
import java.util.ArrayList;

/**
 *
 * @author oroze this is the old manager from ex2, TODO delete what we dont need in here!!!
 */
public class GameManager {
    
    private int numberOfPlayers;
    private int numberOfHumanPlayers;
    private int numberOfColorsForEachPlayer;
    private ArrayList<String> playerNames = new ArrayList<>();
    private ArrayList<String> HumanNames = new ArrayList<>();
    private final GameEngine checkersEngine = new GameEngine();
    private boolean isUserCanSave = true;
    private boolean isOnSkipRoll = false;
    
    public void setLastPlayerAsCurrent(){
        checkersEngine.getLastPlayer();
    }
    
    public void setCurrentPickedMarble(Point pickedMarble){
        checkersEngine.setCurrentPickedMarble(pickedMarble);
    }
    
    public ArrayList<Player> getPlayers(){
        return checkersEngine.getPlayers();
    }

    public ArrayList<String> getPlayerNames() {
        return playerNames;
    }

    public ArrayList<String> getHumanNames() {
        return HumanNames;
    }
    
    public boolean isHumanTurn(){
        return checkersEngine.isHumanTurn();
    }
    
     public void removePlayerFromGame(String playerName){
        checkersEngine.removeCurrentPlayerFromGame(playerName);
    }
    
    public boolean isOnly1PlayerRemaining(){
        return checkersEngine.isOnly1PlayerRemaining();
    }
    
    public ArrayList<Point> getAiMove(){
        return checkersEngine.playAITurn();
    }
    
    public CheckersBoard getBoard(){
        return checkersEngine.getBoard();
    }
    
    public ArrayList<Point> getPossibleMoves(){
        return checkersEngine.getPossibleMoves();
    }
    
    public Player getCurrentPlayer(){
        return checkersEngine.getCurrentPlayerTurn();
    }
       
    public void playAITurn(){
        checkersEngine.playAITurn();
    }
    
    public void clearPossibleMoves(){
        checkersEngine.getPossibleMoves().clear();
    }
    
    public void calculatePossibleMoves(Point marble){
        checkersEngine.calculatePossibleMovesForPlayer(marble);
    }
    
    public void calculateSkipMovesOnly(Point marble){
        checkersEngine.calculatePossibleSkipPointsOnly(marble);
    }
    
    public boolean isSkipMove(Point movingTo){
        return checkersEngine.checkIfSkipped(checkersEngine.getCurrentPickedMarble(), movingTo);
    }
    
    public void moveMarble(Point movingTo){
        checkersEngine.updateBoardWithNewPoint(movingTo);
        checkersEngine.updatePlayerMarblesAfterMove(movingTo);
    }
    
    public Player checkForWinner(){
        return checkersEngine.checkForWinner();
    }
    public void switchTurns(){
        checkersEngine.switchTurns();
    }
    
    public void initGame() {
        isOnSkipRoll = false;
        checkersEngine.initNewGame(playerNames, numberOfPlayers, numberOfHumanPlayers, numberOfColorsForEachPlayer);
    }
    
    public void initGameFromXML(XMLGameData data) {
        playerNames = data.getPlayersNames();
        getNamesForHuman(data, HumanNames);
        numberOfPlayers = data.getPlayers().size();
        numberOfHumanPlayers = data.getNumberOfHumanPlayers();
        numberOfColorsForEachPlayer = data.getNumberOfColorsForPlayer();
        checkersEngine.initNewGameFromXML(data);
    }

    public boolean isDuplicateName(String name) {
        if (playerNames.contains(name)) {
            return true;
        }
        return false;
    }
    

    public void addPlayerToList(String name) {
        playerNames.add(name);
    }

    public void setNumberOfPlayers(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }

    public void setNumberOfHumanPlayers(int numberOfHumanPlayers) {
        this.numberOfHumanPlayers = numberOfHumanPlayers;
    }

    public void setNumberOfColorsForEachPlayer(int numberOfColorsForEachPlayer) {
        this.numberOfColorsForEachPlayer = numberOfColorsForEachPlayer;
    }

    public void setPlayerNames(ArrayList<String> playerNames) {
        this.playerNames = playerNames;
    }

    public XMLGameData getDataFromGame() {
        XMLGameData data = new XMLGameData();
        data.setBoard(checkersEngine.getBoard());
        data.setCurrentPlayersTurn(checkersEngine.getCurrentPlayerTurn());
        data.setPlayers(checkersEngine.getPlayers());
        return data;
    }

    public boolean isOnlyComputersLeft() {
        return checkersEngine.isOnlyComputersLeft();
    }

    public void initGameWithSameSettings() {
        checkersEngine.initWithSameData();
    }

    public void setIsUserCanSave(boolean b) {
        isUserCanSave = b;
    }
    
    public boolean isUserCanSave(){
        return isUserCanSave;
    }
    
    public void clearSets(){
        ColorSets.clearAll();
    }

    public void tempNamesAdder() { //TODO remove this method
        playerNames.clear();
        for (int i = 0; i < numberOfHumanPlayers; i++) {
            playerNames.add("Human #" + i+1);
        }
    }

    public void clearMovesFromBoard() {
        checkersEngine.addOrRemovePossibleMovesInBoard(false);
    }
    
    public boolean isOnSkipRoll(){
        return isOnSkipRoll;
    }
    
    public void setIsOnSkipRoll(boolean b){
        isOnSkipRoll = b;
    }

    public boolean isCurrentUserMarble(Point pickedMarble) {
        boolean found = false;
        for (Point marble : checkersEngine.getCurrentPlayerTurn().getMarbles()) {
            if(marble.equals(pickedMarble)){
                found = true;
                break;
            }
        }
        return found;
    }

    private void getNamesForHuman(XMLGameData data, ArrayList<String> HumanNames) {
        
        for(Player player : data.getPlayers()) {
            if(player.isHuman()) {
                HumanNames.add(player.getName());
            }
        }
        
    }


}

