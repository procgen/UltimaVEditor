import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {


    MainFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Ultima V Save Editor");
        setPreferredSize(new Dimension(500, 350));

        this.setLayout(new GridLayout(0, 1));

        this.add(makeDataField("STR", 14, 1));
        this.add(makeDataField("DEXT", 15, 1));
        this.add(makeDataField("INT", 16, 1));

        pack();
    }

    private static JPanel makeDataField(String label, int offset, int length){
        JPanel p = new JPanel(new FlowLayout());
        JLabel nameLabel = new JLabel(label + ": ", JLabel.LEADING);
        DataField nameField = new DataField(offset, length);
        p.add(nameLabel);
        p.add(nameField);
        return p;
    }

    public static void main(String args[]) {
        MainFrame f = new MainFrame();
        f.setVisible(true);
    }
}
