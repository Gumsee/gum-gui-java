package com.gumse.gui.Font;

import java.awt.color.ColorSpace;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.WritableRaster;
import java.awt.image.Raster;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferInt;
import java.awt.image.DataBufferShort;
import java.awt.image.DataBufferUShort;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.Hashtable;

import javax.swing.JLabel;

import org.lwjgl.BufferUtils;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import com.gumse.textures.Texture;
import com.gumse.maths.ivec2;

import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.RenderingHints;


public class FontLoader 
{
    private static ColorModel glAlphaColorModel;
	private static ColorModel glColorModel;
	private static int fontsize = 32;

    public static Character getFontChar(java.awt.Font font, String ch) 
	{
		/*java.awt.Font tempfont;
		tempfont = font.deriveFont((float)fontsize);
		//Create a temporary image to extract font size
		BufferedImage tempfontImage = new BufferedImage(1,1, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D)tempfontImage.getGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setFont(tempfont);
		FontMetrics fm = g.getFontMetrics();
		int charwidth = fm.charWidth(ch.charAt(0));
		int charheight = fm.getHeight();

		if(charwidth <= 0)  charwidth = 1;
		if(charheight <= 0) charheight = fontsize;

		//Create another image for texture creation
		BufferedImage fontImage;
		fontImage = new BufferedImage(charwidth, charheight, BufferedImage.TYPE_INT_ARGB);
		Graphics2D gt = (Graphics2D)fontImage.getGraphics();
        gt.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		gt.setFont(tempfont);

		//// Uncomment these to fill in the texture with a background color
		//// (used for debugging)
		//gt.setColor(Color.RED);
		//gt.fillRect(0, 0, charwidth, fontsize);
		
		gt.setColor(new java.awt.Color(0, 0, 0, 1));
		int charx = 0;
		int chary = 0;
		gt.drawString(ch, (charx), (chary) + fm.getAscent());



		ByteBuffer imageByteBuffer = null; 
		WritableRaster raster;
		BufferedImage texImage;
		
		int texWidth = 2;
		int texHeight = 2;
		
		// find the closest power of 2 for the width and height
		// of the produced texture
		while (texWidth < fontImage.getWidth()) {
			texWidth *= 2;
		}
		while (texHeight < fontImage.getHeight()) {
			texHeight *= 2;
		}
		
		//texture.setTextureHeight(texHeight);
		//texture.setTextureWidth(texWidth);
		
		// create a raster that can be used by OpenGL as a source
		// for a texture
		if (fontImage.getColorModel().hasAlpha()) {
			raster = Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE,texWidth,texHeight,4,null);
			texImage = new BufferedImage(glAlphaColorModel,raster, false, new Hashtable());
		} else {
			raster = Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE,texWidth,texHeight,3,null);
			texImage = new BufferedImage(glColorModel,raster, false, new Hashtable());
		}
		
		// copy the source image into the produced image
		Graphics gn = texImage.getGraphics();
		gn.setColor(new Color(0f,0f,0f,0f));
		gn.fillRect(0,0,texWidth,texHeight);
		gn.drawImage(fontImage,0,0,null);
		
		// build a byte buffer from the temporary image 
		// that be used by OpenGL to produce a texture.
		byte[] data = ((DataBufferByte) texImage.getRaster().getDataBuffer()).getData(); 
		
		imageByteBuffer = ByteBuffer.allocateDirect(data.length); 
		imageByteBuffer.order(ByteOrder.nativeOrder()); 
		imageByteBuffer.put(data, 0, data.length); 
		imageByteBuffer.flip();

		ivec2 size = new ivec2(texWidth, texHeight);
		int advance = fm.getMaxAdvance();*/

		/*ivec2 size = new ivec2(32, 32);

        BufferedImage image = new BufferedImage(size.x, size.y, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        g.setColor(new Color(255, 255, 255, 0));
        g.drawRect(0, 0, size.x, size.y);
        g.setColor(new Color(255, 255, 255));
        g.setFont(font);
        g.drawString(ch, 0, 0);

        ByteBuffer buf = ByteBuffer.allocateDirect(4 * 4 * size.x * size.y);
        for (int x = 1; x <= size.x; x++)
        {
            for (int y = 1; y <= size.y; y++)
            {
                Color color = new Color(image.getRGB(x - 1, y - 1));
                buf.putFloat((float)(1 / 255 * color.getRed()));
                buf.putFloat((float)(1 / 255 * color.getGreen()));
                buf.putFloat((float)(1 / 255 * color.getBlue()));
                buf.putFloat((float)(1 / 255 * color.getAlpha()));
            }
        }
        buf.flip();*/

		font = new Font("sanserif", Font.PLAIN, 24);
		//new Font()
        // use a JLabel to get a FontMetrics object
        FontMetrics metrics = new JLabel().getFontMetrics(font);
        int width = metrics.stringWidth(ch);
        int height = metrics.getMaxAscent();

		if(width <= 0)  width = 1;
		if(height <= 0) height = 1;
		ivec2 size = new ivec2(width, height);
		int advance = metrics.getLeading();

		//width *= 20;
		//height *= 20;

        // use ARGB or the background will be black as well
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        // create a Graphics2D object from the BufferedImage
        Graphics2D g2d = bi.createGraphics();
        g2d.setFont(font);
        g2d.setColor(Color.black);
        g2d.drawString(ch, 0, height);
        g2d.dispose();

		ByteBuffer byteBuffer = ByteBuffer.allocateDirect(width * height * 4);//.order(ByteOrder.LITTLE_ENDIAN); 
		//ByteBuffer byteBuffer = MemoryUtil.memAlloc(width * height);
		for(int i = 0; i < height; i++)
		{
			for(int j = 0; j < width; j++)
			{
				int color = bi.getRGB(j, i);
				// Components will be in the range of 0..255:
				int blue    = (color & 0x000000ff);
				int green   = (color & 0x0000ff00) >> 8;
				int red     = (color & 0x00ff0000) >> 16;
				int alpha   = (color & 0xff000000) >>> 24;
				byteBuffer.put((byte)alpha);
				byteBuffer.put((byte)green);
				byteBuffer.put((byte)blue);
				byteBuffer.put((byte)alpha);
				System.out.print(alpha > 0 ? "O" : " ");

			}
			System.out.println("");
		}
		System.out.println("");
		byteBuffer.flip();

		Character character = new Character();
		Texture tex = new Texture("FontChar_" + ch, 4, size);

		//tex.loadDirectly(fontChar.image, 1, fontChar.resolution, GL11.GL_UNSIGNED_BYTE);

		tex.bind(0);
		GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, size.x, size.y, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, byteBuffer);
		tex.unbind();

		//character.resolution = size;
		character.texture = tex;
		character.Advance = advance;
        

		return character;
	}

    public static void init()
    {
        glAlphaColorModel = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB),
				new int[] {8,8,8,8},
				true,
				false,
				ComponentColorModel.TRANSLUCENT,
				DataBuffer.TYPE_BYTE);
		
		glColorModel = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB),
				new int[] {8,8,8,0},
				false,
				false,
				ComponentColorModel.OPAQUE,
				DataBuffer.TYPE_BYTE);
    }
}
