package code;
public class App {
    public static void main(String[] args) {
        String state = "17;" +
        "49,30,46;" +
        "7,57,6;" +
        "7,1;20,2;29,2;" +
        "350,10,9,8,28;" +
        "408,8,12,13,34;";

        String solution = LLAPSearch.solve(state, "UC", false);
        System.out.println(solution);
    }
}
