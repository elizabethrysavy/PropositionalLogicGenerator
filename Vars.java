import java.util.Random;
public class Vars { //Variable object
    //Instance Variables
    private String letter; //variable representation
    private boolean negated; //is the variable negated or not?

    //Constructor
    Vars(char letter) {
        this.letter = String.valueOf(letter);
        Random r = new Random();
        int p = r.nextInt(100);
        this.negated = (p > 60); //40% chance variable will be negated
    }

    public String toString() { //convert variable to string
        if(!this.negated) {
            return this.letter;
        }
        else {
            return "\u00AC" + this.letter;
        }
    }

    boolean equals(Vars v2) { //check if two variables are the same (same char representation and same negation status)
        return((this.getLetter().equals(v2.getLetter())) && (this.isNegated() == v2.isNegated()));
    }



    private boolean isNegated() {
        return negated;
    } //return whether or not the variable is negated

    String getLetter() {
        return this.letter;
    } //return the character representation of the variable
}
