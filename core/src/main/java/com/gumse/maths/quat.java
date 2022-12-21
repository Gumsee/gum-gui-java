package com.gumse.maths;

public class quat
{
    public float x, y, z, w;
    
    public quat()        { x = y = z = w = 0; }
    public quat(float f) { x = y = z = w = f; }
    public quat(float sx, float sy, float sz, float sw) { x = sx; y = sy; z = sz; w = sw; }
    public quat(vec3 vec, float sw) { x = vec.x; y = vec.y; z = vec.z; w = sw; }
    public quat(mat3 mat)
    {
        w = (float)Math.sqrt(1.0 + mat.get(0, 0) + mat.get(1, 1) + mat.get(2, 2)) / 2.0f;
        float w4 = (4.0f * w);
        x = (mat.get(2, 1) - mat.get(1, 2)) / w4;
        y = (mat.get(0, 2) - mat.get(2, 0)) / w4;
        z = (mat.get(1, 0) - mat.get(0, 1)) / w4;
    }
    
    
    public void add(quat q)  { this.x += q.x; this.y += q.y; this.z += q.z; this.w += q.w; }
    public void sub(quat q)  { this.x -= q.x; this.y -= q.y; this.z -= q.z; this.w -= q.w; }
    public void mul(float f) { this.x *= f;   this.y *= f;   this.z *= f;   this.w *= f; }
    
    public static quat add(quat q1, quat q2)  { return new quat(q1.x + q2.x, q1.y + q2.y, q1.z + q2.z, q1.w + q2.w); }
    public static quat sub(quat q1, quat q2)  { return new quat(q1.x - q2.x, q1.y - q2.y, q1.z - q2.z, q1.w - q2.w); }
    public static quat mul(quat q1, quat q2)  
    { 
        return new quat(
            q1.w * q2.x + q1.x * q2.w + q1.y * q2.z - q1.z * q2.y, 
            q1.w * q2.y + q1.y * q2.w + q1.z * q2.x - q1.x * q2.z, 
            q1.w * q2.z + q1.z * q2.w + q1.x * q2.y - q1.y * q2.x, 
            q1.w * q2.w - q1.x * q2.x - q1.y * q2.y - q1.z * q2.z
        ); 
    }
    public static quat mul(quat q1, float f)  { return new quat(q1.x * f, q1.y * f, q1.z * f, q1.w * f); }
    public static quat div(quat q1, float f)  { return new quat(q1.x / f, q1.y / f, q1.z / f, q1.w / f); }
    
    public float valueByIndex(int index)
    {
        if     (index == 0) return x;
        else if(index == 1) return y;
        else if(index == 2) return z;
        else if(index == 3) return w;
        else                return 0;
    }
    
    public static quat slerp(quat a, quat b, float f)
    {
        // quaternion to return
        quat qm = new quat();
        // Calculate angle between them.
        double cosHalfTheta = a.w * b.w + a.x * b.x + a.y * b.y + a.z * b.z;
        // if qa=qb or qa=-qb then theta = 0 and we can return qa
        if (Math.abs(cosHalfTheta) >= 1.0){
            qm.x = a.x; qm.y = a.y; qm.z = a.z; qm.w = a.w;
            return qm;
        }
        // Calculate temporary values.
        double halfTheta = Math.acos(cosHalfTheta);
        double sinHalfTheta = Math.sqrt(1.0 - cosHalfTheta*cosHalfTheta);
        // if theta = 180 degrees then result is not fully defined
        // we could rotate around any axis normal to qa or qb
        if (Math.abs(sinHalfTheta) < 0.001){ // fabs is floating point absolute
            qm.w = (a.w * 0.5f + b.w * 0.5f);
            qm.x = (a.x * 0.5f + b.x * 0.5f);
            qm.y = (a.y * 0.5f + b.y * 0.5f);
            qm.z = (a.z * 0.5f + b.z * 0.5f);
            return qm;
        }
        double ratioA = Math.sin((1 - f) * halfTheta) / sinHalfTheta;
        double ratioB = Math.sin(f * halfTheta) / sinHalfTheta; 
        //calculate Quaternion.
        qm.w = (float)(a.w * ratioA + b.w * ratioB);
        qm.x = (float)(a.x * ratioA + b.x * ratioB);
        qm.y = (float)(a.y * ratioA + b.y * ratioB);
        qm.z = (float)(a.z * ratioA + b.z * ratioB);
        return qm;
    }
    
