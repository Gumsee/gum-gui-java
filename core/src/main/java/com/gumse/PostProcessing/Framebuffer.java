package com.gumse.PostProcessing;

import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL32;

import com.gumse.maths.ivec2;
import com.gumse.system.Window;
import com.gumse.textures.Texture;
import com.gumse.tools.Debug;

import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;

public class Framebuffer 
{
    private final int iFBO;
    private final ivec2 vPosition;
    private final ivec2 vSize;

    private Texture pTexture, pDepthTexture;

    public Framebuffer(ivec2 size)
    {
        vPosition = new ivec2(0);
        vSize = size;
        iFBO = GL30.glGenFramebuffers();
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, iFBO);
        GL30.glDrawBuffer(GL30.GL_COLOR_ATTACHMENT0);
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
    }

    public void bind() 
    {
        GL30.glBindTexture(GL30.GL_TEXTURE_2D, 0);
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, iFBO);
        GL30.glViewport(vPosition.x, vPosition.y, vSize.x, vSize.y);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }  

    public void unbind() 
    {
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
        Window.CurrentlyBoundWindow.resetViewport();
    }

    public void addTextureAttachment()
    {  
        pTexture = new Texture();
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, iFBO);
        pTexture.bind(0);
        GL30.glTexImage2D(GL30.GL_TEXTURE_2D, 0, GL30.GL_RGBA, vSize.x, vSize.y, 0, GL30.GL_RGBA, GL30.GL_UNSIGNED_BYTE, 0);
        GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_MAG_FILTER, GL30.GL_LINEAR);
        GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_MIN_FILTER, GL30.GL_LINEAR);
        GL32.glFramebufferTexture(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, pTexture.getID(), 0);
        Texture.unbind();
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
    }  

    public void addDepthTextureAttachment()
    {  
        pDepthTexture = new Texture();
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, iFBO);
        pDepthTexture.bind(0);
        GL30.glTexImage2D(GL30.GL_TEXTURE_2D, 0, GL30.GL_DEPTH_COMPONENT24, vSize.x, vSize.y, 0, GL30.GL_DEPTH_COMPONENT, GL30.GL_UNSIGNED_BYTE, 0);
        GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_MAG_FILTER, GL30.GL_LINEAR);
        GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_MIN_FILTER, GL30.GL_LINEAR);
        GL32.glFramebufferTexture(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, pDepthTexture.getID(), 0);
        Texture.unbind();
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
    }

    public void addDepthAttachment()
    {
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, iFBO);
        int depthBuffer = GL30.glGenRenderbuffers();
        GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, depthBuffer);
        GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, GL30.GL_DEPTH_COMPONENT24, vSize.x, vSize.y);
        GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL30.GL_RENDERBUFFER, depthBuffer);
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
    }


    public Texture getTexture()
    {
        if(this.pTexture == null) { Debug.error("Cannot get Texture from framebuffer, did you call addTextureAttachment?"); }
        return this.pTexture;
    }

    public Texture getDepthTexture()
    {
        if(this.pDepthTexture == null) { Debug.error("Cannot get Depth Texture from framebuffer, did you call addDepthTextureAttachment?"); }
        return this.pDepthTexture;
    }


    //
    // Getter
    //
    public ivec2 getSize()     { return this.vSize; }
    public ivec2 getPosition() { return this.vPosition; }
    public int getID()         { return this.iFBO; }
}
