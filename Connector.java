public class Connector { //Connector object

    //Instance Variables
    private String ctype;
    private String ucode; //unicode for character

    //constructor
    Connector(String ctype) {
        //ctype in form AND, OR, IF, or IFF
        this.ctype = ctype;
        //determine character unicode
        switch(ctype) {
            case "AND":
                this.ucode = "\u2227"; //"∧"
                break;
            case "OR":
                this.ucode = "\u2228"; //"∨"
                break;
            case "IF":
                this.ucode = "\u2192"; //"→"
                break;
            case "IFF":
                this.ucode = "\u2194"; //"↔"
                break;
        }
    }

    String getCtype() {
        return ctype;
    } //return type of connector

    public String toString() {
        return ucode;
    } //convert connector to string

}