    public static quat normalize(quat q)
    {
        float length_of_v = (float)Math.sqrt((q.x * q.x) + (q.y * q.y) + (q.z * q.z) + (q.w * q.w));;
        return new quat(q.x / length_of_v, q.y / length_of_v, q.z / length_of_v, q.w / length_of_v);
    }
    
    public static quat rotateAround(float angle, vec3 up)
    {
        float rad = (float)(angle * Math.PI / 180.0);
        float s = (float)Math.sin(rad * 0.5f);
        return new quat(vec3.mul(up, s), (float)Math.cos(rad * 0.5));
    }
    
    public static quat toQuaternion(vec3 anglesDeg)
    {
        vec3 rad = vec3.div(vec3.mul(anglesDeg, (float)Math.PI), 180.0f);
        // Abbreviations for the various angular functions
        double cy = Math.cos(rad.z * 0.5);
        double sy = Math.sin(rad.z * 0.5);
        double cp = Math.cos(rad.y * 0.5);
        double sp = Math.sin(rad.y * 0.5);
        double cr = Math.cos(rad.x * 0.5);
        double sr = Math.sin(rad.x * 0.5);
    
        quat q = new quat();
        q.w = (float)(cr * cp * cy + sr * sp * sy);
        q.x = (float)(sr * cp * cy - cr * sp * sy);
        q.y = (float)(cr * sp * cy + sr * cp * sy);
        q.z = (float)(cr * cp * sy - sr * sp * cy);
    
        return q;
    }
    
    public static vec3 toEuler(quat q)
    {
        vec3 euler = new vec3();
        // roll (x-axis rotation)
        double sinr_cosp = 2 * (q.w * q.x + q.y * q.z);
        double cosr_cosp = 1 - 2 * (q.x * q.x + q.y * q.y);
        euler.x = (float)Math.atan2(sinr_cosp, cosr_cosp);
    
        // pitch (y-axis rotation)
        double sinp = 2 * (q.w * q.y - q.z * q.x);
        if (Math.abs(sinp) >= 1)
            euler.y = (float)Math.copySign(Math.PI / 2.0, sinp); // use 90 degrees if out of range
        else
            euler.y = (float)Math.asin(sinp);
    
        // yaw (z-axis rotation)
        double siny_cosp = 2 * (q.w * q.z + q.x * q.y);
        double cosy_cosp = 1 - 2 * (q.y * q.y + q.z * q.z);
        euler.z = (float)Math.atan2(siny_cosp, cosy_cosp);
    
        return vec3.div(vec3.mul(euler, 180.0f), (float)Math.PI);
    }
    
    
    public static float dot(quat a, quat b)  { return a.x * b.x + a.y * b.y + a.z * b.z + a.w * b.w; }
    
    public static quat rotateTowards(quat from, quat to, float speed)
    {
        float cosTheta = dot(from, to);
    
        if(cosTheta > 0.9999f) { return to; } // q1 and q2 are already equal. Force q2 just to be sure
    
        if (cosTheta < 0) // Avoid taking the long path around the sphere
        {
            from = quat.mul(from, -1.0f);
            cosTheta *= -1.0f;
        }
    
        float angle = (float)Math.acos(cosTheta);
        if (angle < speed) { return to; }
    
        float fT = speed / angle;
        angle = speed;
    
        quat res = quat.div(
                        quat.add(
                                  quat.mul(from, (float)Math.sin((1.0f - fT) * angle)), 
                                  quat.mul(to,   (float)Math.sin(fT * angle))
                                ), 
                        (float)Math.sin(angle)
                        );
        res = quat.normalize(res);
        return res;
    }
    
    public String toString()
    {
        return toString(true, "quat(", ")", ", ");
    }

    public String toString(boolean oneline, String prefix, String suffix, String delimiter)
    {
        return prefix + this.x + delimiter + this.y + delimiter + this.z + delimiter + this.w + suffix;
    }    
};