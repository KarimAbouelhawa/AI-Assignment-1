import java.util.ArrayList;

public class LLAPSearch extends GenericSearch {
    ArrayList<Node> expansion;
    ArrayList<Node> nodes;

    int initProsperity;
    int initFood;
    int initMaterials;
    int initEnergy;

    int priceFood;
    int priceMaterials;
    int priceEnergy;

    int requestFoodAmount;
    int requestMaterialsAmount;
    int requestEnergyAmount;

    int requestFoodDelay;
    int requestMaterialsDelay;
    int requestEnergyDelay;

    int build1Price;
    int build1Food;
    int build1Materials;
    int build1Energy;
    int build1Prosperity;
    
    int build2Price;
    int build2Food;
    int build2Materials;
    int build2Energy;
    int build2Prosperity;


    
    public String solve(String initalState,String  strategy, Boolean visualize) {
        String[] initalArray = initalState.split(";");
        
        return "1";
    }

    public void initializeVariables(){
        initProsperity = Integer.parseInt(parameters[0]);
            
        // Get initial values of resources
        String[] initialResources = parameters[1].split(",");
        initFood = Integer.parseInt(initialResources[0]);
        initMaterials = Integer.parseInt(initialResources[1]);
        initEnergy = Integer.parseInt(initialResources[2]);

        // Get unit prices of resources
        String[] unitPrices = parameters[2].split(",");
        priceFood = Integer.parseInt(unitPrices[0]);
        priceMaterials = Integer.parseInt(unitPrices[1]);
        priceEnergy = Integer.parseInt(unitPrices[2]);

        // Get the amount of food that can be requested and the delay
        String[] foodDeliveryInfo = parameters[3].split(",");
        requestFoodAmount = Integer.parseInt(foodDeliveryInfo[0]);
        requestFoodDelay = Integer.parseInt(foodDeliveryInfo[1]);

        // Get the amount of materials that can be requested and the delay
        String[] materialsDeliveryInfo = parameters[4].split(",");
        requestMaterialsAmount = Integer.parseInt(materialsDeliveryInfo[0]);
        requestMaterialsDelay = Integer.parseInt(materialsDeliveryInfo[1]);

        // Get the amount of energy that can be requested and the delay
        String[] energyDeliveryInfo = parameters[5].split(",");
        requestEnergyAmount = Integer.parseInt(energyDeliveryInfo[0]);
        requestEnergyDelay = Integer.parseInt(energyDeliveryInfo[1]);

        // Get info on building BUILD1
        String[] build1Info = parameters[6].split(",");
        build1Price = Integer.parseInt(build1Info[0]);
        build1 = Integer.parseInt(build1Info[1]);
        materialsUseBUILD1 = Integer.parseInt(build1Info[2]);
        energyUseBUILD1 = Integer.parseInt(build1Info[3]);
        prosperityBUILD1 = Integer.parseInt(build1Info[4]);

        // Get info on building BUILD2
        String[] build2Info = parameters[7].split(",");
        priceBUILD2 = Integer.parseInt(build2Info[0]);
        foodUseBUILD2 = Integer.parseInt(build2Info[1]);
        materialsUseBUILD2 = Integer.parseInt(build2Info[2]);
        energyUseBUILD2 = Integer.parseInt(build2Info[3]);
        prosperityBUILD2 = Integer.parseInt(build2Info[4]);
    }


    public Boolean goalState(Node node){
        if (node.prosperity == 100)
            return true;
        else
            return false;
    }

    public void expandNode(Node node, boolean type){
        // type true = stack, false = queue
        expansion.add(node);
        if(type)
            nodes.remove(nodes.size() - 1); // try removing using object
        else
            nodes.remove(0);
        if(node.waitTime == 0){
            
        }
    }
}
