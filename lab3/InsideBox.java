import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

public class InsideBox{
    static boolean check(ArrayList<Integer> b1, ArrayList<Integer> b2){
        return b1.get(0) < b2.get(0) && b1.get(1) < b2.get(1) && b1.get(2) < b2.get(2);
    }
    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader("input.txt"));
        BufferedWriter out = new BufferedWriter(new FileWriter("output.txt"));
        String s = in.readLine(); // input string
        ArrayList<ArrayList<Integer>> b = new ArrayList<>(); // array of boxes
        ArrayList<Integer> temp = new ArrayList<>(); // current box
        String cur = ""; // current number of current box
        int ans = 1; // result
        for(int i = 0; i < s.length(); i++){ // fill data in 2-dimension array n * 3
            if (s.charAt(i) == '['){
                temp.clear();
                cur = "";
            }
            else if(s.charAt(i) == ']'){
                temp.add(Integer.parseInt(cur));
                Collections.sort(temp); // rotation feature
                ArrayList<Integer> e = new ArrayList<>();
                b.add(e);
                for(int j = 0; j < 3; j++){
                    b.get(b.size()-1).add(temp.get(j));
                }
            }
            else if(s.charAt(i) == ' '){
                temp.add(Integer.parseInt(cur));
                cur = "";
            }
            else{
                cur += s.charAt(i);
            }
        }
        ArrayList<Integer> d = new ArrayList<>(); // answer for each box
        for(int i = 0; i < b.size(); i++){
            d.add(1);
        }
        // calculating result for each box using dp approach
        for(int i = 0; i < b.size(); i++){
            for(int j = i + 1; j < b.size(); j++){
                if(check(b.get(i), b.get(j))){ // i box put into j box
                    d.set(j, Math.max(d.get(j), d.get(i) + 1));
                }
                if(check(b.get(j), b.get(i))){ // j box put into i box
                    d.set(i, Math.max(d.get(i), d.get(j) + 1));
                }
            }
        }
        for(int i = 0; i < b.size(); i++){
            ans = Math.max(ans, d.get(i)); // choosing max value
        }
        out.write(Integer.toString(ans));
        in.close();
        out.close();
    }
}