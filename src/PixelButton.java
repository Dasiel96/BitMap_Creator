import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Daniel Schechtman on 3/13/2017.
 */
public class PixelButton extends JButton implements ActionListener{

    // these will be used to change the color of the pixel associated with this button
    // and the background of this button
    private Int r;
    private Int g;
    private Int b;

    private int row;
    private int col;

    private Pixel p;

    // this will be used to get the index of
    // the button that need to be changed
    // to record the color the user of this program
    // just used
    private static int index = 0;

    // stores a list of recent colors
    private PixelButton[]pix_list;

    // determines if the pixel being clicked on is apart of the
    // icon object or not
    private boolean partOfBitMap = false;

    // determines if the pixel in the list of most recent
    // colors used has a color stored in it, so the user
    // can't use it until a color has been stored in it
    private boolean colorAdded = false;

    // place holder for the preview and slider component
    private JPanel []preview = null;
    private JSlider[] rgbSliders = null;
    private JCheckBox cb = null;
    private AdvancedWindows aw = null;

    public PixelButton(int row, int col, Int red, Int green, Int blue, Pixel pix, PixelButton[]pix_list){
        super();

        r = red;
        g = green;
        b = blue;

        partOfBitMap = true;

        p = pix;

        this.pix_list = pix_list;

        this.row = row;
        this.col = col;

        setBackground(new Color(p.getRed(), p.getGreen(), p.getBlue()));
    }

    public PixelButton(Int r, Int g, Int b, Pixel pix){
        super();

        this.r = r;
        this.g = g;
        this.b = b;

        p = pix;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        // gets the button the calls this method
        PixelButton pg = (PixelButton) e.getSource();
        if(partOfBitMap) {

            // this will be used to determine if a color need to be added to the recent color list
            boolean addToColorChart = true;

            if(cb.isSelected()){
                aw.setCoord(row, col);
                aw.show();
                cb.setSelected(false);
                colorAdded = false;
            }

            if(colorAdded){
                // sets the button background color and pixel color
                pg.setPixelColor(r.get(), g.get(), b.get());
            }

            // checks each entry to make sure there is no color like the
            // one the user is currently using in the list already
            for (PixelButton pg2 : pix_list) {
                if (pg2.getPixel().equals(pg.getPixel())) {
                    addToColorChart = false;
                    break;
                }
            }

            if (addToColorChart && colorAdded) {
                Pixel p = pg.getPixel();
                pix_list[index % 5].setPixelColor(p.getRed(), p.getGreen(), p.getBlue());
                index++;
            }

            colorAdded = true;
        }
        else {

            // resets the rgb value to whatever color is in the assoicated pixel
            // and changes the preview panels to be that color
            if(colorAdded) {
                Pixel p = pg.getPixel();
                r.set(p.getRed());
                g.set(p.getGreen());
                b.set(p.getBlue());
                rgbSliders[0].setValue(p.getRed());
                rgbSliders[1].setValue(p.getGreen());
                rgbSliders[2].setValue(p.getBlue());
                for(int i = 0; i < preview.length; i++) {
                    preview[i].setBackground(new Color(p.getRed(), p.getGreen(), p.getBlue()));
                }
            }
        }

    }

    public void setPreview(JPanel []b){
        preview = b;
    }

    public void setSliders(JSlider []sliders){
        rgbSliders = sliders;
    }

    public void setCheckBox(JCheckBox c){
        cb = c;
    }

    public void setAdvancedWindow(AdvancedWindows a){
        aw = a;
    }

    public void setPixelColor(int r, int g, int b){
        p.setPixel(r, g, b);
        setBackground(new Color(r, g, b));
        colorAdded = true;
    }

    public Pixel getPixel(){
        return p;
    }
}