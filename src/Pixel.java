/**
 * Created by Daniel Schechtman on 2/13/2017.
 */
public class Pixel {

    // holds the rgb values of the pixel
    private int rgb;

    // lets a pixel be set to a initial value upon initialization
    Pixel(int r, int g, int b) {
        r = checkValue(r);
        g = checkValue(g);
        b = checkValue(b);
        b = b << 16;
        g = g << 8;
        rgb = r + g + b;
    }


    // give an initial value for every pixel
    Pixel(){
        int r = 127;
        int g = 127;
        int b = 127;
        b = b << 16;
        g = g << 8;
        rgb = r + g + b;
    }

    @Override
    public boolean equals(Object p){
        boolean retVal = false;
        if(p instanceof Pixel){
            Pixel other = (Pixel)p;
            retVal = (getRed() == other.getRed()) && (getGreen() == other.getGreen()) && (getBlue() == other.getBlue());
        }
        return retVal;
    }

    public int getRed() {

        // sets the blue byte to the 4th byte of
        // rgb (erasing everything besides the blue byte)
        int rgbCopy = rgb << 24;

        // sets the blue byte to the first byte of rgb so
        // it can be used
        rgbCopy = rgbCopy >>> 24;
        return rgbCopy;
    }

    public int getGreen() {
        int rgbCopy = rgb;

        // sets the green byte to the first slot
        // of rgb
        rgbCopy = rgbCopy >>> 8;

        // sets the green byte to the last slot
        // of rgb(erasing everything in the int besides the
        // green byte)
        rgbCopy = rgbCopy << 24;

        // sets the green byte to the first byte
        // of rgb so it can be used
        rgbCopy = rgbCopy >>> 24;
        return rgbCopy;
    }

    public int getBlue() {
        int rgbCopy = rgb;

        // sets the red to the first byte so it can be used
        rgbCopy = rgbCopy >>> 16;
        return rgbCopy;
    }

    // allows a single pixel to have all it's color
    // values changed at the same time
    public void setPixel(int r, int g, int b){
        setRed(r);
        setGreen(g);
        setBlue(b);
    }

    // will change the red value of the pixel
    public void setRed(int r) {
        changeAVal(r, getGreen(), getBlue());
    }

    // will change the green value of the pixel
    void setGreen(int g) {
        changeAVal(getRed(), g, getBlue());
    }

    // will change the blue value of the pixel
    public void setBlue(int b){
        changeAVal(getRed(), getGreen(), b);
    }

    // this reads the red, green, and blue values
    // from rgb, turns each into it's hex equivelent
    // and returns it into a string
    public String printHex(){
        String red = Integer.toHexString(getRed());
        String green = Integer.toHexString(getGreen());
        String blue = Integer.toHexString(getBlue());

        // this if statements below
        // make sure the string is in
        // #RRGGBB format
        if(red.length() == 1){
            red = "0" + Integer.toHexString(getRed());
        }
        if(green.length() == 1){
            green = "0" + Integer.toHexString(getGreen());
        }
        if(blue.length() == 1){
            blue = "0" + Integer.toHexString(getBlue());
        }
        String rgb_hex = "#" + red + green + blue;
        rgb_hex = rgb_hex.toUpperCase();
        return rgb_hex;
    }

    // since all the set methods follow the same algorithm(besides the value being changed)
    // it made more sense to make a method to do the work
    private void changeAVal(int r, int g, int b){
        // catches any bad values being passed in
        r = checkValue(r);
        g = checkValue(g);
        b = checkValue(b);

        // sets the right color to the right byte in rgb
        b = b << 16;
        g = g << 8;

        // resets rgb
        rgb = r + g + b;
    }

    // this method checks if the value being used for a pixel color
    // can be stored in a byte, and if it can't make it so
    // it can be stored in a byte
    private int checkValue(int val){
        if(val < 0){
            val = 0;
        }
        else if(val > 255){
            val = 255;
        }
        return val;
    }
}