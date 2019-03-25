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

        this.add(makeDataField("STR", 14, 1));
        this.add(makeDataField("DEXT", 15, 1));
        this.add(makeDataField("INT", 16, 1));


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

        this.add(p);


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

    public static void loadFrame(){
        MainFrame f = new MainFrame();
        f.setVisible(true);
    }

    public static void main(String args[]) {
        loadFrame();
    }
}
