public class Item {
    private String description;
    private double weight;
    private String name;
    private boolean isMovable;

    public Item(String name,String description ,double weight) {
        this.description = description;
        this.weight = weight;
        this.name=name;
        isMovable = true;
    }

    public boolean isMovable() {
        return isMovable;
    }

    public void setMovable(boolean movable) {
        isMovable = movable;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getWeight() {
        return weight;
    }

    public String toString() {
        return this.name + " (" + this.description + ") with weight of " + this.weight + "kg";
    }
}
