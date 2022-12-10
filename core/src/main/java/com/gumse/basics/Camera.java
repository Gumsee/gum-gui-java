package com.gumse.basics;

import com.gumse.maths.mat4;
import com.gumse.maths.vec3;
import com.gumse.system.Window;

public class Camera {
    private vec3 vPosition;
    private mat4 mView;
    private mat4 mProjection;
    private float fRoll, fYaw, fPitch;
    private float fFOV;

    public Camera(float fov)
    {
        this.vPosition = new vec3(0,0,0);
        this.fRoll = 0;
        this.fPitch = 0;
        this.fYaw = 0;
        this.fFOV = fov;

        updateView();
        updateProjection();
    }


    private void updateView()
    {
        mView = null;
        mView = new mat4();
        mView.rotate(new vec3(fPitch, fYaw, fRoll));
        mView.translate(new vec3(-vPosition.x, -vPosition.y, -vPosition.z));
    }

    public void updateProjection()
    {
        mProjection = mat4.perspective(fFOV, Window.CurrentlyBoundWindow.getAspectRatioWidthToHeight(), 0.1f, 1000.0f);
    }

    public vec3 getPosition()                  { return this.vPosition; }
    public mat4 getViewMatrix()                { return this.mView; }
    public mat4 getProjectionMatrix()          { return this.mProjection; }
    public float getFOV()                      { return this.fFOV; }

    public void setPosition(vec3 pos)          { this.vPosition = pos; this.updateView(); }
    public void setPitch(float pitch)          { this.fPitch = pitch;  this.updateView(); }
    public void setYaw(float yaw)              { this.fYaw = yaw;      this.updateView(); }
    public void setRoll(float roll)            { this.fRoll = roll;    this.updateView(); }
    public void setProjectionMatrix(mat4 proj) { this.mProjection = proj; }
}