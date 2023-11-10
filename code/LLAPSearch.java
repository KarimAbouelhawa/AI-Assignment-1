package code;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class LLAPSearch extends GenericSearch {
    static ArrayList<Node> expansion = new ArrayList<>();
    static ArrayList<Node> nodes = new ArrayList<>();

    static int initProsperity;
    static int initFood;
    static int initMaterials;
    static int initEnergy;

    static int priceFood;
    static int priceMaterials;
    static int priceEnergy;

    static int requestFoodAmount;
    static int requestMaterialsAmount;
    static int requestEnergyAmount;

    static int requestFoodDelay;
    static int requestMaterialsDelay;
    static int requestEnergyDelay;

    static int build1Price;
    static int build1Food;
    static int build1Materials;
    static int build1Energy;
    static int build1Prosperity;

    static int build2Price;
    static int build2Food;
    static int build2Materials;
    static int build2Energy;
    static int build2Prosperity;

    static int build1TotalPrice;
    static int build2TotalPrice;
    static int upkeepCost;

    public static String solve(String initalState, String strategy, Boolean visualize) {
        nodes.clear();
        expansion.clear();
        String[] initalArray = initalState.split(";");
        initializeVariables(initalArray);
        
        Node initNode = new Node(initProsperity, initFood, initMaterials, initEnergy, 0, null, null, 0);
        switch (strategy) {
            case "BF":
                BF(initNode);
                break;
            case "DF":
                
                break;
            case "ID":
                iterativeDeepening(initNode);
                break;
            case "UC":
                
                break;
            
        
            default:
                break;
        }
        System.out.println(expansion);
        return generateResultString();
    }

    private static String generateResultString() {
    	ArrayList<Node> resultNodes = new ArrayList<>();
    	String res = "";
        Node currentNode = expansion.get(expansion.size() - 1);
        if (currentNode.prosperity < 100){
            return "NOSOLUTION";
        }
        while(currentNode.parentNode != null) {
        	resultNodes.add(currentNode);
        	currentNode = currentNode.parentNode;
        }
        while(!resultNodes.isEmpty()){
            res += resultNodes.remove(resultNodes.size()-1).operator + ",";
        }
        res += ";" + Integer.toString(expansion.get(expansion.size() - 1).moneySpent) + ";" + Integer.toString(expansion.size());
        return res;
    }

    public static void initializeVariables(String[] parameters) {
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
        build1Food = Integer.parseInt(build1Info[1]);
        build1Materials = Integer.parseInt(build1Info[2]);
        build1Energy = Integer.parseInt(build1Info[3]);
        build1Prosperity = Integer.parseInt(build1Info[4]);

        // Get info on building BUILD2
        String[] build2Info = parameters[7].split(",");
        build2Price = Integer.parseInt(build2Info[0]);
        build2Food = Integer.parseInt(build2Info[1]);
        build2Materials = Integer.parseInt(build2Info[2]);
        build2Energy = Integer.parseInt(build2Info[3]);
        build2Prosperity = Integer.parseInt(build2Info[4]);

        build1TotalPrice = (build1Food * priceFood) + (build1Energy * priceEnergy) + (build1Materials * priceMaterials)
                + build1Price;
        build2TotalPrice = (build2Food * priceFood) + (build2Energy * priceEnergy) + (build2Materials * priceMaterials)
                + build2Price;
        upkeepCost = priceFood + priceEnergy + priceMaterials;
    }

    public static Boolean goalState(Node node) {
        if (node.prosperity >= 100)
            return true;
        else
            return false;
    }

    public static void expandNode(Node node, boolean type) {
        // type true = stack, false = queue
        expansion.add(node);
        node.food--;
        node.materials--;
        node.energy--;
        node.money -= upkeepCost;
        node.moneySpent += upkeepCost;
        if (node.money >= 0) {

            if (type)
                nodes.remove(nodes.size() - 1); // try removing using object
            else
                nodes.remove(0);

            if (node.waitTime == 0) {
                if (node.flagFood) {
                    node.food += requestFoodAmount;
                    node.flagFood = false;
                }
                if (node.flagMaterials) {
                    node.materials += requestMaterialsAmount;
                    node.flagMaterials = false;
                }
                if (node.flagEnergy) {
                    node.energy += requestEnergyAmount;
                    node.flagEnergy = false;
                }
                // request food node creation and addition
                Node reqFood = new Node(node);
                reqFood.flagFood = true;
                reqFood.waitTime = requestFoodDelay;
                reqFood.parentNode = node;
                reqFood.operator = "reqFood";
                reqFood.depth += 1;
                nodes.add(reqFood);
                // request materials node creation and addition
                Node reqMaterials = new Node(node);
                reqMaterials.flagMaterials = true;
                reqMaterials.waitTime = requestMaterialsDelay;
                reqMaterials.parentNode = node;
                reqMaterials.operator = "reqMaterials";
                reqMaterials.depth += 1;
                nodes.add(reqMaterials);
                // request energy node creation and addition
                Node reqEnergy = new Node(node);
                reqEnergy.flagEnergy = true;
                reqEnergy.waitTime = requestEnergyDelay;
                reqEnergy.parentNode = node;
                reqEnergy.operator = "reqEnergy";
                reqEnergy.depth += 1;
                nodes.add(reqEnergy);
            }
            // wait node creation and addition
            if (node.waitTime > 0) {    
                Node wait = new Node(node);
                wait.waitTime--;
                wait.parentNode = node;
                wait.operator = "wait";
                wait.depth += 1;
                nodes.add(wait);
            }
            // Build 1 and 2 creation and addition
            if (node.food > build1Food - 1 && node.energy > build1Energy - 1 && node.materials > build1Materials - 1
                    && node.money > build1TotalPrice - upkeepCost) {
                Node Build1 = new Node(node);
                Build1.food -= build1Food - 1;
                Build1.materials -= build1Materials - 1;
                Build1.energy -= build1Energy - 1;
                Build1.money -= build1TotalPrice - upkeepCost;
                Build1.prosperity += build1Prosperity;
                Build1.parentNode = node;
                Build1.moneySpent += build1TotalPrice - upkeepCost;
                Build1.operator = "Build1";
                Build1.depth += 1;
                if (Build1.waitTime!=0) {
                    Build1.waitTime--;
                }
                if (Build1.money >= 0)
                    nodes.add(Build1);
            }
            if (node.food > build2Food - 1 && node.energy > build2Energy - 1 && node.materials > build2Materials - 1
                    && node.money > build2TotalPrice - upkeepCost) {
                Node Build2 = new Node(node);
                Build2.food -= build2Food - 1;
                Build2.materials -= build2Materials - 1;
                Build2.energy -= build2Energy - 1;
                Build2.money -= build2TotalPrice - upkeepCost;
                Build2.prosperity += build2Prosperity;
                Build2.parentNode = node;
                Build2.moneySpent += build2TotalPrice - upkeepCost;
                Build2.operator = "Build2";
                Build2.depth += 1;
                if (Build2.waitTime!=0) {
                    Build2.waitTime--;
                }
                if (Build2.money >= 0)
                    nodes.add(Build2);
            }
        }
    }

    public static void iterativeDeepening(Node initNode){
        nodes.clear();
        expansion.clear();
        nodes.add(initNode);
        boolean endLoop = false;
        int i = 0;
        while (!endLoop){
            endLoop = true;
            for (int j = 0; j <= i; j++){
                Node currentNode = nodes.get(nodes.size() - 1);
                if (currentNode.depth < i)
                    expandNode(currentNode, true);
                else{
                    nodes.remove(nodes.size() - 1);
                    expansion.add(currentNode);
                    if (!nodes.isEmpty())
                        j -= 1;
                    if(currentNode.money > 0)
                        endLoop = false;
                }
                if(goalState(currentNode)){
                    endLoop = true;
                    break;
                }
            }
            nodes.clear();
            nodes.add(initNode);
            i++;
                
        }
    }

    
    public static void ucs(Node node){
        nodes.add(node);
        expandNode(node, false);
        Stack<Node> ucs = new Stack<>();
        int[] min = new int[2];
        // get the individual nodes and push into stack
                min[0] = 0;
                min[1] = node.money;
                ucs.push(nodes.get(0));
        for (int j = 1; j <= ucs.size(); j++){
             // lowest cost node should be on top
            Node current = ucs.pop(); 
            expandNode(current, false);
            if (goalState(current)){
                break;
            }
            for (int i = 1; i <= nodes.size()-1; i++) {
            int money = nodes.get(i).money;
            if (nodes.get(i).money < min[1]){
                min[0] = i;
                min[1] = money;
            }
            else{
                ucs.push(nodes.get(i));
            }
            ucs.push(nodes.get(min[0]));
            }

        }
   
        
        //remainingprosperity/min(prosperityb1 + prosperityb2) * min(b1p,b2p).buildcost (+ previouscost) A*
        //remainingprosperity greedy
        //moneyspent greedy
        //
        
        } 
    public static void BF(Node initNode) {
    	nodes.add(initNode);
        while(true) {
        	Node currentNode = nodes.get(0);
        	if(goalState(currentNode)) {
        		expansion.add(currentNode);
        		break;
        	}
        	expandNode(currentNode, false);
        	if(nodes.isEmpty()) {
        		break;
        	}
        }
    }    
    

}
