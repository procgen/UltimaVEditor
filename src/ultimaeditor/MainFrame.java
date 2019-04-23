package ultimaeditor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/** Class entirely for GUI, extends JFrame to make it happen
 * The gui is made up of DataFields, each one represents an offset we would
 * like to modify in Ultima V.
 * For each one, we provide the offset and number of bytes we are modifying.
 */
public class MainFrame extends JFrame {

    /** Default constructor
     *
     */
    private MainFrame() {
        // Determine basic layout and settings of GUI
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Ultima V Save Editor");
        setPreferredSize(new Dimension(500, 250));

        this.setLayout(new BorderLayout());

        JPanel topP = new JPanel(new GridLayout(0, 1));
        JPanel row1 = new JPanel(new FlowLayout());
        JPanel row2 = new JPanel(new FlowLayout());

        //Create a panel for each character that will hold its own values and offsets.
        JPanel cardPanel = new JPanel(new CardLayout());
        for(int i = 0; i < 16; i++)
        {
            // Since each character is offset by 32 bits, we are able to reuse lots of the same code.
            // makeCharacterField returns the entire panel we want to display.
            cardPanel.add(makeCharacterField(0x20 * i), DataField.charNames[i]);
        }

        // Creates the dropdown menu using character names taken straight from the SAVED.GAM file
        // The panels for each character created in the previous loop are tied to this
        JComboBox chars = new JComboBox<>(DataField.charNames);
        chars.addActionListener(new ActionListener() {
            // When we change what is selected in the dropdown, the current character stats being displayed are changed
            public void actionPerformed(ActionEvent actionEvent) {
                CardLayout cl = (CardLayout)(cardPanel.getLayout());
                cl.show(cardPanel, DataField.charNames[chars.getSelectedIndex()]);
            }
        });


        row1.add(chars); // add the character stats panel (initializes with main character being displayed)

        // Each stat shared by the group is added here. We give a label to them, an offset, and the size of the data
        // that we are storing. Datafield objects themselves are based on textfields and can be added directly to the GUI
        row1.add(makeDataField("Gold", 0x204, 2));
        row1.add(makeDataField("Keys", 0x206, 1));
        row1.add(makeDataField("Gems", 0x207, 1));
        row2.add(makeDataField("B. Badge", 0x218, 1));
        row2.add(makeDataField("M. Carpet", 0x20A, 1));
        row2.add(makeDataField("Skull Keys", 0x20B, 1));
        row2.add(makeDataField("M. Axe", 0x240, 1));
        topP.add(row1);
        topP.add(row2);

        // Use the border layout to get things looking right
        this.add(topP, BorderLayout.NORTH);
        this.add(cardPanel, BorderLayout.CENTER);
        this.add(makeButtonPanel(), BorderLayout.SOUTH);

        pack(); // size window based on contents
    }

    /**
     * Using a given character offset (where the data begins describing a character) we are able to determine where all
     * necessary attributes are stored and create DataField objects for each.
     * @param offset - Offset of the character
     */
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

    /**
     * Method to return a JPanel that stores a label with given text and a datafield (modified textfield) next to it
     * @param label - The text displayed next to the textField
     * @param offset - How to offset where we are accessing the data
     * @param length - How many bytes this value takes up
     * @return - A JPanel for inserting directly into the GUI, containing a DataField and label combo
     */
    private JPanel makeDataField(String label, int offset, int length){
        JPanel p = new JPanel(new FlowLayout());
        JLabel nameLabel = new JLabel(label + ": ", JLabel.LEADING);
        DataField nameField = new DataField(offset, length);
        p.add(nameLabel);
        p.add(nameField);
        return p;
    }

    /**
     * Method that creates a panel containing two buttons:
     * One that reloads all datafields and the main window itself
     * Another that applies all changes that have been made so far
     * @return The JPanel containing both buttons
     */
    private JPanel makeButtonPanel(){
        JPanel p = new JPanel(new FlowLayout());

        JButton refreshButton = new JButton();
        refreshButton.setText("Reload");
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false); // hide the current window
                DataField.reload(); // reload save file
                loadFrame(); // create a new window (with new values in datafields)
                dispose(); // get rid of the current window (previously hidden)
            }
        });

        JButton applyButton = new JButton();
        applyButton.setText("Apply");
        applyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DataField.persist(); // call the static persist method that will save to disk
            }
        });

        p.add(refreshButton);
        p.add(applyButton);
        return p;
    }

    /**
     * Used to create a new main window
     */
    private static void loadFrame(){
        MainFrame f = new MainFrame();
        f.setVisible(true);
    }

    /**
     * Entry point, simply opens the window
    */
    public static void main(String args[]) {
        loadFrame();
    }
}
