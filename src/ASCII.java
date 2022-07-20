import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class ASCII
{
    private volatile int pixel_count;
    private volatile int completed_pixels;
    private volatile boolean is_active;
    private volatile boolean is_complete;
    private BufferedImage img;
    private Path destination;
    private String ascii;
    private int compression_height;
    private int compression_width;
    private int height;
    private int width;
    public ASCII(BufferedImage img, Path destination, String ascii, int compression_height, int compression_width)
    {
        pixel_count = 0;
        completed_pixels = 0;
        is_active = true;
        is_complete = false;
        this.destination=destination;
        this.compression_height=compression_height;
        this.compression_width=compression_width;
        this.ascii=ascii;
        this.img = img;
        height = img.getHeight();
        width = img.getWidth();
        pixel_count = height*width;
    }
    public boolean is_complete()
    {
        return is_complete;
    }
    public void deactivate()
    {
        is_active = false;
    }
    public void activate()
    {
        is_active = true;
    }
    public int get_pixel_count()
    {
        return pixel_count;
    }
    public int get_completed_pixels()
    {
        return completed_pixels;
    }
    public static int get_density(int rgb)
    {
        int r = (rgb>>16)&0xFF;
        int g = (rgb>>8)&0xFF;
        int b = (rgb>>0)&0xFF;
        return (30*r+59*g+11*b)/100;
    }
    public void generate() throws Exception
    {
        int tile_size = compression_height*compression_width;
        for(int i=0;i+compression_height-1<height;i+=compression_height)
        {
            StringBuilder output = new StringBuilder("");
            for(int j=0;j+compression_width-1<width;j+=compression_width)
            {
                completed_pixels+=tile_size;
                if(!is_active){System.out.println("process aborted");return;}
                int index = 0;
                for(int I=0;I<compression_height;I++)
                    for(int J=0;J<compression_width;J++)
                        index+=get_density(img.getRGB(J+j,I+i));
                index*=ascii.length();
                index/=(0xFF*compression_width*compression_height);
                if(index>=ascii.length())index=ascii.length()-1;
                output.append(ascii.charAt(index));
            }
            Files.writeString(destination,output.toString()+"\n", StandardOpenOption.APPEND);
            try {Thread.sleep(100);} catch(Exception e){}
            //text written line-by-line to lower RAM use
        }
        is_complete = true;
    }
}
