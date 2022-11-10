package com.gumse.basics;

import com.gumse.maths.mat4;
import com.gumse.maths.tools;
import com.gumse.maths.vec3;

public class Transformable {
    protected vec3 vPosition;
    protected vec3 vRotation;
    protected vec3 vScale;
    protected mat4 mTransformationMatrix;

    public float dist = 0;
    public Transformable()
    {
        this.vPosition = new vec3(0, 0, 0);
        this.vRotation = new vec3(0);
        this.vScale = new vec3(Globals.TILE_SIZE, Globals.TILE_SIZE, 1);
        this.mTransformationMatrix = new mat4();
    }

    protected void updateTransformationMat()
    {
        //mTransformationMatrix = mat4.perspective(90.0f, Window.getAspectRatio(), 0.1f, 1000.0f);
        //mTransformationMatrix = mat4.ortho(0, 1080, 1920, 0, -100.0f, 1000.0f);
        //mTransformationMatrix.mul(mat4.view(new vec3(0, 0, -dist), new vec3(0.01f), new vec3(0,1,0)));
        //mTransformationMatrix.mul(Camera.FPSViewRH(new vec3(0, 0, 40), 0.0f, dist));
        mTransformationMatrix = tools.createTransformationMatrix(this.vPosition, vRotation, vScale);
        //mTransformationMatrix = tools.createTransformationMatrix(this.vPosition, vRotation, vScale);
    }

    public void setPosition(vec3 pos)      { this.vPosition.set(pos);    updateTransformationMat(); }
    public void setRotation(vec3 rot)      { this.vRotation.set(rot);    updateTransformationMat(); }
    public void setScale(vec3 scale)       { this.vScale.set(scale);     updateTransformationMat(); }

    public void increasePosition(vec3 pos) { this.vPosition.add(pos); updateTransformationMat(); }
    public void increaseRotation(vec3 rot) { this.vRotation.add(rot); updateTransformationMat(); }
    public void increaseScale(vec3 scale)  { this.vScale.add(scale);  updateTransformationMat(); }

    public vec3 getPosition()              { return this.vPosition; }
    public vec3 getRotation()              { return this.vRotation; }
    public vec3 getScale()                 { return this.vScale; }
}
