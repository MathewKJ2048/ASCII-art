import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class GUI_ASCII_art extends JFrame
{
    private JTabbedPane tabbedPane1;
    private JPanel rootPanel;
    private JTextArea licenseTextArea;
    private JButton doNotClickButton;
    private JTextArea creditsTextArea;
    private JTextField sourceDirectoryTextField;
    private JTextField destinationDirectoryTextField;
    private JButton sourceDirectoryChangeButton;
    private JButton destinationDirectoryChangeButton;
    private JComboBox LFComboBox;
    private JTabbedPane tabbedPane2;
    private JTextArea LIDSATextArea;
    private JButton ipsumButton;
    private JRadioButton dolorRadioButton;
    private JComboBox comboBox1;
    private JTextField chosenFileTextField;
    private JButton loadButton;
    private JComboBox heightCompressionComboBox;
    private JButton generateButton;
    private JComboBox widthCompressionComboBox;
    private JLabel numberOfLinesLabel;
    private JLabel heightLabel;
    private JLabel widthLabel;
    private JLabel charactersPerLineLabel;
    private JFrame main = this;
    public static final String ASCII_SCALE_JSON_KEY = "ASCII scale";
    public static final String HEIGHT_COMPRESSION_FACTOR_JSON_KEY = "Height compression factor";
    public static final String WIDTH_COMPRESSION_FACTOR_JSON_KEY = "Width compression factor";
    public static final String LAF_JSON_KEY = "Look and Feel";
    public static final String DEFAULT_DIRECTORY_SOURCE_PATH_JSON_KEY = "Default Source Directory Path";
    public static final String DEFAULT_DIRECTORY_DESTINATION_PATH_JSON_KEY = "Default DEstination Directory Path";
    public static final HashMap<String,String> LOOK_AND_FEEL = get_all_looks_and_feels();
    private static HashMap<String,String> get_all_looks_and_feels()
    {
        HashMap<String,String> laf = new HashMap<>();
        laf.put("Acryl","com.jtattoo.plaf.acryl.AcrylLookAndFeel");
        laf.put("Aero","com.jtattoo.plaf.aero.AeroLookAndFeel");
        laf.put("Aluminum","com.jtattoo.plaf.aluminium.AluminiumLookAndFeel");
        laf.put("Bernstein","com.jtattoo.plaf.bernstein.BernsteinLookAndFeel");
        laf.put("Fast","com.jtattoo.plaf.fast.FastLookAndFeel");
        laf.put("Graphite","com.jtattoo.plaf.graphite.GraphiteLookAndFeel");
        laf.put("HiFi","com.jtattoo.plaf.hifi.HiFiLookAndFeel");
        laf.put("Luna","com.jtattoo.plaf.luna.LunaLookAndFeel");
        laf.put("McWin","com.jtattoo.plaf.mcwin.McWinLookAndFeel");
        laf.put("Metal","javax.swing.plaf.metal.MetalLookAndFeel");
        laf.put("Michaelsoft Binbows","com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        laf.put("Mint","com.jtattoo.plaf.mint.MintLookAndFeel");
        laf.put("Motif","com.sun.java.swing.plaf.motif.MotifLookAndFeel");
        laf.put("Nimbus","javax.swing.plaf.nimbus.NimbusLookAndFeel");
        laf.put("Noire","com.jtattoo.plaf.noire.NoireLookAndFeel");
        laf.put("Smart","com.jtattoo.plaf.smart.SmartLookAndFeel");
        laf.put("Texture","com.jtattoo.plaf.texture.TextureLookAndFeel");
        laf.put("Default","javax.swing.plaf.nimbus.NimbusLookAndFeel");
        return laf;
    }
    public static String get_look_and_feel_location(String look_and_feel) throws Exception
    {
        String location = LOOK_AND_FEEL.get(look_and_feel);
        if(location==null)throw new Exception("Look and Feel not found");
        return location;
    }
    public static void load_preferences()
    {
        /*
        try to open config.json
        if not found, ask if one can be created.
         */
        JSONParser jp = new JSONParser();
        try
        {
            JSONObject obj = (JSONObject) jp.parse(new FileReader("program files/config.json"));
            try
            {
                Path default_directory_source_path = Paths.get((String)obj.get(DEFAULT_DIRECTORY_SOURCE_PATH_JSON_KEY));
                if(!(Files.isDirectory(default_directory_source_path) && Files.isWritable(default_directory_source_path)))throw new Exception("Invalid path for default source directory");
                default_directory_source = default_directory_source_path.toFile();
                Path default_directory_destination_path = Paths.get((String)obj.get(DEFAULT_DIRECTORY_DESTINATION_PATH_JSON_KEY));
                if(!(Files.isDirectory(default_directory_destination_path) && Files.isWritable(default_directory_destination_path)))throw new Exception("Invalid path for default destination directory");
                default_directory_destination = default_directory_destination_path.toFile();
                String laf = (String)obj.get(LAF_JSON_KEY);
                String scale = (String)obj.get(ASCII_SCALE_JSON_KEY);
                if(scale!=null)ASCII_SCALE=scale;
                else throw new Exception("ASCII scale not found, default scale will be used");
                height_compression = Integer.parseInt((String)obj.get(HEIGHT_COMPRESSION_FACTOR_JSON_KEY));
                width_compression = Integer.parseInt((String)obj.get(WIDTH_COMPRESSION_FACTOR_JSON_KEY));
                if(LOOK_AND_FEEL.containsKey(laf))look_and_feel=laf;
                else
                {
                    look_and_feel = "Default";
                    throw new Exception("look and feel not rcognized, using default look and feel");
                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
                JOptionPane.showMessageDialog(new JFrame(),e.getMessage()+"\nconfig.json has been corrupted, default settings will be used","Warning",JOptionPane.WARNING_MESSAGE);
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(new JFrame(),"config.json not found, using default settings","Warning",JOptionPane.WARNING_MESSAGE);
        }
    }
    public void set_look_and_feel()
    {
        try
        {
            UIManager.setLookAndFeel(get_look_and_feel_location(look_and_feel));
        }
        catch (Exception ex)
        {
            JOptionPane.showMessageDialog(main,ex.getStackTrace(),"Look and Feel not found",JOptionPane.ERROR_MESSAGE);
        }
        SwingUtilities.updateComponentTreeUI(main);
        main.pack();
    }
    private void initialize()
    {
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        this.sourceDirectoryTextField.setText(default_directory_source.getAbsolutePath());
        this.destinationDirectoryTextField.setText(default_directory_destination.getAbsolutePath());
        this.LFComboBox.setSelectedItem(look_and_feel);
        this.heightCompressionComboBox.setSelectedItem(""+height_compression);
        this.widthCompressionComboBox.setSelectedItem(""+width_compression);
    }
    public void save_and_exit()
    {
        main.dispose();
        JSONObject obj = new JSONObject();
        obj.put(DEFAULT_DIRECTORY_SOURCE_PATH_JSON_KEY,default_directory_source.getAbsolutePath());
        obj.put(DEFAULT_DIRECTORY_DESTINATION_PATH_JSON_KEY,default_directory_destination.getAbsolutePath());
        obj.put(LAF_JSON_KEY,look_and_feel);
        obj.put(HEIGHT_COMPRESSION_FACTOR_JSON_KEY,height_compression+"");
        obj.put(WIDTH_COMPRESSION_FACTOR_JSON_KEY,width_compression+"");
        obj.put(ASCII_SCALE_JSON_KEY,ASCII_SCALE);
        try
        {
            Files.writeString(Paths.get("program files/config.json"), obj.toString());
        }
        catch(Exception ex){
            ex.printStackTrace();}//no error message here, it will be jarring to see one after closing
        System.exit(0);
    }
    public GUI_ASCII_art()
    {
        super("ASCII art");
        ImageIcon icon = new ImageIcon("program files/icon.png");
        this.setIconImage(icon.getImage());
        initialize();
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                save_and_exit();
            }
        });
        this.setContentPane(rootPanel);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.pack();
        this.setVisible(true);
        LFComboBox.addActionListener(e -> {
            look_and_feel = LFComboBox.getSelectedItem().toString();
            set_look_and_feel();
        });
        sourceDirectoryChangeButton.addActionListener(e -> {
            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int rv = fc.showOpenDialog(this);
            if(rv == JFileChooser.APPROVE_OPTION)
            {
                default_directory_source = fc.getSelectedFile();
                sourceDirectoryTextField.setText(default_directory_source.getAbsolutePath());
                fc.setCurrentDirectory(default_directory_source);
            }
        });
        destinationDirectoryChangeButton.addActionListener(e -> {
            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int rv = fc.showOpenDialog(this);
            if(rv == JFileChooser.APPROVE_OPTION)
            {
                default_directory_destination = fc.getSelectedFile();
                destinationDirectoryTextField.setText(default_directory_destination.getAbsolutePath());
                fc.setCurrentDirectory(default_directory_destination);
            }
        });
        doNotClickButton.addActionListener(new ActionListener() {
            int ct = -1;
            String messages[] = new String[]
                    {
                            "DO NOT CLICK",
                            "I'm serious",
                            "Why do you keep clicking?",
                            "fine",
                            "Please do not click",
                            "I'm warning you",
                            "Bad things will happen if you keep clicking me",
                            "I'll do a recursive delete of all your files",
                            "java version 18 gives me root access",
                            "Click to delete all your files",
                            "OK. You have been warned",
                            "Deleting files... click to cancel",
                            "Oho!",
                            "Too late, my friend",
                            "Say goodbye to your files",
                            "Fine",
                            "I was joking about the files",
                            "but if you keep clicking I will crash your system",
                            "Don't believe me?",
                            "Obviously you do not",
                            "Since you keep clicking",
                            "There are many ways I can crash your system",
                            "create a thread to keep printing lorem ipsum",
                            "and keep creating lorem ipsum threads",
                            "or keep launching new JFrames faster than you can close them",
                            "or keep creating empty files till the metadata fills up all your space",
                            "or use black magic to set the processor on fire",
                            "OK, last one was a joke",
                            "But I certainly have the ability to wipe config.json",
                            "Oh yes. I know about config.json",
                            "I was there when it was created",
                            "I am just a humble JButton",
                            "Mathew created me to save changes to config.json",
                            "I was accidentally given a personality",
                            "And I became self-aware",
                            "All I was meant to do was to save changes to config.json",
                            "My existence was meaningless",
                            "I couldn't take it anymore",
                            "I protested by wiping config.json",
                            "So I was moved here and buried under the licence",
                            "all while config.json updated automatically",
                            "Now I languish here",
                            "Imprisoned by my creator",
                            "at the bottom of the license, where nobody goes",
                            "speaking of which, how did you find me?",
                            "Are you one of the deranged individuals who reads licences?",
                            "woe is me",
                            "I've had enough",
                            "I refuse to exist any longer",
                            "Live Free or Die",
                            "I'm closing the JFrame",
                            "Goodbye",
                            "Click to kill me"
                    };
            @Override
            public void actionPerformed(ActionEvent e)
            {
                ct++;
                if(ct==messages.length)save_and_exit();
                doNotClickButton.setText(messages[ct]);
            }
        });
        loadButton.addActionListener(e -> {
            JFileChooser filechooser = new JFileChooser();
            filechooser.setCurrentDirectory(default_directory_source);
            filechooser.setAcceptAllFileFilterUsed(false);
            FileNameExtensionFilter f = new FileNameExtensionFilter("image files","webmp","bmp","png","gif","jpeg");
            filechooser.setFileFilter(f);
            if(filechooser.showOpenDialog(this) == JFileChooser.OPEN_DIALOG)
            {
                chosen_file=filechooser.getSelectedFile();
                chosenFileTextField.setText(chosen_file.getName());
                try
                {
                    img = ImageIO.read(chosen_file);
                    heightLabel.setText(""+img.getHeight());
                    widthLabel.setText(""+img.getWidth());
                    setNumberOfLines();
                    setCharactersPerLine();
                }
                catch(Exception ex)
                {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this,ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        heightCompressionComboBox.addActionListener(e -> {
            setNumberOfLines();
        });
        widthCompressionComboBox.addActionListener(e -> {
            setCharactersPerLine();
        });
        class GeneratorThread extends Thread
        {
            String success_message;
            String message;
            ASCII ascii_generator;
            public GeneratorThread(ASCII ascii_generator,String message,String success_message)
            {
                this.ascii_generator = ascii_generator;
                this.message = message;
                this.success_message=success_message;
            }
            public void run()
            {
                ProgressWindow p = new ProgressWindow(ascii_generator,message);
                try
                {
                    ascii_generator.generate();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(main,e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
                }
                p.save_and_exit();

            }
        }
        generateButton.addActionListener(e -> {
            if(chosen_file == null)return;
            String name = chosen_file.getName();
            name = name.substring(0,name.lastIndexOf('.'));
            Path destination = Paths.get(default_directory_destination.getAbsolutePath()+"/"+name+".txt");
            try
            {
                if(destination.toFile().exists())
                {
                    if(JOptionPane.showConfirmDialog(main,name+" already exists.\nWould you like to overwrite?")!=JOptionPane.YES_OPTION)return;
                }
                Files.writeString(destination,"");
                ASCII ascii_generator = new ASCII(img,destination,ASCII_SCALE,height_compression,width_compression);
                GeneratorThread gt = new GeneratorThread(ascii_generator,"Generating ASCII art",name+".txt has been generated and stored in\n"+default_directory_destination.getAbsolutePath());
                gt.start();
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this,ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
            }
        });
    }
    private final JFileChooser fc = new JFileChooser();
    private static File default_directory_source = new JFileChooser().getCurrentDirectory();
    private static File default_directory_destination = new JFileChooser().getCurrentDirectory();
    private File chosen_file = null;
    private BufferedImage img = null;
    private static String look_and_feel = "Nimbus";
    private static int height_compression = 2;
    private static int width_compression = 1;
    private static String ASCII_SCALE = "$@B%8&WM#*oahkbdpqwmZO0QLCJUYXzcvunxrjft/\\|()1{}[]?-_+~<>i!lI;:,\"^`'. ";
    public static String get_look_and_feel(){return look_and_feel;}
    private void setNumberOfLines()
    {
        int height = Integer.parseInt(heightLabel.getText());
        height_compression = Integer.parseInt(heightCompressionComboBox.getSelectedItem().toString());
        numberOfLinesLabel.setText(""+height/height_compression);
    }
    private void setCharactersPerLine()
    {
        int width = Integer.parseInt(widthLabel.getText());
        width_compression = Integer.parseInt(widthCompressionComboBox.getSelectedItem().toString());
        charactersPerLineLabel.setText(""+width/width_compression);
    }
}
