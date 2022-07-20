import javax.swing.*;

public class Main
{
    public static void main(String args[])
    {
        try
        {
            GUI();
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void GUI() throws Exception
    {
        GUI_ASCII_art.load_preferences();
        UIManager.setLookAndFeel(GUI_ASCII_art.get_look_and_feel_location(GUI_ASCII_art.get_look_and_feel()));
        GUI_ASCII_art r = new GUI_ASCII_art();
    }
}