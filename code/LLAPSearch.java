package code;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.PriorityQueue;

public class LLAPSearch extends GenericSearch {
    static ArrayList<Node> expansion = new ArrayList<>();
    static ArrayList<Node> nodes = new ArrayList<>();
    static HashSet<String> states = new HashSet<>();

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
        states.clear();
        String[] initalArray = initalState.split(";");
        initializeVariables(initalArray);

        Node initNode = new Node(initProsperity, initFood, initMaterials, initEnergy, 0, null, null, 0);
        switch (strategy) {
            case "BF":
                BF(initNode);
                break;
            case "DF":
                depthFirst(initNode);
                break;
            case "ID":
                iterativeDeepening(initNode);
                break;
            case "UC":
                ucs(initNode);
                break;
            case "GR1":
                greedy(initNode, true);
                break;
            case "GR2":
                greedy(initNode, false);
                break;
            case "AS1":
                AStar(initNode, true);
                break;
            case "AS2":
                AStar(initNode, false);
                break;
            

            default:
                break;
        }
        return generateResultString();
    }

    private static String generateResultString() {
        ArrayList<Node> resultNodes = new ArrayList<>();
        String res = "";
        Node currentNode = expansion.get(expansion.size() - 1);
        if (currentNode.prosperity < 100) {
            return "NOSOLUTION";
        }
        while (currentNode.parentNode != null) {
            resultNodes.add(currentNode);
            currentNode = currentNode.parentNode;
        }
        if (!resultNodes.isEmpty())
            res += resultNodes.remove(resultNodes.size() - 1).operator;
        while (!resultNodes.isEmpty()) {
            res += "," + resultNodes.remove(resultNodes.size() - 1).operator;
        }
        res += ";" + Integer.toString(expansion.get(expansion.size() - 1).moneySpent) + ";"
                + Integer.toString(expansion.size());
        System.out.println(res);
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

    public static void expandNode(Node node) {
        // type true = stack, false = queue
        // if (type)
        //     nodes.remove(nodes.size() - 1); // try removing using object
        // else
        nodes.remove(node);
        String state = new State(node).toString();
        if (states.contains(state)) {
            return;
        }
        states.add(state);
        expansion.add(node);
        Node newNode = new Node(node);
        newNode.food--;
        newNode.materials--;
        newNode.energy--;
        newNode.money -= upkeepCost;
        newNode.moneySpent += upkeepCost;
        newNode.depth += 1;
        if (newNode.money >= 0 && newNode.food >= 0 && newNode.materials >= 0 && newNode.energy >= 0) {


            if (newNode.waitTime == 0) {
                if (newNode.flagFood) {
                    newNode.food += requestFoodAmount;
                    if (newNode.food > 50)
                        newNode.food = 50;
                    newNode.flagFood = false;
                }
                if (newNode.flagMaterials) {
                    newNode.materials += requestMaterialsAmount;
                    if (newNode.materials > 50)
                        newNode.materials = 50;
                    newNode.flagMaterials = false;
                }
                if (newNode.flagEnergy) {
                    newNode.energy += requestEnergyAmount;
                    if (newNode.energy > 50)
                        newNode.energy = 50;
                    newNode.flagEnergy = false;
                }
                // request food node creation and addition
                if(newNode.food + requestFoodAmount <= 50){
                    Node reqFood = new Node(newNode);
                    reqFood.flagFood = true;
                    reqFood.waitTime = requestFoodDelay;
                    reqFood.parentNode = node;
                    reqFood.operator = "requestFood";
                    nodes.add(reqFood);
                }
                // request materials node creation and addition
                if(newNode.materials + requestMaterialsAmount <= 50){
                    Node reqMaterials = new Node(newNode);
                    reqMaterials.flagMaterials = true;
                    reqMaterials.waitTime = requestMaterialsDelay;
                    reqMaterials.parentNode = node;
                    reqMaterials.operator = "requestMaterials";
                    nodes.add(reqMaterials);
                }
                // request energy node creation and addition
                if(newNode.energy + requestEnergyAmount <= 50){
                    Node reqEnergy = new Node(newNode);
                    reqEnergy.flagEnergy = true;
                    reqEnergy.waitTime = requestEnergyDelay;
                    reqEnergy.parentNode = node;
                    reqEnergy.operator = "requestEnergy";
                    nodes.add(reqEnergy);
                }
            }
            // wait node creation and addition
            if (node.waitTime > 0) {
                Node wait = new Node(newNode);
                wait.waitTime--;
                wait.parentNode = node;
                wait.operator = "wait";
                nodes.add(wait);
            }
            // Build 1 and 2 creation and addition
            if (newNode.food >= build1Food - 1 && newNode.energy >= build1Energy - 1
                    && newNode.materials >= build1Materials - 1
                    && newNode.money >= build1TotalPrice - upkeepCost) {
                Node Build1 = new Node(newNode);
                Build1.food -= build1Food - 1;
                Build1.materials -= build1Materials - 1;
                Build1.energy -= build1Energy - 1;
                Build1.money -= build1TotalPrice - upkeepCost;
                Build1.prosperity += build1Prosperity;
                Build1.parentNode = node;
                Build1.moneySpent += build1TotalPrice - upkeepCost;
                Build1.operator = "Build1";
                if (Build1.waitTime != 0) {
                    Build1.waitTime--;
                }
                if (Build1.money >= 0)
                    nodes.add(Build1);
            }
            if (newNode.food >= build2Food - 1 && newNode.energy >= build2Energy - 1
                    && newNode.materials >= build2Materials - 1
                    && newNode.money >= build2TotalPrice - upkeepCost) {
                Node Build2 = new Node(newNode);
                Build2.food -= build2Food - 1;
                Build2.materials -= build2Materials - 1;
                Build2.energy -= build2Energy - 1;
                Build2.money -= build2TotalPrice - upkeepCost;
                Build2.prosperity += build2Prosperity;
                Build2.parentNode = node;
                Build2.moneySpent += build2TotalPrice - upkeepCost;
                Build2.operator = "Build2";
                if (Build2.waitTime != 0) {
                    Build2.waitTime--;
                }
                if (Build2.money >= 0)
                    nodes.add(Build2);
            }
        }
    }

    public static void iterativeDeepening(Node initNode) {
        nodes.add(initNode);
        boolean endLoop = false;
        int i = 0;
        while (!endLoop) {
            endLoop = true;
            while (!nodes.isEmpty()) {
                Node currentNode = nodes.get(nodes.size() - 1);
                if (currentNode.depth < i)
                    expandNode(currentNode);
                else {
                    nodes.remove(nodes.size() - 1);
                    expansion.add(currentNode);
                    if (currentNode.money > 0)
                        endLoop = false;
                }
                if (goalState(currentNode)) {
                    endLoop = true;
                    break;
                }
            }
            nodes.clear();
            nodes.add(initNode);
            states.clear();
            i++;

        }
    }

    public static void ucs(Node node){
        PriorityQueue<Node> queue = new PriorityQueue<>();
        queue.add(node);
        while (!nodes.isEmpty() || !queue.isEmpty()){
            while (!nodes.isEmpty()){
                queue.add(nodes.remove(0));
            }

            Node currentNode = queue.remove();
            if (goalState(currentNode)){
                expansion.add(currentNode);
                return;
            }
            nodes.add(currentNode);
            expandNode(currentNode);
        }
    }
            

    public static void BF(Node initNode) {
        nodes.add(initNode);
        while (true) {
            Node currentNode = nodes.get(0);
            if (goalState(currentNode)) {
                expansion.add(currentNode);
                break;
            }
            expandNode(currentNode);
            if (nodes.isEmpty()) {
                break;
            }
        }
    }

    public static void depthFirst(Node initNode) {
        nodes.add(initNode);
        while (!nodes.isEmpty()) {
            Node currentNode = nodes.get(nodes.size() - 1);
            expandNode(currentNode);
            if (goalState(currentNode)) {
                break;
            }
        }
    }


    public static void greedy(Node initNode, boolean type){
        if (type)
        {
            PriorityQueue<Node> queue = new PriorityQueue<>();
            queue.add(initNode);
            while (!nodes.isEmpty() || !queue.isEmpty()){
                while (!nodes.isEmpty()){
                    Node newNode = nodes.remove(0);
                    newNode.comparator = - newNode.money;
                    queue.add(newNode);
                }
                Node currentNode = queue.remove();
                if (goalState(currentNode)){
                    expansion.add(currentNode);
                    return;
                }
                nodes.add(currentNode);
                expandNode(currentNode);
            }
        }
        else
        {
            PriorityQueue<Node> queue = new PriorityQueue<>();
            queue.add(initNode);
            int minBuildCost = build1Prosperity < build2Prosperity ?  build1TotalPrice :  build2TotalPrice;
            while (!nodes.isEmpty() || !queue.isEmpty()){
                while (!nodes.isEmpty()){
                    Node newNode = nodes.remove(0);
                    int newComparator = ((100 - newNode.prosperity) / (Math.min(build1Prosperity,build2Prosperity))) * minBuildCost;
                    newNode.comparator = - newComparator;
                    queue.add(newNode);
                }
                Node currentNode = queue.remove();
                if (goalState(currentNode)){
                    expansion.add(currentNode);
                    return;
                }
                nodes.add(currentNode);
                expandNode(currentNode); 
        }
    }

}

public static void AStar(Node initNode, boolean type){
        if (type)
        {
            PriorityQueue<Node> queue = new PriorityQueue<>();
            queue.add(initNode);
            int maxBuildCost = build1Prosperity > build2Prosperity ?  build1TotalPrice :  build2TotalPrice;
            while (!nodes.isEmpty() || !queue.isEmpty()){
                while (!nodes.isEmpty()){
                    Node newNode = nodes.remove(0);
                    int newComparator = ((100 - newNode.prosperity) / (Math.max(build1Prosperity,build2Prosperity))) * maxBuildCost;
                    newNode.comparator = (newComparator + newNode.moneySpent);
                    queue.add(newNode);
                }
                Node currentNode = queue.remove();
                if (goalState(currentNode)){
                    expansion.add(currentNode);
                    return;
                }
                nodes.add(currentNode);
                expandNode(currentNode);
            }
        }
        else
        {
            PriorityQueue<Node> queue = new PriorityQueue<>();
            queue.add(initNode);
            int maxBuildFood = build1Prosperity > build2Prosperity ?  build1Food :  build2Food;
            while (!nodes.isEmpty() || !queue.isEmpty()){
                while (!nodes.isEmpty()){
                    Node newNode = nodes.remove(0);
                    int newComparator = (100 - newNode.prosperity / Math.max(build1Prosperity,build2Prosperity)) * maxBuildFood * priceFood;
                    newNode.comparator = - newComparator;
                    queue.add(newNode);
                }
                Node currentNode = queue.remove();
                if (goalState(currentNode)){
                    expansion.add(currentNode);
                    return;
                }
                nodes.add(currentNode);
                expandNode(currentNode); 
        }
    }

}
}
