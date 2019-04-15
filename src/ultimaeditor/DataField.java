package ultimaeditor;

import javax.swing.*;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.IOException;
import java.nio.file.*;
import java.util.Arrays;


/**
 * Class that represents a single value in a set of bytes that we want to modify using a gui
 * saveData is a static byte array that stores the entire SAVED.GAM file to be read from and written to
 * Each individual DataField can has an offset that tells its location from the beginning of the bytearray and
 * has a length value that tells how many bytes are represented by the DataField. It also stores the value within the
 * object itself for displaying in the GUI
 * The class extends JTextField to facilitate its use in the GUI as an editable textfield.
 *
 */
public class DataField extends JTextField {

//    private static Path filePath = Paths.get("G:/Documents/CECS 378/Ultima_5/SAVED.GAM");
//    private static Path filePath = Paths.get("/home/josh/Documents/Projects/UltimaV/Ultima_5/Ultima_5/SAVED.GAM");
    private static Path filePath = Paths.get("./SAVED.GAM");
    private static byte[] saveData;
    public static String[] charNames = new String[16];

    // Static block - When this class is loaded we call 'reload' to read the SAVED.GAM file and store it in saveData
    static {
        reload();
    }

    private int length;
    private int offset;
    private byte[] data;

    // Static method that takes the temporary data from saveData and writes it to disk
    public static void persist(){
        try{
            Files.write(filePath, saveData, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e){
            System.err.println("Failed to write to save game.");
            System.exit(-1);
        }
    }

    // Reads the SAVED.GAM file and stores it into saveData.
    // It also converts the bytes of each character name into a string for use in the GUI.
    public static void reload(){
        try{
            saveData = Files.readAllBytes(filePath); // Read the SAVED.GAM file and store
        }
        catch(IOException e)
        {
            System.err.println("Failed to read save game.");
            System.exit(404);
        }

        for(int i = 0; i < 16; i++)
        {
            String name = ""; //Convert each of the 16 character's names into a string
            for(int j = 0x2 + (i * 0x20); j <= 0x9 + (i * 0x20); j++)
            {
                name += (char)saveData[j];
            }
            charNames[i] = name;
        }
    }


    public DataField(int offset, int length) {
        this.offset = offset;
        this.length = length;
        data = new byte[length];
        data = Arrays.copyOfRange(saveData, offset, offset + length);
        setText(Integer.toString(ByteToInt(data)));
        setColumns(length * 2);

        // Commits changes in this textfield to the saveData when you press enter
        this.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(getText());
                setData(Integer.parseInt(getText()));
            }
        });

        this.addFocusListener(new FocusListener() {
            // Automatically select everything in the textField when you click on it
            @Override
            public void focusGained(FocusEvent e) {
                selectAll();
            }

            // When you click away, commit all changes in the textfield to saveData
            @Override
            public void focusLost(FocusEvent e) {
                setData(Integer.parseInt(getText()));
            }
        });

    }

    //Converts a byte array to an integer little endian
    private int ByteToInt(byte[] bytes){
        int newInt = 0;
        for(int b = 0; b < length; b++){
            newInt = newInt | ((bytes[b] & 0xFF) << (b * 8));
        }
        return newInt;
    }

    //Converts an integer into bytes and stores them in saveData
    private void setData(int newData)
    {
        for(int b = 0; b < length; b++){
            data[b] = (byte)((newData >>> (b * 8)) & 0xFF);
            //shift int by 8 bits for each iteration to get each byte and mask it to 8 bits to prevent bugs
            //store the result in the data attribute of DataField
        }
        for(int b = offset; b < offset + length; b++){
            saveData[b] = data[b - offset];
            // use stored data attribute and offset to modify the saveData we have locally
        }
    }


}
