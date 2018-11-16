import javax.swing.*;
import java.awt.*;

public class JPanelImage extends JPanel {
    ImageIcon imageIcon;
    String nameImage;

    /**
     * Constructor for JPanelImage
     * @param nameImage
     */
    JPanelImage(String nameImage){
        if(nameImage!=null){
            this.nameImage = nameImage;
        }
    }

    /**
     * Refresh window
     * @param graphics graphics
     */
    public void paint(Graphics graphics){
        imageIcon = new ImageIcon(getClass().getResource(this.nameImage));
        graphics.drawImage(imageIcon.getImage(), 0, 0, (int)getSize().getWidth(), (int)getSize().getHeight(), null);
        setOpaque(false);
        super.paint(graphics);
    }
}