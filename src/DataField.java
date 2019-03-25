

import javax.swing.*;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.IOException;
import java.nio.file.*;
import java.util.Arrays;

public class DataField extends JTextField {

    private static byte[] saveData;
    private static Path filePath = Paths.get("G:/Documents/CECS 378/Ultima_5/SAVED.GAM");

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
            System.exit(404);
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
            newInt = newInt | (bytes[b] << (b * 8));
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
