/**
 * Created by Daniel Schechtman on 2/15/2017.
 */

// this class was created so I can
// pass an int value int methods by reference
public class Int {
    int val;

    // constructor to set the value of the int
    // initially
    Int(int v){
        val = v;
    }

    public int get(){
        return val;
    }

    // method for addition
    public void add(int increase){
        val += increase;
    }

    // method for hard setting values
    public void set(int value){
        val = value;
    }

}
