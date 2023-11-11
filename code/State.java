package code;

public class State {
    int prosperity;
    int food;
    int materials;
    int energy;
    int moneySpent;

    boolean flagFood, flagMaterials, flagEnergy;

    public State(Node node){
        prosperity = node.prosperity;
        food = node.food;
        materials = node.materials;
        energy = node.energy;
        moneySpent = node.moneySpent;
        flagFood = node.flagFood;
        flagMaterials = node.flagMaterials;
        flagEnergy = node.flagEnergy;
    }

    public String toString(){
        return (String.valueOf(prosperity) + ";" + String.valueOf(food) + ";" + String.valueOf(materials) 
        + ";" + String.valueOf(energy) + ";" + String.valueOf(moneySpent) + ";" + Boolean.toString(flagFood) 
        + ";" + Boolean.toString(flagMaterials) + ";" + Boolean.toString(flagEnergy));
    }
}
