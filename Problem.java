import java.util.ArrayList;
import java.util.Random;
public class Problem {
    //Instance Variables
    private int numPrems;
    private ArrayList<Statement> premises = new ArrayList<>(); //list of premises
    private Statement goal;

    //Constructor
    Problem(int prems, int nvars, int d) {
        this.numPrems = prems;
        //select variables to be used in statements
        //list of variables used
        char[] vars = new char[nvars];
        Random r = new Random();
        int n = r.nextInt(25 - nvars);
        for(int i = 0; i < nvars; i++) { //pick maxNumVars sequential letters to be variables
            //array of alphabet, V is not included so it doesn't get mixed with the OR symbol
            char[] letters = "ABCDEFGHIJKLMNOPQRSTUWXYZ".toCharArray();
            vars[i] = letters[n+i];
        }

        for(int i = 0; i < numPrems; i++) {
            Statement s = new Statement(d, vars); //create specified number of premise statements
            this.premises.add(s);
        }
        this.goal = new Statement(d, vars); //create goal statement
    }

    void printProblem() { //print premises and goal
        for(int i = 0; i < numPrems; i++) {
            premises.get(i).printStatement(); //each premise printed on different line
        }
        String g = "\u2234 " + goal.toString(); //"therefore" symbol added in front of goal
        System.out.println(g);
    }

    public String toString() { //convert problem to string
        StringBuilder ps = new StringBuilder();
        for(int i = 0; i < numPrems; i++) {
            ps.append(premises.get(i).toString());
            ps.append("\n");
        }
        ps.append("\u2234 ").append(goal.toString());
        return ps.toString();
    }

    String convertForm() { //convert form of problem to one usable by proplog.js for validity checking
        StringBuilder s = new StringBuilder();
        for (Statement premises1 : premises) {
            s.append("(");
            s.append(premises1.convertForm());
            s.append(") & ");
        }
        s.append("(").append(goal.convertForm()).append(")");
        return s.toString();
    }

}
