import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
public class TextJustification {
    /* function which checks if input string contains empty words or not */
    public static boolean check(String str){
        if(str.charAt(0) == ' ')
            return false;
        for(int i = 1; i < str.length(); i++)
            if(str.charAt(i) == ' ' && str.charAt(i - 1) == ' ')
                return false;
        return true;
    }

    @SuppressWarnings("resource")
    public static void main(String[] args) throws IOException {
        BufferedWriter out = new BufferedWriter(new FileWriter("output.txt")); // output file
        String useable = ".,!?-:;()'" + '"'; // part of legal symbols
        Set<Character> canUse = new HashSet<>(); // legal symbols in log2(N) accessed container
        List<String> words = new ArrayList<>(); // divide input string on words
        int maxw = 0; // max width

        /* fill legal symbols container */
        for(int i = 0; i < useable.length(); i++)
            canUse.add(useable.charAt(i));
        for(char i = 'a'; i <= 'z'; i++)
            canUse.add(i);
        for(char i = 'A'; i <= 'Z'; i++)
            canUse.add(i);

        try{
            try (BufferedReader in = new BufferedReader(new FileReader("input.txt"))) {
                /* check if input file empty or not */
                if(new File("input.txt").length() == 0)
                    throw new EmptyFileException("Exception, file is empty!");

                String s = in.readLine(); // input string
                String k = in.readLine(); // input max width

                /* check if the second line exists or not */
                if(k == null)
                    throw new NoLineException("Exception, intended line width is not specified!");

                maxw = Integer.parseInt(k);

                /* check if max width positive or not */
                if(maxw <= 0)
                    throw new WidthValueException("Exception, line width cannot be negative or zero!");

                /* check if each word no longer than max legal or not */
                if(s.length() > 300)
                    throw new TextSizeException("Exception, input exceeds text max size!");

                /* check if there is no empty words in input or not */
                if(!check(s))
                    throw new EmptyWordException("Exception, input contains an empty word!");

                /* check if all symbols are legal or not */
                for(int i = 0; i < s.length(); i++){
                    if(s.charAt(i) == ' ') continue;
                    if(!canUse.contains(s.charAt(i)))
                        throw new InvalidSymbolException("Exception, input contains forbidden symbol '" + s.charAt(i) + "'!");
                }

                /* check if all words has legal length or not */
                String cur = "";
                for(int i = 0; i < s.length(); i++){
                    if(s.charAt(i) == ' '){
                        if(cur.length() > 20)
                            throw new WordSizeException("Exception, '" + cur + "' exceeds the limit of 20 symbols!");
                        words.add(cur);
                        cur = "";
                    }
                    else
                        cur += s.charAt(i);
                }
                if(!cur.isEmpty()){
                    if(cur.length() > 20)
                        throw new WordSizeException("Exception, '" + cur + "' exceeds the limit of 20 symbols!");
                    words.add(cur);
                }
                for(int i = 0; i < words.size(); i++)
                    if(words.get(i).length() > maxw)
                        throw new WidthSizeException("Exception, '" + words.get(i) + "' exceeds " + maxw + " symbols!");
                in.close(); // close input file
            } catch (FileNotFoundException e) {
                throw e;
            } catch (NumberFormatException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            /* solve task by using two pointers approach */
            for(int i = 0; i < words.size();){
                int l = i, r = i+1, len = words.get(i).length();
                while(r < words.size() && len + words.get(r).length() + 1 <= maxw){
                    len += words.get(r).length() + 1;
                    r++;
                }
                len -= (r - l - 1);
                int space_len = (maxw - len) / Math.max(r - l - 1, 1), additional = (maxw - len) % Math.max(r - l - 1, 1); // needed amount of spaces between words and additional ones
                if(r < words.size() && r - l > 1){
                    while(l < r){
                        out.write(words.get(l));
                        for(int j = 0; j < space_len; j++)
                            out.write(' ');
                        if(additional > 0)
                            out.write(' ');
                        additional = Math.max(additional - 1, 0);
                        l++;
                    }
                    out.write("\n");
                }
                else{
                    while(l < r){
                        out.write(words.get(l) + ' ');
                        l++;
                    }
                    if(r < words.size())
                        out.write("\n");
                }
                i = r;

            }
        } catch(FileNotFoundException e) {
            out.write("Exception, file not found!");
        }
        catch(EmptyFileException | TextSizeException | NoLineException | WidthValueException | EmptyWordException | InvalidSymbolException | WordSizeException | WidthSizeException e){
            out.write(e.getMessage());
        }

        out.close();
    }
}
class EmptyFileException extends Exception{
    public EmptyFileException(String message) {
        super(message);
    }
}
class TextSizeException extends Exception{
    public TextSizeException(String message) {
        super(message);
    }
}
class NoLineException extends Exception{
    public NoLineException(String message) {
        super(message);
    }
}
class WidthValueException extends Exception{
    public WidthValueException(String message) {
        super(message);
    }
}
class EmptyWordException extends Exception{
    public EmptyWordException(String message) {
        super(message);
    }
}

class InvalidSymbolException extends Exception{
    public InvalidSymbolException(String message) {
        super(message);
    }
}

class WordSizeException extends Exception{
    public WordSizeException(String message) {
        super(message);
    }
}
class WidthSizeException extends Exception{
    public WidthSizeException(String message) {
        super(message);
    }
}