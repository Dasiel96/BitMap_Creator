import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;

/**
 * Created by Daniel Schechtman on 2/14/2017.
 */
public class Icon {
    // this will store all of the pixels into a 2d arraylist
    private ArrayList<ArrayList<Pixel>> bit_map = new ArrayList<>();

    private static int fn = 1;

    // this constructor will make a 40x40 bitmap
    Icon(){
        for(int i = 0; i < 40; i++){
            bit_map.add(new ArrayList<>());
            for(int j = 0; j < 40; j++){
                bit_map.get(i).add(new Pixel());
            }
        }
    }

    // this constructor will make an WxH sized bitmap
    Icon(int row, int column){
        for(int i = 0; i < row; i++){
            bit_map.add(new ArrayList<>());
            for(int j = 0; j < column; j++){
                bit_map.get(i).add(new Pixel());
            }
        }
    }

    // this method will get the pixel at positon
    // column and row and return it so the user of the
    // program can manipulate the specific pixel
    public Pixel getPixel(int row, int column){
        row = checkRow(row);
        column = checkColumn(column, row);
        return bit_map.get(row).get(column);
    }

    public int getRowSize(){
        return bit_map.size();
    }

    public int getColSize(){
        return bit_map.get(0).size();
    }

    public static Icon createIcon(String path){
        Icon new_icon = null;
        try{
            // loads the bitmap file
            File file = new File(path);
            BufferedImage ir = ImageIO.read(file);

            // sets the demensions of the new icon
            new_icon = new Icon(ir.getHeight(), ir.getWidth());
            for(int i = 0; i < new_icon.getRowSize(); i++){
                for(int j = 0; j < new_icon.getColSize(); j++){

                    // gets the colors of the pixel and sets the icon pixel to that button
                    Color c = new Color(ir.getRGB(j, i));
                    new_icon.getPixel(i, j).setPixel(c.getRed(), c.getGreen(), c.getBlue());
                }
            }

        }catch (IOException e){}

        return new_icon;
    }

    // this method will generate a bmp file
    public void createBitMap(){

        // Doesn't make sense to make this a global
        // var in Icon since only createBitMap()
        // uses it
        ArrayList<Integer> bytes = new ArrayList<>();

        Int bit_map_size = new Int(0);
        createHeader(bit_map_size, bytes);
        createImageHeader(bit_map_size, bytes);

        // I made my own Integer wrapper and call it Int
        // this is because I to get a value from
        // createHeader() and use it in createImageHeader()
        // and it is easier to use a value that is passed by referance
        // rather then returning a value
        generateBMPFile(bytes);
    }

    private void createHeader(Int bms, ArrayList<Integer> bytes){

        // this will determine how much padding
        // will need to be added to the bit_map
        // to make sure the bitmap is properly formatted
        int padding_for_row = 3 * bit_map.get(0).size();

        // this will make sure padding_for_row is a multiple of 4
        while (padding_for_row%4 != 0){
            padding_for_row++;
        }

        int bit_map_size = padding_for_row * bit_map.size() + 54;

        // adds the size of the bit map so it can be used later in
        // createHeaderImage()
        bms.set(bit_map_size);

        bytes.add(66);
        bytes.add(77);

        // adds the bit_map_size to the
        // bytes ArrayList
        addIntAsByte(bytes, bit_map_size);

        for(int i = 0; i < 4; i++){
            bytes.add(0);
        }

        // adds the offset to bytes ArrayList
        addIntAsByte(bytes, 54);
    }

    private void createImageHeader(Int bms, ArrayList<Integer> bytes){

        // adds the size of the Image Header to bytes ArrayList
        addIntAsByte(bytes, 40);

        // adds the width of the bitmap image to bytes ArrayList
        addIntAsByte(bytes, bit_map.get(0).size());

        // adds the height of the bitmap image to bytes ArrayList
        addIntAsByte(bytes, bit_map.size());

        // this adds the planes of the image to the byte ArrayList
        bytes.add(1);
        bytes.add(0);

        // adds the number representation of how many bits a pixel
        // uses to the bytes ArrayList
        bytes.add(24);
        bytes.add(0);

        // adds the compression ratio
        // to bytes Array
        for (int i = 0; i < 4; i++){
            bytes.add(0);
        }


        // adds the size of the actual image
        // to the bytes ArrayList
        addIntAsByte(bytes, bms.get() - 54);

        // adds the resolution of the image to
        // the bytes ArrayList
        addIntAsByte(bytes, 2000);
        addIntAsByte(bytes, 2000);

        // adds two necessary fields to the bytes ArrayList
        // the first field this loop adds usually only
        // applies to 8-bit images
        // the second field of this image is used
        // to determine what color must be included in the image
        for(int i = 0; i < 8; i++){
            bytes.add(0);
        }

    }

