

import javax.swing.*;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.IOException;
import java.nio.file.*;
import java.util.Arrays;

public class
DataField extends JTextField {

    private static Path filePath = Paths.get("G:/Documents/CECS 378/Ultima_5/SAVED.GAM");
    private static byte[] saveData;
    public static String[] charNames = new String[16];

    static {
        reload();
    }

    private int length;
    private int offset;
    private byte[] data;

    public static void persist(){
        try{
            Files.write(filePath, saveData, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e){
            System.err.println("Failed to write to save game.");
            System.exit(-1);
        }
    }

    public static void reload(){
        try{
            saveData = Files.readAllBytes(filePath);
        }
        catch(IOException e)
        {
            System.err.println("Failed to read save game.");
            System.exit(404);
        }

        for(int i = 0; i < 16; i++)
        {
            String name = "";
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

        this.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(getText());
                setData(Integer.parseInt(getText()));
            }
        });

        this.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                selectAll();
            }

            @Override
            public void focusLost(FocusEvent e) {
                setData(Integer.parseInt(getText()));
            }
        });

    }

    private int ByteToInt(byte[] bytes){
        int newInt = 0;
        for(int b = 0; b < length; b++){
            newInt = newInt | ((bytes[b] & 0xFF) << (b * 8));
        }
        return newInt;
    }

    private void setData(int newData)
    {
        for(int b = 0; b < length; b++){
            data[b] = (byte)((newData >>> (b * 8)) & 0xFF);
        }
        for(int b = offset; b < offset + length; b++){
            saveData[b] = data[b - offset];
        }
    }


}
