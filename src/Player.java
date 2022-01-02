import java.util.ArrayList;

public class Player {
    private String name;
    private ArrayList<Item> bag;
    private double maxWeightInBag;
    private Room currentRoom;

    public Player(String name) {
        this.name = name;
        bag = new ArrayList<>();
        maxWeightInBag=25;
        this.currentRoom= currentRoom;
    }

    public String getName() {
        return name;
    }

    public void addItem(Item item){
        bag.add(item);

    }

    public double getMaxWeightInBag() {
        return maxWeightInBag;
    }

    public void setMaxWeightInBag(double maxWeightInBag) {
        this.maxWeightInBag = maxWeightInBag;
    }

    public Room getCurrentRoom() {
        return currentRoom;
    }

    public void setCurrentRoom(Room currentRoom) {
        this.currentRoom = currentRoom;
    }

    private String getBagInfo() {
        String bagInfo= "";
        if (!bag.isEmpty()) {
            bagInfo= "i have a bag which contains:";
            for (Item item :bag) {
                bagInfo += "\n   " + item.toString();
            }
        }
        return bagInfo;
    }

    public String getInfo(){
        String info = "My name is " + name;
        info+= " and i am " + currentRoom.getLongDescription();
        info+= "\n" + getBagInfo();
        return info;
    }
    public String playerinfo(){
        String info = "My name is " + name + " and i can carry a maximum of " + maxWeightInBag + " kg.";

        info+= "\n" + getBagInfo() ;
        return info;

    }

    public boolean go(String direction) {
        Room nextRoom = currentRoom.getExit(direction);
        if (nextRoom == null) return false;
        currentRoom=nextRoom;
        return true;
    }

    public boolean take(String name) {
        if (currentRoom.hasItem(name)){
            Item item = currentRoom.getItem(name);
            if (item.isMovable()){
                currentRoom.removeIItem(item);
                bag.add(item);
                return true;

            }
        }
        return false;
    }

    private boolean hasItem(String itemName){
        for (Item item: bag) {
            if (itemName.equals(item.getName())) return true;
        }
        return false;
    }
    public boolean drop(String itemName) {
        if(hasItem(itemName)){
            Item item = getItem(itemName);
            bag.remove(item);
            currentRoom.addItem(item);
            return true;

        }
        return false;
    }
    public Item getItem(String itemName){
        for (Item bagItem : bag) {
            if (bagItem.getName().equals(itemName)){
                return bagItem;
            }
        }
        return null;
    }
    public boolean eat(String itemName){
        if (hasItem(itemName) && itemName.equals("cookie")){
            Item food =getItem(itemName);
            bag.remove(food);
            maxWeightInBag+=5;
            return true;
        }
        return false;
    }


}
