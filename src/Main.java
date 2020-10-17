import javax.swing.*;

/**
 * Created by Daniel Schechtman on 3/10/2017.
 */
public class Main{
    public static void main(String []arg){

        String rows = JOptionPane.showInputDialog("How many rows do you want the bit map to have?");
        String cols = JOptionPane.showInputDialog("How many columns do you want the bit map to have?");

        if(rows == null){
            rows = "1";
        }
        if(cols == null){
            cols = "1";
        }

        BitMapGUI bmg = new BitMapGUI(Integer.valueOf(rows), Integer.valueOf(cols));
    }
}