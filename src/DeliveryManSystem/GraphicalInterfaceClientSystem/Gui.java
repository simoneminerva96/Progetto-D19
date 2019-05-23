package DeliveryManSystem.GraphicalInterfaceClientSystem;

import DeliveryManSystem.DeliveryManClient;

import javax.swing.*;
import java.awt.*;

public class Gui extends JFrame {

    private Dimension screenSize;
    private int width,height;
    private DeliveryManClient deliveryman;

    public Gui(DeliveryManClient deliveryman){

        this.deliveryman = deliveryman;
        ClientGUI();
    }

    public void ClientGUI() {

        //settings frame

        screenSize = getToolkit().getScreenSize();
        setLayout(new BorderLayout());
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        width = screenSize.width/6;
        height = screenSize.height/2;
        setSize(new java.awt.Dimension(width,height));
        setResizable(false);
        setTitle("Amazon for poor people");
        Image image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("Icons/amazonFakeIcon.png"));
        setIconImage(image);
        add(new LoginPanel(this , deliveryman, width , height));
        setVisible(true);

    }


}

