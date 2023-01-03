package com.gumse;

import static org.lwjgl.opengl.GL11.*;

import com.gumse.basics.Camera;
import com.gumse.basics.Globals;
import com.gumse.gui.GUI;
import com.gumse.maths.*;
import com.gumse.model.Model3D;
import com.gumse.system.Display;
import com.gumse.system.Window;
import com.gumse.system.Window.*;
import com.gumse.tools.Debug;
import com.gumse.tools.FPS;


public class Example3D
{
    public static void main(String[] args) 
    {
        Globals.DEBUG_BUILD = true;
        System.setProperty("java.awt.headless", "true"); //for iCrap support

        Debug.init();
        Display.init();
        
        //Window Options
        Window pMainWindow = new Window("Example App", new ivec2(500, 500), Window.GUM_WINDOW_RESIZABLE, null);
        pMainWindow.setClearColor(new vec4(0.09f, 0.1f, 0.11f, 1.0f)); // Set the clear color);

        Model3D pCube = new Model3D(null);
        pCube.load("models/teapot.obj", Example3D.class);
        //pCube.load("models/card.obj");
        pCube.setPosition(new vec3(0,0,-70));
        pCube.setScale(new vec3(20));

        Camera camera = new Camera(90.0f);
        camera.setPosition(new vec3(0, 0, 100.0f));
        
        GUI testGUI = new GUI(pMainWindow);
		pMainWindow.onResized(new WindowResizePosCallback() {
            @Override public void run(ivec2 val) {
                testGUI.setSize(val);
                camera.updateProjection();
            }
        });
        
        //float aspect = (float)Framebuffer.CurrentlyBoundFramebuffer.getSize().y / (float)Framebuffer.CurrentlyBoundFramebuffer.getSize().x;
        //float distance = 10.0f;
        //camera.setProjectionMatrix(mat4.ortho(aspect * distance / 2, distance * 0.5f, -aspect * distance / 2, -distance * 0.5f, 0.1f, 1000.0f));
        //camera.setProjectionMatrix(Framebuffer.CurrentlyBoundFramebuffer.getScreenMatrix());

        while(pMainWindow.isOpen())
        {
            Display.pollEvents();
            pMainWindow.clear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            testGUI.render();
            testGUI.update();

            //System.out.println("Mouse is: " + (Mouse.isBusy() ? "Busy" : "Available"));

            Model3D.getDefaultShader().use();
            Model3D.getDefaultShader().loadUniform("projectionMatrix", camera.getProjectionMatrix());
            Model3D.getDefaultShader().loadUniform("viewMatrix", camera.getViewMatrix());
            pCube.increaseRotation(new vec3(FPS.getFrametime() * 100.0f));
            pCube.render();
            Model3D.getDefaultShader().unuse();

            pMainWindow.finishRender();
            pMainWindow.getMouse().reset();
            pMainWindow.getKeyboard().reset();

            //pMainWindow.update();
            FPS.update();
		}

        pMainWindow.cleanup();
    }
}