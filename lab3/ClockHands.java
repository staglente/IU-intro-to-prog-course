import java.io.*;

public class ClockHands {
    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader("input.txt"));
        BufferedWriter out = new BufferedWriter(new FileWriter("output.txt"));
        String s = in.readLine(); // input string
        int h = (s.charAt(0) - '0') * 10 + (s.charAt(1) - '0'); // only hours
        int m = (s.charAt(3) - '0') * 10 + (s.charAt(4) - '0'); // only minutes
        if(h > 23 || m > 59){
            out.write("Wrong format"); // impossible case
        }
        else{
            String ans = Integer.toString(Math.abs((h%12) * 5 - m) * 6); // answer calculations
            out.write(ans);
        }
        in.close();
        out.close();
    }
}