    private void generateBMPFile(ArrayList<Integer> bytes){

            // this will read all the pixel RGB values
            // into the bytes ArrayList
            for (int l = bit_map.size()-1; l >= 0; l--) {
                for (int m = 0; m < bit_map.get(l).size(); m++) {
                    // I made this pixel variable
                    // to save some time on typing
                    Pixel p = bit_map.get(l).get(m);
                    bytes.add(p.getBlue());
                    bytes.add(p.getGreen());
                    bytes.add(p.getRed());
                }

                // this will actually add padding
                // to the bytes ArrayList
                // the code below will calculate the
                // number of bytes each row uses (since all rows
                // are the same size) and sees if that
                // size is a multiple of 4
                int padding = 3 * bit_map.get(l).size();
                while (padding % 4 != 0) {
                    bytes.add(0);
                    padding++;
                }
            }

        // this variable will be used to create a new
        // file if one already exists
        // I figured that if a user of this program wanted
        // to make multiple images, it'd be helpful to keep
        // overwriting the same file
        String file_name = "/icon.bmp";
        String new_dict = "/Bit Map Folder";

        // this allows the program to make the .bmp file
        // in the directory this program is run in
        String path = System.getProperty("user.dir");


        try {

            if(!new File(path+new_dict).exists()){
                new File(path+new_dict).mkdir();
            }
            // will be used to create a new file
            File file = new File(path+new_dict+file_name);

            // this if statement my not be nesscary
            // but I figured it wouldn't hurt to add
            // some extra protection against bugs
            if(!file.isDirectory()){

                // this will be used to generate a new name for a file
                // that doesn't exist
                while (file.exists()){
                    file_name = "/icon"+Integer.toString(fn)+".bmp";
                    file = new File(path+new_dict+file_name);
                    fn++;
                }
            }

            if(!file.exists()){
                file.createNewFile();
            }

            // this will be used to read an array of bytes
            // into a file
            // I originally used FileWriter and BufferedWriter
            // to do this job, but there were some bugs with
            // that, so I switched over to this method
            OutputStream op = new FileOutputStream(file);

            // create an array off bytes
            byte []write_bytes = new byte[bytes.size()];

            // copy over every byte from bytes so it can
            // be read into the file
            for(int i = 0; i < bytes.size(); i++){
                int val = bytes.get(i);
                write_bytes[i] = (byte)val;
            }
            op.write(write_bytes);
            op.close();
        }
        catch (IOException e){

        }


    }

    // breaks an int into 4 bytes and adds them to the array
    // I made this a function because it seems easier to
    // call a function then to write the same code over and over
    private void addIntAsByte(ArrayList<Integer> bytes, int data){

            // gets the first byte in the int
            int temp = data << 24;
            temp = temp >> 24;
            bytes.add(temp);

            // gets the second bytes in the int
            temp = data << 16;
            temp = temp >> 24;
            bytes.add(temp);

            // gets the third byte in the int
            temp = data << 8;
            temp = temp >> 24;
            bytes.add(temp);

            // gets the fourth byte in the int
            temp = temp >> 24;
            bytes.add(temp);
    }


    // The methods below are here so a user of this class
    // can't go outside the bound of the bit_map ArrayList
    // when they use this class

    // this is here because both checkColumn()
    // and checkRow() check if a value is below zero
    // so I decided to make a method to check for that
    private int checkVal(int val){
        if(val < 0){
            val = 0;
        }
        return val;
    }

    private int checkRow(int r){
        r = checkVal(r);
        if(r >= bit_map.size()){
            r = bit_map.size() - 1;
        }
        return r;
    }

    private int checkColumn(int c, int r){
        r = checkRow(r);
        c = checkVal(c);
        if(c >= bit_map.get(r).size()){
            c = bit_map.get(r).size() - 1;
        }
        return c;
    }
}
