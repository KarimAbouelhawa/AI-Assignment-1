package code;
public class App {
    public static void main(String[] args) {
        String initialState8 = "93;" +
			"46,42,46;" +
			"5,32,24;" +
			"13,2;24,1;20,1;" +
			"155,7,5,10,7;" +
			"5,5,5,4,4;";

        String solution = LLAPSearch.solve(initialState8, "UC", false);
        System.out.println(solution);
    }
}
