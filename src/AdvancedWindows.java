import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

/**
 * Created by Daniel Schechtman on 3/16/2017.
 */

/*
This class will be used to make JFrames that are connected to eachother.
That is, all the JFrames in this class will have a next and a prev button,
and clicking one of those buttons will take you to another JFrame
 */
public class AdvancedWindows implements ActionListener{

    // This will hold all of the JFrames
    private ArrayList<JFrame> frames = new ArrayList<>();

    // This will be used to make the current JFrame that
    // the user was using visable. It will also serve as a
    // type of memory of which window the user last used
    private int showFrame = 0;

    // These will store the coordiantes of the Pixel that is
    // being used by this class. The Pixel will be apart of a 2d ArrayList this class has access to
    private int row_coord;
    private int col_coord;

    // The RGB values that every class that changes color has access to
    private Int r;
    private Int g;
    private Int b;

    // ArrayList of Buttons, this is where this class will
    // find the pixel it needs to work with
    private ArrayList<ArrayList<PixelButton>> pix;

    // This Class's primary reason for being created was to serve
    // as a way to handle the advanced option windows (I also use it
    // for the color sliders and color picker window)
    // But seeing what it's original purpose was, this variable will
    // be used to store the last location the user got
    // a bitmap from, so they don't have to search through their
    // file system every time they want a bitmap
    private String path = System.getProperty("user.home");


    AdvancedWindows(Int red, Int green, Int blue, ArrayList<ArrayList<PixelButton>> map){
        for(int i = 0; i < 3; i++){
            frames.add(new JFrame("Advanced Options: "+Integer.toString(i+1)));
            frames.get(i).setLayout(new FlowLayout());
        }

        r = red;
        g = green;
        b = blue;
        pix = map;

    }

    AdvancedWindows(Int red, Int green, Int blue, ArrayList<ArrayList<PixelButton>> map, String label, int windows){
        for(int i = 0; i < windows; i++){
            frames.add(new JFrame(label + ": "+Integer.toString(i+1)));
            frames.get(i).setLayout(new GridLayout(5, 0));
        }

        r = red;
        g = green;
        b = blue;
        pix = map;
    }

    // This will get the next JFrame when the user clicks a Next or Prev Button
    @Override
    public void actionPerformed(ActionEvent e) {
        JButton b = (JButton)e.getSource();
        String command = b.getActionCommand();

        // this closes the current JFrame
        frames.get(showFrame).dispatchEvent(new WindowEvent(frames.get(showFrame), WindowEvent.WINDOW_CLOSING));

        // Gets the next JFrame
        if(command.equals("Next")){
            showFrame++;
        }
        else {
            showFrame--;
        }

        // Opens the next JFrame
        frames.get(showFrame).setVisible(true);
    }


    // Resets the layout of the Layout Grid for a single JFrame
    public void resetGrid(int index, int rows, int cols){
        frames.get(index).setLayout(new GridLayout(rows, cols));
    }

    public JFrame getFrame(int index){
        return frames.get(index);
    }


    public ArrayList<ArrayList<PixelButton>> getGrid(){
        return pix;
    }

    public String getPath(){
        return path;
    }

    public void setPath(String new_path){
        path = new_path;
    }

    public int getCoordRow(){
        return row_coord;
    }

    public int getCoordCol(){
        return col_coord;
    }

    public void setCoord(int row, int col){
        row_coord = row;
        col_coord = col;
    }

    public void show(){
        frames.get(showFrame).setVisible(true);
    }

    // Makes the Next and Prev Buttons
    public JButton returnButton(String label){
        JButton retval = new JButton(label);
        retval.setActionCommand(label);
        retval.addActionListener(this);
        return retval;
    }
}
