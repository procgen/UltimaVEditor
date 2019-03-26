import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame {

    MainFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Ultima V Save Editor");
        setPreferredSize(new Dimension(500, 350));

        this.setLayout(new BorderLayout());

        JPanel topP = new JPanel(new GridLayout(0, 1));
        JPanel row1 = new JPanel(new FlowLayout());
        JPanel row2 = new JPanel(new FlowLayout());


        JPanel cardPanel = new JPanel(new CardLayout());
        for(int i = 0; i < 16; i++)
        {
            cardPanel.add(makeCharacterField(0x20 * i), DataField.charNames[i]);
        }

        JComboBox chars = new JComboBox(DataField.charNames);
        chars.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                CardLayout cl = (CardLayout)(cardPanel.getLayout());
                cl.show(cardPanel, DataField.charNames[chars.getSelectedIndex()]);
            }
        });


        row1.add(chars);
        row1.add(makeDataField("Gold", 0x204, 2));
        row1.add(makeDataField("Keys", 0x206, 1));
        row1.add(makeDataField("Gems", 0x207, 1));
        row2.add(makeDataField("Black Badge", 0x218, 1));
        row2.add(makeDataField("Magic Carpet", 0x20A, 1));
        row2.add(makeDataField("Skull Keys", 0x20B, 1));
        row2.add(makeDataField("Magic Axe", 0x240, 1));
        topP.add(row1);
        topP.add(row2);
        this.add(topP, BorderLayout.NORTH);

        this.add(cardPanel, BorderLayout.CENTER);


        this.add(makeButtonPanel(), BorderLayout.SOUTH);

        pack();
    }

    private JPanel makeCharacterField(int offset){
        JPanel centerP = new JPanel(new GridLayout(0, 1));
        JPanel row1 = new JPanel(new FlowLayout());
        JPanel row2 = new JPanel(new FlowLayout());

        row1.add(makeDataField("STR", 0x0E + offset, 1));
        row1.add(makeDataField("DEXT", 0x0F + offset, 1));
        row1.add(makeDataField("INT", 0x10 + offset, 1));

        row2.add(makeDataField("HP", 0x12 + offset, 2));
        row2.add(makeDataField("HM", 0x14 + offset, 2));
        row2.add(makeDataField("EXP", 0x16 + offset, 2));

        centerP.add(row1);
        centerP.add(row2);
        return centerP;
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
