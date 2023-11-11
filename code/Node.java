package code;
public class Node {
    int prosperity;
    int food;
    int materials;
    int energy;
    int moneySpent;

    Node parentNode;

    String operator;

    int waitTime;

    int money = 100000;

    int depth;

    boolean flagFood, flagMaterials, flagEnergy;
    
    public Node(int prosperity, int food, int materials, int energy, int moneySpent, Node parentNode, String operator, int depth){
        this.prosperity = prosperity;
        this.food = food;
        this.materials = materials;
        this.energy = energy;
        this.moneySpent = moneySpent;
        this.parentNode = parentNode;
        this.operator = operator;
        this.depth = depth;
    }

    public Node(Node node){
        this.prosperity = node.prosperity;
        this.food = node.food;
        this.materials = node.materials;
        this.energy = node.energy;
        this.moneySpent = node.moneySpent;
        this.parentNode = node.parentNode;
        this.operator = node.operator;
        this.flagFood = node.flagFood;
        this.flagMaterials = node.flagMaterials;
        this.flagEnergy = node.flagEnergy;
        this.waitTime = node.waitTime;
        this.money = node.money;
        this.depth = node.depth;
    }

    // public void requestFood(int amount, int foodTime){
    //     waitTime = foodTime;
        
    // }
}
