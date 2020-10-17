import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;

/**
 * Created by Daniel Schechtman on 3/13/2017.
 */
public class BitMapGUI{

    ArrayList<ArrayList<PixelButton>> buttonGrid = new ArrayList<>();
    AdvancedWindows advancedPanes;
    AdvancedWindows colorPanes;

    // these int referances will be used in both
    // this class and the PixelButton class
    // to control the color of the pixels in the
    // bit map editor gui
    private Int r = new Int(0);
    private Int g = new Int(0);
    private Int b = new Int(0);

    // these are here to fix a bug
    // that happens because the sliders
    // and the color palet buttons don't
    // communicate with eachother which leads to
    // weird colors when you use the sliders after
    // the using the color palet
    private int backup_r;
    private int backup_g;
    private int backup_b;

    // stores the pixels and creates the bit map
    private Icon icon;

    private JPanel []rgb_preview = new JPanel[11];
    private JSlider []rgbSlider = new JSlider[3];

    JCheckBox cb = new JCheckBox("Advanced");

    // makes the body for the GUI
    private JFrame mainFrame = new JFrame("Bit Map Editor");

    public BitMapGUI(int rows, int cols) {
        // used for the advanced options
        advancedPanes = new AdvancedWindows(r, g, b, buttonGrid);

        // used for the coloring options
        colorPanes = new AdvancedWindows(r, g, b, buttonGrid, "Color Options", 2);

        // this will get the size screen this program
        // is being run on so the GUI can go into full screen
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();

        for(int i = 0; i < 3; i++){
            rgbSlider[i] = new JSlider(0, 255, 0);
        }

        for(int i = 0; i < rgb_preview.length; i++){
            rgb_preview[i] = new JPanel();
        }

        setAdvancedWindow();
        setColorWindow();

        // this is here to protect against buggy values
        if(rows < 1){
            rows = 1;
        }
        if(cols < 1){
            cols = 1;
        }

        // generates the pixels
        icon = new Icon(rows, cols);

        // the GUI itself must be bigger the
        // the bit map size, since it needs to
        // have 3 slider for color, a check box for
        // advanced options, a button to create the bitmap,
        // and a list of colors the user has used previously
        // so these will store the size of the bit map itself
        // to avoid bugs in the program
        int temp_cols = cols;
        int temp_rows = rows;

        // this will be a Label for each slider
        String []rgb = {"Red", "Green", "Blue"};

        // this will store the 5 most recent colors the user
        // has used
        PixelButton []colorList = new PixelButton[5];

        for(int i = 0; i < rgb_preview.length; i++) {
            rgb_preview[i].setBackground(new Color(0, 0, 0));
        }

        // generates a button for these 5 colors
        for(int i = 0; i < 5; i++){
            colorList[i] = new PixelButton(r, g, b, new Pixel());
            colorList[i].addActionListener(colorList[i]);
            colorList[i].setSliders(rgbSlider);
            colorList[i].setPreview(rgb_preview);
        }
        

        // these will make sure the GUI itself is always big enough to
        // store all of the neccesary UI elements
        if(cols < 5){
            cols = 5;
        }
        if(rows < 4){
            rows = 4;
        }

        // sets a GUI with a GridLayout that will
        // always at least be a 7x6
        // I want 7 rows because I add a row of invisible
        // buttons between the bitmap part of the GUI
        // and the Previous Color list part to make
        // the UI look cleaner
        // and I add an extra column because of the sliders
        // other UI stuff in the GUI
        mainFrame.setLayout(new GridLayout(rows+2, cols+1));

        addBitMap(rows, cols, temp_cols, temp_rows, colorList, rgb);

        // set to cols + 1 so that the correct padding
        // and color list can be added to the JFrame
        temp_cols = cols + 1;
        for(int i = 0; i < temp_cols; i++){
            JButton b = new JButton();
            b.setVisible(false);
            mainFrame.add(b);
        }


        // adds the 5 most previous used color buttons
        // and padding for the rest of the row
        for(int i = 0; i < temp_cols; i++){
            if(i < 5){
                mainFrame.add(colorList[i]);
            }
            else {
                JButton b = new JButton();
                b.setVisible(false);
                mainFrame.add(b);
            }
        }


        // sets the size to roughly the size of the screen
        mainFrame.setSize(d.width, d.height-100);

        // centers the GUI
        mainFrame.setLocationRelativeTo(null);


        mainFrame.setVisible(true);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void addBitMap(int rows, int cols, int temp_cols, int temp_rows, PixelButton []colorList, String []rgb){
        // this will tell the program if it still needs to add a UI element
        int addUIEl = 0;

        for(int i = 0; i < rows ; i++){
            if(i < temp_rows){
                buttonGrid.add(new ArrayList<>());
            }
            for(int j = 0; j < temp_cols; j++){
                // this checks to see if the x coordinate
                // is within the bounds of the icon object
                if(i < temp_rows) {
                    PixelButton pg = new PixelButton(i, j, r, g, b, icon.getPixel(i, j), colorList);
                    buttonGrid.get(i).add(pg);
                    pg.setPreview(rgb_preview);
                    pg.setSliders(rgbSlider);
                    pg.setCheckBox(cb);
                    pg.setAdvancedWindow(advancedPanes);
                    pg.addActionListener(pg);
                    pg.setPixelColor(255, 255, 255);
                    mainFrame.add(pg);
                }

                // since the bitmap can be smaller then 7x6 pixels
                // this checks to see if more padding is needed to
                // keep the UI looking clean
                if (j + 1 == temp_cols) {
                    // starts off at where the edge of the bitmap UI
                    // ends
                    int addButton = temp_cols;

                    // if the bitmap has less than 5 rows
                    // start at the beginning of the JFrame
                    if(i >= temp_rows){
                        addButton = 0;
                    }

                    // will keep adding padding from the starting point
                    // until it reaches the end of the JFrame
                    for (; addButton < cols; addButton++) {
                        JButton b = new JButton();
                        b.setVisible(false);
                        mainFrame.add(b);
                    }
                }
            }

            // this if statement will create the sliders
            if(addUIEl < 3){

                // allows for the slider label to be shown
                rgbSlider[addUIEl].setPaintLabels(true);

                // sets the action of the slider
                rgbSlider[addUIEl].addChangeListener(new ChangeListener() {
                    @Override
                    public void stateChanged(ChangeEvent e) {

                        // gets the slider that calls this method
                        JSlider slider = (JSlider) e.getSource();

                        // gets the label of the slider
                        Dictionary label = slider.getLabelTable();
                        String color = ((Hashtable<Integer, JLabel>) label).get(0).getText();

                        // figures out what slider was called
                        // based on the label
                        // then sets the appropriate backup variable
                        // and color variable
                        if(color.equals("Red")){
                            backup_r = slider.getValue();
                            r.set(slider.getValue());
                            g.set(backup_g);
                            b.set(backup_b);
                        }
                        else if(color.equals("Green")){
                            backup_g = slider.getValue();
                            r.set(backup_r);
                            g.set(slider.getValue());
                            b.set(backup_b);
                        }
                        else {
                            backup_b = slider.getValue();
                            r.set(backup_r);
                            g.set(backup_g);
                            b.set(slider.getValue());
                        }

                        // since there are more then 1 display panel change all of their
                        // color to the same thing
                        for(int i = 0; i < rgb_preview.length; i++) {
                            rgb_preview[i].setBackground(new Color(r.get(), g.get(), b.get()));
                        }
                    }
                });

                // creates the label
                Hashtable<Integer, JLabel> labelTable = new Hashtable<>();
                labelTable.put(0, new JLabel(rgb[addUIEl]));

                rgbSlider[addUIEl].setLabelTable(labelTable);

                colorPanes.getFrame(0).add(rgbSlider[addUIEl]);

                if(addUIEl == 0){
                    JButton b = new JButton("Color Options");
                    b.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            colorPanes.show();
                        }
                    });
                    mainFrame.add(b);
                }
                else if(addUIEl == 1){
                    mainFrame.add(rgb_preview[0]);
                }
                else if(addUIEl == 2){
                    colorPanes.getFrame(0).add(rgb_preview[1]);
                    colorPanes.getFrame(0).add(colorPanes.returnButton("Next"));
                    mainFrame.add(cb);
                }


                addUIEl++;
            }
            else if(addUIEl == 3){
                // creates a button to generate a bitmap
                JButton b = new JButton("Create Bit Map");
                b.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        icon.createBitMap();

                        // gets the path to the bitmap created by Icon
                        String path = System.getProperty("user.dir") + "/";
                        String dirc = "Bit Map Folder/";
                        String bmpFile = "icon.bmp";

                        // will be used to rename the file
                        String newfileName = JOptionPane.showInputDialog("Enter a name for the file");

                        // will generate a new name so there aren't two BitMap.bmp
                        if(newfileName == null || newfileName.equals(bmpFile) || newfileName.isEmpty()){
                            newfileName = "BitMap.bmp";
                            File file = new File(path+dirc+bmpFile);
                            int newName = 0;
                            while(file.exists()){
                                newfileName = "BitMap"+Integer.toString(newName)+".bmp";
                                file = new File(path+dirc+newfileName);
                                newName++;
                            }
                        }

                        // will rename the file to the name the
                        // user asked
                        else {
                            newfileName += ".bmp";
                        }

                        File file1 = new File(path+dirc+bmpFile);
                        File file2 = new File(path+dirc+newfileName);
                        file1.renameTo(file2);

                        // There seems to be a bug and Icon.bmps are still
                        // being made, I don't really want to hunt it down
                        // at the moment
                        // so I just decided to have the program
                        // check for any Icon.bmp and delete it
                        if(file1.exists()){
                            file1.delete();
                        }
                        for(int i = 0; i < 25; i++){
                            bmpFile = "icon"+Integer.toString(i)+".bmp";
                            file1 = new File(path+dirc+bmpFile);
                            if(file1.exists()){
                                file1.delete();
                            }
                        }
                    }
                });
                mainFrame.add(b);
                addUIEl++;
            }
            else {
                // adds padding
                JButton b = new JButton();
                b.setVisible(false);
                mainFrame.add(b);
            }
        }
    }

    // Initalizes the Frame for the Advanced options
    private void setAdvancedWindow(){
        JFrame frame = advancedPanes.getFrame(0);

        JTextField rows = new JTextField("Row");
        JTextField cols = new JTextField("Column");
        rows.setColumns(7);
        cols.setColumns(7);

        JButton fill = new JButton("Enter");

        // this ActionListener will get input from the user
        // and from the pixel they select, set all rows
        // at and below that selected pixel to a certain
        // color and set all columns at and to the right
        // of that pixel to a certian color
        fill.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row;
                int col;

                // ensures the user can't put in a buggy value
                try {
                    row = Integer.valueOf(rows.getText());
                    col = Integer.valueOf(cols.getText());
                }catch (Exception ex){
                    row = 0;
                    col = 0;
                }


                for(int i = advancedPanes.getCoordRow(); i < advancedPanes.getCoordRow() + row; i++){
                    for(int j = advancedPanes.getCoordCol(); j < advancedPanes.getCoordCol() + col; j++){

                        // makes sure that the user doesn't break the program if
                        // they enter a too big a value for either the row or the column
                        try {
                            advancedPanes.getGrid().get(i).get(j).setPixelColor(r.get(), g.get(), b.get());
                        }catch (Exception ex){}
                    }
                }
            }
        });

        frame.add(rows);
        frame.add(cols);
        frame.add(fill);

        frame.add(advancedPanes.returnButton("Next"));
        frame.setSize(500, 500);

        // centers the JFrame to the middle of the screen
        frame.setLocationRelativeTo(null);

        // makes sure the JFrame will close without closing the
        // entire program
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        frame = advancedPanes.getFrame(1);
        frame.add(advancedPanes.returnButton("Previous"));

        JButton getFile = new JButton("Get Bit Map");

        // This actionListener will get the bitmap the user
        // selects, copy it over to a new Icon, and then
        // start writing it to the current BitMap from
        // the pixel selected
        getFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // creates a way to go into the file system
                // to get a bitmap
                JFileChooser fc = new JFileChooser();

                // initally starts the file system at the home directory
                // but will change that to the directory the user last
                // found a bitmap
                fc.setCurrentDirectory(new File(advancedPanes.getPath()));

                //makes sure the sure has selected a bitmap
                int result = fc.showOpenDialog(new JFrame());

                if(result == JFileChooser.APPROVE_OPTION){

                    // gets the bitmap file
                    File selectedFile = fc.getSelectedFile();

                    // sets the path to the directory where the bitmap was found
                    advancedPanes.setPath(selectedFile.getAbsolutePath());

                    Icon icon = Icon.createIcon(selectedFile.getAbsolutePath());

                    // this will make a PNG file for the preview of the file
                    // temporarily saves that file to a Bit Map Folder that is either there
                    // or is created in the current directery by png
                    File png = new File(System.getProperty("user.dir")+"/Bit Map Folder/");
                    if(!png.isDirectory()){
                        png.mkdir();
                    }
                    png = new File(System.getProperty("user.dir")+"/Bit Map Folder/temp.png");

                    // Turns the BMP file found into a PNG file
                    try {
                        BufferedImage image = ImageIO.read(selectedFile);

                        ImageIO.write(image, "png", png);
                    }
                    catch (IOException ex){}

                    // Creates a new Window for the preview
                    JFrame j = new JFrame("Preview");
                    j.setLayout(new GridBagLayout());
                    GridBagConstraints c = new GridBagConstraints();

                    // enlarges the image so it is viewable in the preview
                    ImageIcon ii = new ImageIcon(png.getAbsolutePath());
                    ii = new ImageIcon(ii.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH));


                    // adds the button and the image to the preview
                    JLabel jl = new JLabel(ii);
                    JButton b = new JButton("OK");

                    // this will write the bitmap to the current bitmap
                    // when the user presses OK
                    b.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            for(int i = 0; i < icon.getRowSize(); i++){
                                for(int j = 0; j < icon.getColSize(); j++) {
                                    try {
                                        Pixel p = icon.getPixel(i, j);
                                        advancedPanes.getGrid().get(i+advancedPanes.getCoordRow()).get(j+advancedPanes.getCoordCol()).setPixelColor(p.getRed(), p.getGreen(), p.getBlue());
                                    }catch (Exception ex){}
                                }
                            }
                            j.dispatchEvent(new WindowEvent(j, WindowEvent.WINDOW_CLOSING));
                        }
                    });


                    j.setSize(700, 700);
                    j.setLocationRelativeTo(null);
                    c.gridx = 0;
                    c.gridy = 0;
                    c.ipadx = 100;
                    c.ipady = 100;
                    j.add(jl, c);

                    c.ipady = 20;
                    c.gridx = 0;
                    c.gridy = 1;

                    j.add(b, c);

                    JButton cancel = new JButton("Cancel");
                    cancel.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            j.dispatchEvent(new WindowEvent(j, WindowEvent.WINDOW_CLOSING));
                        }
                    });
                    c.gridy = 2;
                    j.add(cancel, c);
                    j.setVisible(true);

                    // removes the preview image
                    png.delete();
                }
            }

        });

        frame.add(getFile);
        frame.setSize(500, 500);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    }

    // This initializes the window for the color options
    private void setColorWindow(){
        JFrame frame = colorPanes.getFrame(0);

        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        // This is for the color picker, I will make 40 buttons
        // and give each of them a different RGB value
        ArrayList<PixelButton> buttons = new ArrayList<>();

        for(int i = 0; i < 40; i++){
            buttons.add(new PixelButton(r, g, b, new Pixel()));
            buttons.get(i).setSliders(rgbSlider);
            buttons.get(i).setPreview(rgb_preview);
            buttons.get(i).addActionListener(buttons.get(i));
        }

        buttons.get(0).setPixelColor(0, 0, 0);
        buttons.get(1).setPixelColor(255, 0, 0);
        buttons.get(2).setPixelColor(0, 255, 0);
        buttons.get(3).setPixelColor(0, 0, 255);
        buttons.get(4).setPixelColor(255, 255, 255);
        buttons.get(5).setPixelColor(153, 0, 153);
        buttons.get(6).setPixelColor(128, 128, 128);
        buttons.get(7).setPixelColor(51, 0, 0);
        buttons.get(8).setPixelColor(0, 51, 0);
        buttons.get(9).setPixelColor(0, 0, 51);
        buttons.get(10).setPixelColor(102, 255, 102);
        buttons.get(11).setPixelColor(204, 102, 0);
        buttons.get(12).setPixelColor(153, 51, 255);
        buttons.get(13).setPixelColor(0, 76, 153);
        buttons.get(14).setPixelColor(255, 255, 0);
        buttons.get(15).setPixelColor(255, 0, 255);
        buttons.get(16).setPixelColor(0, 255, 255);
        buttons.get(17).setPixelColor(0, 153, 153);
        buttons.get(18).setPixelColor(153, 0, 76);
        buttons.get(19).setPixelColor(153, 153, 255);
        buttons.get(20).setPixelColor(175, 238,238);
        buttons.get(21).setPixelColor(3,180,204);
        buttons.get(22).setPixelColor(139, 69, 19);
        buttons.get(23).setPixelColor(245, 245, 220);
        buttons.get(24).setPixelColor(165, 42, 42);
        buttons.get(25).setPixelColor(92, 64, 51);
        buttons.get(26).setPixelColor(210,105,30);
        buttons.get(27).setPixelColor(210,180,140);
        buttons.get(28).setPixelColor(255, 140, 0);
        buttons.get(29).setPixelColor(233, 150, 122);
        buttons.get(30).setPixelColor(255, 165, 0);
        buttons.get(31).setPixelColor(255, 20, 147);
        buttons.get(32).setPixelColor(255, 162, 173);
        buttons.get(33).setPixelColor(79, 7, 79);
        buttons.get(34).setPixelColor(205, 205, 193);
        buttons.get(35).setPixelColor(217, 217, 243);
        buttons.get(36).setPixelColor(184, 134, 11);
        buttons.get(37).setPixelColor(238, 232, 170);
        buttons.get(38).setPixelColor(142, 35, 35);
        buttons.get(39).setPixelColor(255, 99, 71);

        colorPanes.resetGrid(1, 6, 10);

        frame = colorPanes.getFrame(1);
        frame.add(colorPanes.returnButton("Prev"));
        for(int i = 2; i < rgb_preview.length; i++) {
            frame.add(rgb_preview[i]);
        }

        for(int i = 0; i < 10; i++) {
            JButton b = new JButton();
            b.setVisible(false);
            frame.add(b);
        }

        for(PixelButton pb : buttons){
            frame.add(pb);
        }


        frame.setSize(800, 800);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
    }

}