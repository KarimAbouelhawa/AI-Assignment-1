public class Node {
    int prosperity;
    int food;
    int materials;
    int energy;
    int moneySpent;

    Node parentNode;

    String operator;

    int waitTime;

    int depth;
    
    public Node(int prosperity, int food, int materials, int energy, int moneySpent, Node parentNode, String operator){
        this.prosperity = prosperity;
        this.food = food;
        this.materials = materials;
        this.energy = energy;
        this.moneySpent = moneySpent;
        this.parentNode = parentNode;
        this.operator = operator;
    }
}
