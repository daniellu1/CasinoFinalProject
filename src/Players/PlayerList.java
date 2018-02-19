package Players;

import java.util.ArrayList;

public class PlayerList {
    
    public static ArrayList<Player> playerList = new ArrayList<>(); 

    public void addPlayer(Player p){
        playerList.add(p); 
    }
    
    public void removePlayer(Player p){
        playerList.remove(p); 
    }
}
