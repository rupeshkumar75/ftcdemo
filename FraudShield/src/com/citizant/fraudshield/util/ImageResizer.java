package com.citizant.fraudshield.util;

import static org.imgscalr.Scalr.OP_ANTIALIAS;
import static org.imgscalr.Scalr.OP_BRIGHTER;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.imageio.ImageIO;

import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Method;



public class ImageResizer {
	
	public static byte[] resizeImage(byte[] original, int width){
        try{
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(original));
            
            //Calculate image height
            BufferedImage resized2 = createThumbnail(image);
           
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write(resized2, "jpg", out);
            out.flush();
            out.close();
            byte[] bout = out.toByteArray();
            
         //   FileOutputStream fout = new FileOutputStream("c:/temp/thumb0000.jpg");
         //   fout.write(bout);
         //   fout.flush();
         //   fout.close();
            
            return bout;
        }catch(Exception e){
           e.printStackTrace();
        }
        return null;
      }
	
	  public static BufferedImage createThumbnail(BufferedImage img) {
    	  // Create quickly, then smooth and brighten it.
    	  img = Scalr.resize(img, Method.QUALITY, 150, OP_ANTIALIAS, OP_BRIGHTER);
    	 
    	  // Let's add a little border before we return result.
    	  return img;
    }
}
