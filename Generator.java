import javax.swing.*;
import java.util.ArrayList;

public class Generator {

    //form objects
    private JRadioButton pc3;
    private JRadioButton pc2;
    private JRadioButton pc1;
    private JTextPane ptext;
    private JButton goButton;
    private JSpinner numPrem;
    private JSpinner numVar;
    private JPanel op;
    private JPanel opts;
    private JPanel warnp;
    private JPanel outp;
    private JPanel premsp;
    private JPanel numsp;
    private JButton vbutton;
    private JTextArea vtext;
    private JTextArea vwarn;
    private JTextArea ploc;
    private JTextArea cmdtxt;


    private int diff = 1;
    private Problem pr;
    int nv, np;

    private Generator() {
        ButtonGroup pgroup = new ButtonGroup();
        pgroup.add(pc1);
        pgroup.add(pc2);
        pgroup.add(pc3);
        numPrem.setValue(2); //set initial number of premises to 2
        numVar.setValue(3); //set initial number of variables to 3
        Problem p = new Problem(2, 3,1); //create an initial problem
        pr = p;
        ptext.setText(p.toString());
        ploc.setText(System.getProperty("user.dir")); //set initial text of directory location. WARNING! may not be correct (i.e. mine is missing last folder in address)
        String warn = "WARNING! VALIDITY CHECKER DOES NOT CURRENTLY WORK CORRECTLY IN THIS PROGRAM.\n";
        warn+= "To check validity of this problem, paste the following into the Command Prompt:";
        vwarn.setText(warn); //warning text
        //when Go button clicked, runGen()
        goButton.addActionListener(e -> runGen());
        //when Validate button clicked, validate()
        vbutton.addActionListener(e -> validate());

    }

    private void runGen() { //Problem generation
        //determine selected difficulty level
        if(pc1.isSelected()) diff = 1;
        else if(pc2.isSelected()) diff = 2;
        else if(pc3.isSelected()) diff = 3;

        nv = (Integer) numVar.getValue();
        np = (Integer) numPrem.getValue();
        if(nv < 1 || nv > 25) { //if number variables selected is invalid
            ptext.setText("Please choose a maximum number of variables between 1 and 25");
            return;
        }

        if(np > 5) { //if number of premises selected is invalid
            ptext.setText("Please choose a number of premises between 0 and 5");
            return;
        }

        Problem p = new Problem(np, nv, diff); //create problem
        pr = p;
        ptext.setText(p.toString());

    }

    private void validate() { //check if the problem is valid
        String fLoc = ploc.getText(); //directory location
        String probtxt = editProb(ptext.getText());
        Verify v = new Verify(probtxt, fLoc); //run verifier
        vtext.setText(v.getSolvable());

        //text for box under warning, for copying and pasting into command prompt to determine validity
        String ctxt = "cd " + fLoc;
        ctxt+="\nnode proplog.js \"" + probtxt + "\"";
        ctxt+="\n(If already in project directory, cd line can be skipped)";

        cmdtxt.setText(ctxt);
    }

    private String editLine(String s) {
        s = s.replace('\u2227', '&'); //replace AND character
        s = s.replace('\u2228', '|'); //replace OR character
        s = s.replaceAll("\u2192", "->"); //replace IF character
        s = s.replaceAll("\u2194", "<->"); //replace IFF character
        s = s.replace('\u00AC', '-'); //replace NOT character
        s = s.replace("\u2234 ", "");
        return s;

    }
    private String editProb(String pstr) {
       String[] prems = pstr.split("\n");
       StringBuilder newprob = new StringBuilder();
       int i;
       for(i = 0; i < prems.length - 1; i++) {
           newprob.append("(");
           newprob.append(editLine(prems[i]));
           newprob.append(") & ");
       }
        newprob.append("(").append(editLine(prems[i])).append(")");
       return newprob.toString();

    }

    public static void main(String[] args) { //Main function
        JFrame frame = new JFrame("Generator");
        frame.setContentPane(new Generator().op);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }



}


