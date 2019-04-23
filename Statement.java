import java.util.ArrayList;
import java.util.Random;
public class Statement { //Statement object
    //a Statement is a single line in the Problem (either a premise or the goal)

    //Instance variables
    private ArrayList<Vars> vlist = new ArrayList<>(); //array containing variables found in statement
    private ArrayList<Connector> cons = new ArrayList<>(); //array of connectors used
    private String[] neg;
    private String[] pcons = new String[]{"AND", "OR", "IF", "IFF"}; //possible connectors
    private int diff; //difficulty level
    private Random r = new Random();
    private int numVars; //number of variables used in statement

    //constructor
    Statement(int diff, char[] pvlist) { //constructor
        //diff is complexity level (1, 2, or 3), pvlist is list of possible variables to use
        this.diff = diff;
        if(pvlist.length == 1) numVars = 1;
        else numVars = chooseNumVars();
        chooseVarsAndCons(pvlist);
    }


    private int chooseNumVars() { //choose number of variables in statement, probability of a number depends on complexity level
        int rand = r.nextInt(99);
        switch(diff) {
            case 1: //Complexity 1: probabilities for number of variables
                if(rand < 20) return 1; //1 var: 20%
                else if(rand < 70) return 2; //2 var: 50%
                else if(rand < 95) return 3; //3 var: 25%
                else return 4; //4 var: 5%
            case 2: //Complexity 2: probabilities for number of variables
                if(rand < 10) return 1; //1 var: 10%
                else if(rand < 30) return 2; //2 var: 20%
                else if(rand < 55) return 3; //3 var: 25%
                else if(rand < 80) return 4; //4 var: 25%
                else if(rand < 90) return 5; //5 var: 10%
                else return 6; //6 var: 10%
            case 3: //Complexity 3: probabilities for number of variables
                if(rand < 4) return 1; //1 var: 4%
                else if(rand < 14) return 2; //2 var: 10%
                else if(rand < 31) return 3; //3 var: 17%
                else if(rand < 48) return 4; //4 var: 17%
                else if(rand < 61) return 5; //5 var: 13%
                else if(rand < 74) return 6; //6 var: 13%
                else if (rand < 87) return 7; //7 var: 13%
                else return 8; //8 var: 13%
        }
        return 3; //random value because it said I still needed a return statement
    }

    private static <T> String[] objToStrList(ArrayList<T> a) { //convert an ArrayList of objects to an array of Strings
        String[] tc = new String[a.size()];
        for(int i = 0; i < a.size(); i++) {
            tc[i] = a.get(i).toString();
        }
        return tc;
    }


    private void chooseVarsAndCons(char[] pvlist) { //choose variables and connectors used in statement
        // pvlist is list of possible variables to use in statement
        //choose connectors (numVars-1 of them)
        for(int i = 0; i < (numVars-1); i++) {
            cons.add(chooseConnector());
        }

        //choose variables to use in statement, variable can be reused but cannot use same one twice in a row
        //two variables are the same if they have the same character representation and the same negation status
        Vars prev;
        int rn = r.nextInt(pvlist.length);
        Vars v = new Vars(pvlist[rn]);
        vlist.add(v);
        prev = v;
        for(int i = 1; i < numVars; i++) {
            Connector prevcon = cons.get(i-1);
            //make sure same variable isn't used twice in a row
            while ((prevcon.getCtype().equals("OR") && v.equals(prev)) || (v.getLetter().equals(prev.getLetter()))) {
                rn = r.nextInt(pvlist.length);
                v = new Vars(pvlist[rn]);
            }
            vlist.add(v);
            prev = v;
        }

        //choose if parts of statements are negated
        int ns; //number of possible things to negate
        if(numVars == 1) ns = 1;
        else ns = numVars-1;
        neg = new String[ns];
        for(int i = 0; i < neg.length; i++) {
            if(chooseNegated()) neg[i] = "\u00AC";
            else neg[i] = "";
        }
    }

    private boolean chooseNegated() { //choose if statement negated or not
        int p = r.nextInt(100);
        return (p <= 30); //30% chance of being negated
    }


