package com.gumse.gui.Font;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import org.lwjgl.opengl.GL11;
import com.gumse.textures.Texture;

import com.gumse.maths.ivec2;

import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.RenderingHints;


public class FontLoader 
{
    private static int fontsize = 64;

    public static Character getFontChar(java.awt.Font font, String ch) 
	{
		//font = new Font("sanserif", Font.PLAIN, 24);
		Font tempfont = font.deriveFont((float)fontsize);
		//Create a temporary image to extract font size
		BufferedImage tempfontImage = new BufferedImage(1,1, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D)tempfontImage.getGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setFont(tempfont);
		FontMetrics fm = g.getFontMetrics();
		int charwidth = fm.stringWidth(ch);
		int charheight = fm.getHeight();

		if (charwidth <= 0)  charwidth = 1;
		if (charheight <= 0) charheight = fontsize;

		//Create another image for texture creation
		BufferedImage fontImage;
		fontImage = new BufferedImage(charwidth,charheight, BufferedImage.TYPE_INT_ARGB);
		Graphics2D gt = (Graphics2D)fontImage.getGraphics();
        gt.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		gt.setFont(tempfont);
		gt.setColor(new java.awt.Color(1.0f,1.0f,1.0f,1.0f));
		gt.drawString(ch, 0, 0 + fm.getAscent());

		ivec2 size = new ivec2(charwidth, charheight);
		int advance = 0; // metrics.getMaxAdvance();
		ivec2 bearing = new ivec2(0, 0);
		ByteBuffer byteBuffer = ByteBuffer.allocateDirect(charwidth * charheight);//.order(ByteOrder.LITTLE_ENDIAN);
		//ByteBuffer byteBuffer = MemoryUtil.memAlloc(width * height);
		for(int i = 0; i < charheight; i++)
		{
			for(int j = 0; j < charwidth; j++)
			{
				int color = fontImage.getRGB(j, i);
				// Components will be in the range of 0..255:
				/*int blue    = (color & 0x000000ff);
				int green   = (color & 0x0000ff00) >> 8;
				int red     = (color & 0x00ff0000) >> 16;*/
				int alpha   = (color & 0xff000000) >>> 24;
				//byteBuffer.put((byte)alpha);
				//byteBuffer.put((byte)green);
				//byteBuffer.put((byte)blue);
				byteBuffer.put((byte)alpha);
				//System.out.print(alpha > 0 ? "O" : " ");

			}
			//System.out.println("");
		}
		byteBuffer.flip();

		Character character = new Character();
		Texture tex = new Texture("FontChar_" + ch, 1, size);

		//tex.loadDirectly(fontChar.image, 1, fontChar.resolution, GL11.GL_UNSIGNED_BYTE);

		tex.bind(0);
		GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RED, size.x, size.y, 0, GL11.GL_RED, GL11.GL_UNSIGNED_BYTE, byteBuffer);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		Texture.unbind();

		//character.resolution = size;
		character.texture = tex;
		character.Advance = advance;
		character.Bearing = bearing;
        

		return character;
	}

    public static void init()
    {
    }
}
