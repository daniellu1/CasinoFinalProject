package Players;

import RFID.UserInput;

public class Player {
    
    private String id; 
    private int money; 
    private String name; 
    
    public Player(String id){
        this.id = id; 
        money = 1000; 
        name = UserInput.getString("Input Name:");
    }
    
    public String getId(){
        return(id);
    }
    
    public int getMoney(){
        return(money); 
    }
    
    public void addMoney(int amount){
        money+=amount;
    }
    
    public int setMoney(int amount){
        money=amount;
    }
    
    public String getName(){
        return(name); 
    }
}