    public String toString() { //convert statement to string
        String[] sv = objToStrList(vlist);
        String[] sc = objToStrList(cons);

        String s = "";
        switch(numVars) { //format of converted string depends on number of variables
            //each case contains all possible arrangements of parenthases that I could think of. Couldn't think of a better solution than hard-coding it
            case 1: //1 possible format for 1 variable statement
                //v0    ex. A
                s = sv[0];
                break;
            case 2: //1 possible format for 2 variable statement
                //v0 c0 v1  ex. A V B
                s = String.format("%s %s %s", sv[0], sc[0], sv[1]);
                break;
            case 3: //2 possible formats for 3 variable statement
                int n = r.nextInt(2);
                if(n == 0) { //n1(v0 c0 v1) c1 v2   ex. (A V B) V C
                    s = String.format("%s(%s %s %s) %s %s", neg[1], sv[0], sc[0], sv[1], sc[1], sv[2]);
                }
                else { //v0 c0 n1(v1 c1 v2)     ex. A V (B V C)
                    s = String.format("%s %s %s(%s %s %s)", sv[0], sc[0], neg[1], sv[1], sc[1], sv[2]);
                }
                break;
            case 4: //1 possible format for 4 variable statement
                //n1(v0 c0 v1) c1 n2(v2 c2 v3)  ex. (A V B) V (C V D)
                s = String.format("%s(%s %s %s) %s %s(%s %s %s)", neg[1], sv[0], sc[0], sv[1], sc[1], neg[2], sv[2], sc[2], sv[3]);
                break;
            case 5: //4 possible formats for 5 variable statement
                n = r.nextInt(4);
                switch(n) {
                    case 0:
                        //n1[n2(v0 c0 v1) c1 n3(v2 c2 v3)] c3 v4    ex. [(A V B) V (C V D)] V E
                        s = String.format("%s[%s(%s %s %s) %s %s(%s %s %s)] %s %s", neg[1], neg[2], sv[0], sc[0], sv[1], sc[1], neg[3], sv[2], sc[2], sv[3], sc[3], sv[4]);
                        break;
                    case 1:
                        //n1[n2(v0 c0 v1) c1 v2] c2 n3(v3 c3 v4)    ex. [(A V B) V C] V (D V E)
                        s = String.format("%s[%s(%s %s %s) %s %s] %s %s(%s %s %s)", neg[1], neg[2], sv[0], sc[0], sv[1], sc[1], sv[2], sc[2], neg[3], sv[3], sc[3], sv[4]);
                        break;
                    case 2:
                        //n1[v0 c0 n2(v1 c1 v2)] c2 n3(v3 c3 v4)    ex. [A V (B V C)] V (D V E)
                        s = String.format("%s[%s %s %s(%s %s %s)] %s %s(%s %s %s)", neg[1], sv[0], sc[0], neg[2], sv[1], sc[1], sv[2], sc[2], neg[3], sv[3], sc[3], sv[4]);
                        break;
                    case 3:
                        //v0 c0 n1[n2(v1 c1 v2) c2 n3(v3 c3 v4)]    ex. A V [(B V C) V (D V E)]
                        s = String.format("%s %s %s[%s(%s %s %s) %s %s(%s %s %s)]", sv[0], sc[0], neg[1], neg[2], sv[1], sc[1], sv[2], sc[2], neg[3], sv[3], sc[3], sv[4]);
                        break;
                }
                break;
            case 6: //6 possible formats for 6 variable statement
                n = r.nextInt(6);
                switch(n) {
                    case 0:
                        //n1[n2(v0 c0 v1) c1 n3(v2 c2 v3)] c3 n4(v4 c4 v5)  ex. [(A V B) V (C V D)] V (E V F)
                        s = String.format("%s[%s(%s %s %s) %s %s(%s %s %s)] %s %s(%s %s %s)", neg[1], neg[2], sv[0], sc[0], sv[1], sc[1], neg[3], sv[2], sc[2], sv[3], sc[3], neg[4], sv[4], sc[4], sv[5]);
                        break;
                    case 1:
                        //n1(v0 c0 v1) c1 n2[n3(v2 c2 v3) c3 n4(v4 c4 v5)]  ex. (A V B) V [(C V D) V (E V F)]
                        s = String.format("%s(%s %s %s) %s %s[%s(%s %s %s) %s %s(%s %s %s)]", neg[1], sv[0], sc[0], sv[1], sc[1], neg[2], neg[3], sv[2], sc[2], sv[3], sc[3], neg[4], sv[4], sc[4], sv[5]);
                        break;
                    case 2:
                        //n1[n2(v0 c0 v1) c1 v2] c2 n3[n4(v3 c3 v4) c4 v5]  ex. [(A V B) V C] V [(D V E) V F]
                        s = String.format("%s[%s(%s %s %s) %s %s] %s %s[%s(%s %s %s) %s %s]", neg[1], neg[2], sv[0], sc[0], sv[1], sc[1], sv[2], sc[2], neg[3], neg[4], sv[3], sc[3], sv[4], sc[4], sv[5]);
                        break;
                    case 3:
                        //n1[n2(v0 c0 v1) c1 v2] c2 n3[v3 c3 n4(v4 c4 v5)]  ex. [(A V B) V C] V [D V (E V F)]
                        s = String.format("%s[%s(%s %s %s) %s %s] %s %s[%s %s %s(%s %s %s)]", neg[1], neg[2], sv[0], sc[0], sv[1], sc[1], sv[2], sc[2], neg[3], sv[3], sc[3], neg[4], sv[4], sc[4], sv[5]);
                        break;
                    case 4:
                        //n1[v0 c0 n2(v1 c1 v2)] c2 n3[n4(v3 c3 v4) c4 v5]  ex. [A V (B V C)] V [(D V E) V F]
                        s = String.format("%s[%s %s %s(%s %s %s)] %s %s[%s(%s %s %s) %s %s]", neg[1], sv[0], sc[0], neg[2], sv[1], sc[1], sv[2], sc[2], neg[3], neg[4], sv[3], sc[3], sv[4], sc[4], sv[5]);
                        break;
                    case 5:
                        //n1[v0 c0 n2(v1 c1 v2)] c2 n3[v3 c3 n4(v4 c4 v5)]  ex. [A V (B V C)] V [D V (E V F)]
                        s = String.format("%s[%s %s %s(%s %s %s)] %s %s[%s %s %s(%s %s %s)]", neg[1], sv[0], sc[0], neg[2], sv[1], sc[1], sv[2], sc[2], neg[3], sv[3], sc[3], neg[4], sv[4], sc[4], sv[5]);
                        break;
                }
                break;
            case 7: //4 possible formats for 7 variable statement
                n = r.nextInt(4);
                switch(n) {
                    case 0:
                        //n1[n2(v0 c0 v1) c1 v2] c2 n3[n4(v3 c3 v4) c4 n5(v5 c5 v6)]    ex. [(A V B) V C] V [(D V E) V (F V G)]
                        s = String.format("%s[%s(%s %s %s) %s %s] %s %s[%s(%s %s %s) %s %s(%s %s %s)]", neg[1], neg[2], sv[0], sc[0], sv[1], sc[1], sv[2], sc[2], neg[3], neg[4], sv[3], sc[3], sv[4], sc[4], neg[5], sv[5], sc[5], sv[6]);
                        break;
                    case 1:
                        //n1[v0 c0 n2(v1 c1 v2)] c2 n3[n4(v3 c3 v4) c4 n5(v5 c5 v6)]    ex. [A V (B V C)] V [(D V E) V (F V G)]
                        s = String.format("%s[%s %s %s(%s %s %s)] %s %s[%s(%s %s %s) %s %s(%s %s %s)]", neg[1], sv[0], sc[0], neg[2], sv[1], sc[1], sv[2], sc[2], neg[3], neg[4], sv[3], sc[3], sv[4], sc[4], neg[5], sv[5], sc[5], sv[6]);
                        break;
                    case 2:
                        //n1[n2(v0 c0 v1) c1 n3(v2 c2 v3)] c3 n4[n5(v4 c4 v5) c5 v6]    ex. [(A V B) V (C V D)] V [(E V F) V G]
                        s = String.format("%s[%s(%s %s %s) %s %s(%s %s %s)] %s %s[%s(%s %s %s) %s %s]", neg[1], neg[2], sv[0], sc[0], sv[1], sc[1], neg[3], sv[2], sc[2], sv[3], sc[3], neg[4], neg[5], sv[4], sc[4], sv[5], sc[5], sv[6]);
                        break;
                    case 3:
                        //n1[n2(v0 c0 v1) c1 n3(v2 c2 v3)] c3 n4[v4 c4 n5(v5 c5 v6)]    ex. [(A V B) V (C V D)] V [E V (F V G)]
                        s = String.format("%s[%s(%s %s %s) %s %s(%s %s %s)] %s %s[%s %s %s(%s %s %s)]", neg[1], neg[2], sv[0], sc[0], sv[1], sc[1], neg[3], sv[2], sc[2], sv[3], sc[3], neg[4], sv[4], sc[4], neg[5], sv[5], sc[5], sv[6]);
                        break;
                }
                break;
            case 8: //1 possible format for 8 variable statement
                //n1[n2(v0 c0 v1) c1 n3(v2 c2 v3)] c3 n4[n5(v4 c4 v5) c5 n6(v6 c6 v7)]  ex. [(A V B) V (C V D)] V [(E V F) V (G V H)]
                s = String.format("%s[%s(%s %s %s) %s %s(%s %s %s)] %s %s[%s(%s %s %s) %s %s(%s %s %s)]", neg[1], neg[2], sv[0], sc[0], sv[1], sc[1], neg[3], sv[2], sc[2], sv[3], sc[3], neg[4], neg[5], sv[4], sc[4], sv[5], sc[5], neg[6], sv[6], sc[6], sv[7]);
                break;
        }

        if(!neg[0].equals("")) s = String.format("¬(%s)", s); //if whole statement is negated, wrap whole thing in () and add not in front
        return s;
    }


    private Connector chooseConnector() { //choose which connector to use
        int cnum = r.nextInt(4);
        return new Connector(pcons[cnum]);
    }

    void printStatement() { //print the statement
        System.out.println(this.toString());
    } //print the statement


    String convertForm() { //convert the form of the statement to one that proplog.js can use for validity checking
        String s = this.toString();
        s = s.replace('\u2227', '&'); //replace AND character
        s = s.replace('\u2228', '|'); //replace OR character
        s = s.replaceAll("\u2192", "->"); //replace IF character
        s = s.replaceAll("\u2194", "<->"); //replace IFF character
        s = s.replace('\u00AC', '-'); //replace NOT character
        return s;

    }

}
