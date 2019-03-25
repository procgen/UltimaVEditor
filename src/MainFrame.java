import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame {

    MainFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Ultima V Save Editor");
        setPreferredSize(new Dimension(500, 350));

        this.setLayout(new GridLayout(0, 1));

        this.add(makeDataField("GOLD", 0x204, 2));
        this.add(makeDataField("KEYS", 0x206, 1));
        this.add(makeDataField("GEMS", 0x207, 1));


        this.add(makeDataField("STR", 0x0E, 1));
        this.add(makeDataField("DEXT", 0x0F, 1));
        this.add(makeDataField("INT", 0x10, 1));
        this.add(makeDataField("HP", 0x12, 2));
        this.add(makeDataField("HM", 0x14, 2));
        this.add(makeDataField("EXP", 0x16, 2));

        this.add(makeButtonPanel());


        pack();
    }

    private JPanel makeDataField(String label, int offset, int length){
        JPanel p = new JPanel(new FlowLayout());
        JLabel nameLabel = new JLabel(label + ": ", JLabel.LEADING);
        DataField nameField = new DataField(offset, length);
        p.add(nameLabel);
        p.add(nameField);
        return p;
    }

    private JPanel makeButtonPanel(){
        JPanel p = new JPanel(new FlowLayout());

        JButton refreshButton = new JButton();
        refreshButton.setText("Reload");
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                DataField.reload();
                loadFrame();
                dispose();
            }
        });

        JButton applyButton = new JButton();
        applyButton.setText("Apply");
        applyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DataField.persist();
            }
        });

        p.add(refreshButton);
        p.add(applyButton);
        return p;
    }

    public static void loadFrame(){
        MainFrame f = new MainFrame();
        f.setVisible(true);
    }

    public static void main(String args[]) {
        loadFrame();
    }
}
