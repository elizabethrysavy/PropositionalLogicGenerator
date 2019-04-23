import java.io.*;
class Verify {
    private String probstr;
    private String uiLoc;

    Verify(String p, String floc) { //constructor
        this.uiLoc = floc;
        this.probstr = p;

    }


    /*TODO This currently does not work correctly
       currently when run it returns:
             Microsoft Windows [Version 6.3.9600]
            (c) 2013 Microsoft Corporation. All rights reserved.
       which is the text found at the top of the command prompt
       It does this for any command I try to run and I don't know why it doesn't work
       Couldn't find anyone online having similar problems
     */
    String getSolvable() {
        String cmd = "cd " + uiLoc + " && node proplog.js \"" + probstr + "\"";
        //run command prompt and run cmd command
        ProcessBuilder pb = new ProcessBuilder("cmd.exe", cmd);

        StringBuilder outstream = new StringBuilder();
        try { //collect the output from the command prompt
            Process p = pb.start();
            BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            OutputStream ost = p.getOutputStream();
            while(!(line = r.readLine()).equals("")) {
                outstream.append(line).append("\n");
            }

            r.close();
            ost.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return outstream.toString();

    }

}

