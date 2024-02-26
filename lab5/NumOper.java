package lab5;
import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;

public class NumOper {
    public static void main(String[] args) throws IOException, DataException, OperationException, NumbersException, MathException {
        try (BufferedReader in = new BufferedReader(new FileReader("input.txt"))) {
            try (BufferedWriter out = new BufferedWriter(new FileWriter("output.txt"))) {
                ArrayList<Integer> ops = new ArrayList<>(); // list of operations
                ArrayList<Number> nums = new ArrayList<>(); // list of numbers
                boolean was3 = false; // if operations include division or not
                String o = in.readLine(), cur = "";
                // read data
                if (o == null)
                    throw new DataException("Exception: Invalid data");
                try {
                    for (int i = 0; i < o.length(); i++) {
                        if (o.charAt(i) == ' ' || o.charAt(i) == ',') {
                            if (cur.isEmpty())
                                throw new DataException("Exception: Invalid data");
                            ops.add(Integer.parseInt(cur));
                            cur = "";
                        } else if ((o.charAt(i) >= '0' && o.charAt(i) <= '9') || o.charAt(i) == '-')
                            cur += o.charAt(i);
                        else
                            throw new DataException("Exception: Invalid data");
                    }
                    if (!cur.isEmpty())
                        ops.add(Integer.parseInt(cur));
                    for (int op : ops) {
                        if (op < 1 || op > 6)
                            throw new OperationException("Exception: Non-existing operation");
                        if (op == 3)
                            was3 = true;
                    }
                    // if operations list size is ok or not
                    if (ops.size() < 1 || ops.size() > 10)
                        throw new OperationException("Exception: The list of operations has an invalid length");
                    o = in.readLine();
                    if (o == null)
                        throw new DataException("Exception: Invalid data");
                    cur = "";
                    // read and check data for validity
                    for (int i = 0; i < o.length(); i++) {
                        char u = o.charAt(i);
                        if (u == ' ' || u == ',') {
                            if (cur.isEmpty())
                                throw new DataException("Exception: Invalid data");
                            boolean p = false; // if string already has point
                            for (int j = 0; j < cur.length() - 2; j++) {
                                if ((cur.charAt(j) < '0' || cur.charAt(j) > '9') && !(cur.charAt(j) == '.' && !p) && !(j == 0 && cur.charAt(j) == '-'))
                                    throw new DataException("Exception: Invalid data");
                                if (cur.charAt(j) == '.')
                                    p = true;
                            }
                            if (cur.length() == 1 && (cur.charAt(0) >= '0' && cur.charAt(0) <= '9'))
                                nums.add(Integer.parseInt(cur));
                            else if(cur.length() == 1)
                                throw new DataException("Exception: Invalid data");
                            else if (cur.length() == 2) {
                                if (cur.charAt(0) == '-' && (cur.charAt(1) >= '0' && cur.charAt(1) <= '9'))
                                    nums.add(Integer.parseInt(cur));
                                else if ((cur.charAt(1) == 'F' || cur.charAt(1) == 'f') && (cur.charAt(0) >= '0' && cur.charAt(0) <= '9'))
                                    nums.add(Float.parseFloat(cur.substring(0, 1)));
                                else if ((cur.charAt(1) == 'L' || cur.charAt(1) == 'l') && (cur.charAt(0) >= '0' && cur.charAt(0) <= '9'))
                                    nums.add(Long.parseLong(cur.substring(0, 1)));
                                else if ((cur.charAt(1) >= '0' && cur.charAt(1) <= '9') && ((cur.charAt(0) >= '0' && cur.charAt(0) <= '9'))) {
                                    if (p) {
                                        nums.add(Double.parseDouble(cur));
                                    } else {
                                        nums.add(Integer.parseInt(cur));
                                    }
                                } else
                                    throw new DataException("Exception: Invalid data");
                            } else if (cur.length() > 2) {
                                if(cur.charAt(0) == '.' || (cur.charAt(0) == '-' && !(cur.charAt(1) >= '0' && cur.charAt(1) <= '9')))
                                    throw new DataException("Exception: Invalid data");
                                char v = cur.charAt(cur.length() - 2), w = cur.charAt(cur.length() - 1);
                                if ((v == 'B' && w == 'I') || (v == 'b' && w == 'i'))
                                    nums.add(new BigInteger(cur.substring(0, cur.length() - 2)));
                                else if ((v == 'B' && w == 'D') || (v == 'b' && w == 'd'))
                                    nums.add(new BigDecimal(cur.substring(0, cur.length() - 2)));
                                else if ((v >= '0' && v <= '9') && (w == 'F' || w == 'f') && cur.length() <= 15)
                                    nums.add(Float.parseFloat(cur.substring(0, cur.length() - 1)));
                                else if ((v >= '0' && v <= '9') && (w == 'L' || w == 'l') && new BigInteger(cur.substring(0, cur.length() - 1)).abs().compareTo(new BigInteger("9223372036854775807")) < 1)
                                    nums.add(Long.parseLong(cur.substring(0, cur.length() - 1)));
                                else if ((v == '.' && !p) && (w >= '0' && w <= '9'))
                                    nums.add(Double.parseDouble(cur));
                                else if ((v >= '0' && v <= '9') && (w >= '0' && w <= '9')) {
                                    if (p)
                                        nums.add(Double.parseDouble(cur));
                                    else if (Math.abs(Long.parseLong(cur)) < Long.parseLong("2147483647"))
                                        nums.add(Integer.parseInt(cur));
                                    else
                                        throw new DataException("Exception: Invalid data");
                                } else
                                    throw new DataException("Exception: Invalid data");
                            }
                            cur = "";
                        } else
                            cur += o.charAt(i);
                    }
                    if (!cur.isEmpty()) {
                        boolean p = false; // if string already has a point or not
                        for (int j = 0; j < cur.length() - 2; j++) {
                            if ((cur.charAt(j) < '0' || cur.charAt(j) > '9') && !(cur.charAt(j) == '.' && !p) && !(j == 0 && cur.charAt(j) == '-'))
                                throw new DataException("Exception: Invalid data");
                            if (cur.charAt(j) == '.')
                                p = true;
                        }
                        if (cur.length() == 1 && (cur.charAt(0) >= '0' && cur.charAt(0) <= '9'))
                            nums.add(Integer.parseInt(cur));
                        else if (cur.length() == 2) {
                            if (cur.charAt(0) == '-' && (cur.charAt(1) >= '0' && cur.charAt(1) <= '9'))
                                nums.add(Integer.parseInt(cur));
                            else if ((cur.charAt(1) == 'F' || cur.charAt(1) == 'f') && (cur.charAt(0) >= '0' && cur.charAt(0) <= '9'))
                                nums.add(Float.parseFloat(cur.substring(0, 1)));
                            else if ((cur.charAt(1) == 'L' || cur.charAt(1) == 'l') && (cur.charAt(0) >= '0' && cur.charAt(0) <= '9'))
                                nums.add(Long.parseLong(cur.substring(0, 1)));
                            else if ((cur.charAt(1) >= '0' && cur.charAt(1) <= '9') && ((cur.charAt(0) >= '0' && cur.charAt(0) <= '9'))) {
                                if (p) {
                                    nums.add(Double.parseDouble(cur));
                                } else {
                                    nums.add(Integer.parseInt(cur));
                                }
                            } else
                                throw new DataException("Exception: Invalid data");
                        } else if (cur.length() > 2) {
                            if(cur.charAt(0) == '.' || (cur.charAt(0) == '-' && !(cur.charAt(1) >= '0' && cur.charAt(1) <= '9')))
                                throw new DataException("Exception: Invalid data");
                            char v = cur.charAt(cur.length() - 2), w = cur.charAt(cur.length() - 1);
                            if ((v == 'B' && w == 'I') || (v == 'b' && w == 'i'))
                                nums.add(new BigInteger(cur.substring(0, cur.length() - 2)));
                            else if ((v == 'B' && w == 'D') || (v == 'b' && w == 'd'))
                                nums.add(new BigDecimal(cur.substring(0, cur.length() - 2)));
                            else if ((v >= '0' && v <= '9') && (w == 'F' || w == 'f') && cur.length() <= 15)
                                nums.add(Float.parseFloat(cur.substring(0, cur.length() - 1)));
                            else if ((v >= '0' && v <= '9') && (w == 'L' || w == 'l') && new BigInteger(cur.substring(0, cur.length() - 1)).abs().compareTo(new BigInteger("9223372036854775807")) < 1)
                                nums.add(Long.parseLong(cur.substring(0, cur.length() - 1)));
                            else if ((v == '.' && !p) && (w >= '0' && w <= '9'))
                                nums.add(Double.parseDouble(cur));
                            else if ((v >= '0' && v <= '9') && (w >= '0' && w <= '9')) {
                                if (p)
                                    nums.add(Double.parseDouble(cur));
                                else if (Math.abs(Long.parseLong(cur)) < Long.parseLong("2147483647"))
                                    nums.add(Integer.parseInt(cur));
                                else
                                    throw new DataException("Exception: Invalid data");
                            } else
                                throw new DataException("Exception: Invalid data");
                        }
                    }
                    // if numbers list size is ok or not
                    if (nums.size() < 1 || nums.size() > 20)
                        throw new NumbersException("Exception: The list of numbers has an invalid length");
                    o = in.readLine();
                    if ((was3 && o == null) || (!was3 && o != null))
                        throw new DataException("Exception: Invalid data");
                    double d = 0; // divisor
                    boolean p_p = false; // if string already has a point or not
                    cur = "";
                    if (o != null) {
                        for (int i = 0; i < o.length(); i++) {
                            if ((o.charAt(i) < '0' || o.charAt(i) > '9') && !(i == 0 && o.charAt(i) == '-') && !(o.charAt(i) == '.' && !p_p))
                                throw new DataException("Exception: Invalid data");
                            else {
                                cur += o.charAt(i);
                                if (o.charAt(i) == '.')
                                    p_p = true;
                            }

                        }
                    }
                    if (o != null)
                        d = Double.parseDouble(cur);
                    // calculate needed operations
                    for (int op : ops) {
                        try {
                            switch (op) {
                                case 1: // sum of all numbers in the list
                                    double sum = 0;
                                    for (Number num : nums)
                                        sum += num.doubleValue();
                                    out.write(sum + "\n");
                                    break;
                                case 2: // product of all numbers in the list
                                    double prod = 1;
                                    for (Number num : nums)
                                        prod *= num.doubleValue();
                                    out.write(prod + "\n");
                                    break;
                                case 3: // divide all numbers in the list by third line number
                                    if (Math.abs(d) <= 1e-14)
                                        throw new MathException("Exception: Division by 0\n");
                                    for (int i = 0; i < nums.size(); i++) {
                                        nums.set(i, nums.get(i).doubleValue() / d);
                                        out.write(String.valueOf(nums.get(i)));
                                        if (i < nums.size() - 1)
                                            out.write(", ");
                                    }
                                    out.write("\n");
                                    break;
                                case 4: // absolute average
                                    double sm = 0;
                                    for (Number num : nums)
                                        sm += Math.abs(num.doubleValue());
                                    double av = sm / nums.size();
                                    out.write(av + "\n");
                                    break;
                                case 5: // square root
                                    for (int i = 0; i < nums.size(); i++) {
                                        if (nums.get(i).doubleValue() < 0)
                                            throw new MathException("Exception: Square root cannot be calculated for negative value\n");
                                    }
                                    for (int i = 0; i < nums.size(); i++) {
                                        nums.set(i, Math.sqrt(nums.get(i).doubleValue()));
                                        out.write(String.valueOf(nums.get(i)));
                                        if (i < nums.size() - 1)
                                            out.write(", ");
                                    }
                                    out.write("\n");
                                    break;
                                case 6: // delete all negative values
                                    for (int i = 0; i < nums.size(); i++) {
                                        if (nums.get(i).doubleValue() < 0) {
                                            nums.remove(i);
                                            i--;
                                        }
                                    }
                                    for (int i = 0; i < nums.size(); i++) {
                                        out.write(String.valueOf(nums.get(i)));
                                        if (i < nums.size() - 1)
                                            out.write(", ");
                                    }
                                    out.write("\n");
                                    break;
                            }
                        } catch (MathException e) {
                            out.write(e.getMessage());
                        }
                    }
                } catch (OperationException | NumbersException | DataException e) {
                    out.write(e.getMessage());
                }
                in.close();
                out.close();
            }
        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}

class OperationException extends Exception { // non-existing operation or list of operations has invalid length
    public OperationException(String message) {
        super(message);
    }
}

class NumbersException extends Exception { // list of numbers has invalid length
    public NumbersException(String message) {
        super(message);
    }
}

class DataException extends Exception { // invalid data
    public DataException(String message) {
        super(message);
    }
}

class MathException extends Exception { // square root of negative or division by 0
    public MathException(String message) {
        super(message);
    }
}