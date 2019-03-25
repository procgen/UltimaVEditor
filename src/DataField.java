import javax.swing.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class DataField extends JTextField implements FocusListener {

    private static byte[] saveData;

    static {
        try{
            saveData = Files.readAllBytes(Paths.get("G:/Documents/CECS 378/Ultima_5/SAVED.GAM"));
        }
        catch(IOException e)
        {
            System.exit(404);
        }
    }

    private int length;
    private int offset;
    private byte[] data;

    private int ByteToInt(byte[] bytes){
        int newInt = 0;
        for(int b = 0; b < length; b++){
            newInt = newInt | (bytes[b] << (b * 8));
        }
        return newInt;
    }

    public DataField(int offset, int length) {
        this.offset = offset;
        this.length = length;
        data = new byte[length];
        data = Arrays.copyOfRange(saveData, offset, offset + length);
        setText(Integer.toString(ByteToInt(data)));
        setColumns(length * 2);
    }

    private void setData(String newData)
    {
        String dataHex = Integer.toHexString(Integer.parseInt(newData));

    }

    @Override
    public void focusGained(FocusEvent fe){
        selectAll();
    }

    @Override
    public void focusLost(FocusEvent fe){
        setData(getText());
    }

}
