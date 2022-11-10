package com.gumse.model;

import java.util.ArrayList;

import com.gumse.maths.vec2;
import com.gumse.maths.vec3;

public class Mesh 
{
    public ArrayList<Vertex> alVertices;
    public ArrayList<Integer> alIndices;
    public String sName;

    public Mesh() 
    {
        sName = "";
        alVertices = new ArrayList<Vertex>();
        alIndices = new ArrayList<Integer>();
    };



    public void addTriangle(int a, int b, int c)
    {
        alIndices.add(a);
        alIndices.add(b);
        alIndices.add(c);
    }

    public void addQuad(int a, int b, int c, int d)
    {
        alIndices.add(a);
        alIndices.add(b);
        alIndices.add(c);
        alIndices.add(a);
        alIndices.add(c);
        alIndices.add(d);
    }



    public void addMesh(Mesh mesh)
    {
        int IndexOffset = this.alVertices.size();
        for(int i = 0; i < mesh.alVertices.size(); i++)
            this.alVertices.add(mesh.alVertices.get(i));

        for(int i = 0; i < mesh.alIndices.size(); i++)
            this.alIndices.add(mesh.alIndices.get(i) + IndexOffset);
    }



    public static Mesh generateUVSphere(int parallels, int meridians)
    {
        Mesh mesh = new Mesh();
        mesh.alVertices.add(new Vertex(new vec3(0.0f, 1.0f, 0.0f), new vec2(0,0), new vec3(0,1,0)));
            for (int j = 0; j < parallels - 1; ++j)
            {
                float polar = (float)Math.PI * (j+1) / parallels;
                float sp = (float)Math.sin(polar);
                float cp = (float)Math.cos(polar);
                for (int i = 0; i < meridians; ++i)
                {
                    float azimuth = 2.0f * (float)Math.PI * (float)i / (float)meridians;
                    float sa = (float)Math.sin(azimuth);
                    float ca = (float)Math.cos(azimuth);
                    float x = sp * ca;
                    float y = cp;
                    float z = sp * sa;
                    mesh.alVertices.add(new Vertex(new vec3(x, y, z), new vec2(0,0), new vec3(x,y,z)));
                }
            }
            mesh.alVertices.add(new Vertex(new vec3(0.0f, -1.0f, 0.0f), new vec2(0,0), new vec3(0,-1,0)));

            for (int i = 0; i < meridians; ++i)
            {
                int a = i + 1;
                int b = (i + 1) % meridians + 1;
                mesh.addTriangle(0, b, a);
            }

            for (int j = 0; j < parallels - 2; ++j)
            {
                int aStart = j * meridians + 1;
                int bStart = (j + 1) * meridians + 1;
                for (int i = 0; i < meridians; ++i)
                {
                    int a = aStart + i;
                    int a1 = aStart + (i + 1) % meridians;
                    int b = bStart + i;
                    int b1 = bStart + (i + 1) % meridians;
                    mesh.addQuad(a, a1, b1, b);
                }
            }

            for (int i = 0; i < meridians; ++i)
            {
                int a = i + meridians * (parallels - 2) + 1;
                int b = (i + 1) % meridians + meridians * (parallels - 2) + 1;
                mesh.addTriangle(mesh.alVertices.size() - 1, a, b);
            }

        return mesh;
    }

    public static Mesh generateCube(vec3 dimensions)
    {
        Mesh mesh = new Mesh();
        mesh.alVertices.clear();
        mesh.alVertices.add(new Vertex(new vec3(-dimensions.x,  dimensions.y,  dimensions.z), new vec2(1.0f, 0.0f), new vec3(-1.0f,  0.0f,  0.0f), new vec3( 0.0f, 0.0f, 1.0f)));
        mesh.alVertices.add(new Vertex(new vec3(-dimensions.x, -dimensions.y, -dimensions.z), new vec2(0.0f, 1.0f), new vec3(-1.0f,  0.0f,  0.0f), new vec3( 0.0f, 0.0f, 1.0f))); 
        mesh.alVertices.add(new Vertex(new vec3(-dimensions.x, -dimensions.y,  dimensions.z), new vec2(1.0f, 1.0f), new vec3(-1.0f,  0.0f,  0.0f), new vec3( 0.0f, 0.0f, 1.0f))); 
        mesh.alVertices.add(new Vertex(new vec3(-dimensions.x,  dimensions.y, -dimensions.z), new vec2(1.0f, 0.0f), new vec3( 0.0f,  0.0f, -1.0f), new vec3(-1.0f, 0.0f, 0.0f))); 
        mesh.alVertices.add(new Vertex(new vec3( dimensions.x, -dimensions.y, -dimensions.z), new vec2(0.0f, 1.0f), new vec3( 0.0f,  0.0f, -1.0f), new vec3(-1.0f, 0.0f, 0.0f))); 
        mesh.alVertices.add(new Vertex(new vec3(-dimensions.x, -dimensions.y, -dimensions.z), new vec2(1.0f, 1.0f), new vec3( 0.0f,  0.0f, -1.0f), new vec3(-1.0f, 0.0f, 0.0f))); 
        mesh.alVertices.add(new Vertex(new vec3( dimensions.x,  dimensions.y, -dimensions.z), new vec2(0.0f, 1.0f), new vec3( 1.0f,  0.0f,  0.0f), new vec3( 0.0f, 0.0f, 1.0f))); 
        mesh.alVertices.add(new Vertex(new vec3( dimensions.x, -dimensions.y,  dimensions.z), new vec2(1.0f, 0.0f), new vec3( 1.0f,  0.0f,  0.0f), new vec3( 0.0f, 0.0f, 1.0f))); 
        mesh.alVertices.add(new Vertex(new vec3( dimensions.x, -dimensions.y, -dimensions.z), new vec2(0.0f, 0.0f), new vec3( 1.0f,  0.0f,  0.0f), new vec3( 0.0f, 0.0f, 1.0f))); 
        mesh.alVertices.add(new Vertex(new vec3( dimensions.x,  dimensions.y,  dimensions.z), new vec2(0.0f, 1.0f), new vec3( 0.0f,  0.0f,  1.0f), new vec3(-1.0f, 0.0f, 0.0f))); 
        mesh.alVertices.add(new Vertex(new vec3(-dimensions.x, -dimensions.y,  dimensions.z), new vec2(1.0f, 0.0f), new vec3( 0.0f,  0.0f,  1.0f), new vec3(-1.0f, 0.0f, 0.0f))); 
        mesh.alVertices.add(new Vertex(new vec3( dimensions.x, -dimensions.y,  dimensions.z), new vec2(0.0f, 0.0f), new vec3( 0.0f,  0.0f,  1.0f), new vec3(-1.0f, 0.0f, 0.0f))); 
        mesh.alVertices.add(new Vertex(new vec3( dimensions.x, -dimensions.y, -dimensions.z), new vec2(1.0f, 1.0f), new vec3( 0.0f, -1.0f,  0.0f), new vec3( 1.0f, 0.0f, 0.0f))); 
        mesh.alVertices.add(new Vertex(new vec3(-dimensions.x, -dimensions.y,  dimensions.z), new vec2(0.0f, 0.0f), new vec3( 0.0f, -1.0f,  0.0f), new vec3( 1.0f, 0.0f, 0.0f))); 
        mesh.alVertices.add(new Vertex(new vec3(-dimensions.x, -dimensions.y, -dimensions.z), new vec2(0.0f, 1.0f), new vec3( 0.0f, -1.0f,  0.0f), new vec3( 1.0f, 0.0f, 0.0f))); 
        mesh.alVertices.add(new Vertex(new vec3(-dimensions.x,  dimensions.y, -dimensions.z), new vec2(1.0f, 1.0f), new vec3( 0.0f,  1.0f,  0.0f), new vec3(-1.0f, 0.0f, 0.0f))); 
        mesh.alVertices.add(new Vertex(new vec3( dimensions.x,  dimensions.y,  dimensions.z), new vec2(0.0f, 0.0f), new vec3( 0.0f,  1.0f,  0.0f), new vec3(-1.0f, 0.0f, 0.0f))); 
        mesh.alVertices.add(new Vertex(new vec3( dimensions.x,  dimensions.y, -dimensions.z), new vec2(0.0f, 1.0f), new vec3( 0.0f,  1.0f,  0.0f), new vec3(-1.0f, 0.0f, 0.0f))); 
        mesh.alVertices.add(new Vertex(new vec3(-dimensions.x,  dimensions.y,  dimensions.z), new vec2(1.0f, 0.0f), new vec3(-1.0f,  0.0f,  0.0f), new vec3( 0.0f, 0.0f, 1.0f))); 
        mesh.alVertices.add(new Vertex(new vec3(-dimensions.x,  dimensions.y, -dimensions.z), new vec2(0.0f, 0.0f), new vec3(-1.0f,  0.0f,  0.0f), new vec3( 0.0f, 0.0f, 1.0f))); 
        mesh.alVertices.add(new Vertex(new vec3(-dimensions.x, -dimensions.y, -dimensions.z), new vec2(0.0f, 1.0f), new vec3(-1.0f,  0.0f,  0.0f), new vec3( 0.0f, 0.0f, 1.0f))); 
        mesh.alVertices.add(new Vertex(new vec3(-dimensions.x,  dimensions.y, -dimensions.z), new vec2(1.0f, 0.0f), new vec3( 0.0f,  0.0f, -1.0f), new vec3(-1.0f, 0.0f, 0.0f))); 
        mesh.alVertices.add(new Vertex(new vec3( dimensions.x,  dimensions.y, -dimensions.z), new vec2(0.0f, 0.0f), new vec3( 0.0f,  0.0f, -1.0f), new vec3(-1.0f, 0.0f, 0.0f))); 
        mesh.alVertices.add(new Vertex(new vec3( dimensions.x, -dimensions.y, -dimensions.z), new vec2(0.0f, 1.0f), new vec3( 0.0f,  0.0f, -1.0f), new vec3(-1.0f, 0.0f, 0.0f))); 
        mesh.alVertices.add(new Vertex(new vec3( dimensions.x,  dimensions.y, -dimensions.z), new vec2(0.0f, 1.0f), new vec3( 1.0f,  0.0f,  0.0f), new vec3( 0.0f, 0.0f, 1.0f))); 
        mesh.alVertices.add(new Vertex(new vec3( dimensions.x,  dimensions.y,  dimensions.z), new vec2(1.0f, 1.0f), new vec3( 1.0f,  0.0f,  0.0f), new vec3( 0.0f, 0.0f, 1.0f))); 
        mesh.alVertices.add(new Vertex(new vec3( dimensions.x, -dimensions.y,  dimensions.z), new vec2(1.0f, 0.0f), new vec3( 1.0f,  0.0f,  0.0f), new vec3( 0.0f, 0.0f, 1.0f))); 
        mesh.alVertices.add(new Vertex(new vec3( dimensions.x,  dimensions.y,  dimensions.z), new vec2(0.0f, 1.0f), new vec3( 0.0f,  0.0f,  1.0f), new vec3(-1.0f, 0.0f, 0.0f))); 
        mesh.alVertices.add(new Vertex(new vec3(-dimensions.x,  dimensions.y,  dimensions.z), new vec2(1.0f, 1.0f), new vec3( 0.0f,  0.0f,  1.0f), new vec3(-1.0f, 0.0f, 0.0f))); 
        mesh.alVertices.add(new Vertex(new vec3(-dimensions.x, -dimensions.y,  dimensions.z), new vec2(1.0f, 0.0f), new vec3( 0.0f,  0.0f,  1.0f), new vec3(-1.0f, 0.0f, 0.0f))); 
        mesh.alVertices.add(new Vertex(new vec3( dimensions.x, -dimensions.y, -dimensions.z), new vec2(1.0f, 1.0f), new vec3( 0.0f, -1.0f,  0.0f), new vec3( 1.0f, 0.0f, 0.0f))); 
        mesh.alVertices.add(new Vertex(new vec3( dimensions.x, -dimensions.y,  dimensions.z), new vec2(1.0f, 0.0f), new vec3( 0.0f, -1.0f,  0.0f), new vec3( 1.0f, 0.0f, 0.0f))); 
        mesh.alVertices.add(new Vertex(new vec3(-dimensions.x, -dimensions.y,  dimensions.z), new vec2(0.0f, 0.0f), new vec3( 0.0f, -1.0f,  0.0f), new vec3( 1.0f, 0.0f, 0.0f))); 
        mesh.alVertices.add(new Vertex(new vec3(-dimensions.x,  dimensions.y, -dimensions.z), new vec2(1.0f, 1.0f), new vec3( 0.0f,  1.0f,  0.0f), new vec3(-1.0f, 0.0f, 0.0f))); 
        mesh.alVertices.add(new Vertex(new vec3(-dimensions.x,  dimensions.y,  dimensions.z), new vec2(1.0f, 0.0f), new vec3( 0.0f,  1.0f,  0.0f), new vec3(-1.0f, 0.0f, 0.0f))); 
        mesh.alVertices.add(new Vertex(new vec3( dimensions.x,  dimensions.y,  dimensions.z), new vec2(0.0f, 0.0f), new vec3( 0.0f,  1.0f,  0.0f), new vec3(-1.0f, 0.0f, 0.0f)));        
        //mesh.alIndices = ArrayList<Float>(Arrays.asList({0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35 }));
        return mesh;
    }

    public ArrayList<Float> getVertexPositions()
    {
        ArrayList<Float> retList = new ArrayList<Float>();
        for(int i = 0; i < this.alVertices.size(); i++)
        {
            retList.add(this.alVertices.get(i).vPosition.x);
            retList.add(this.alVertices.get(i).vPosition.y);
            retList.add(this.alVertices.get(i).vPosition.z);
        }
        return retList;
    }
    public ArrayList<Float> getVertexTexcoords()
    {
        ArrayList<Float> retList = new ArrayList<Float>();
        for(int i = 0; i < this.alVertices.size(); i++)
        {
            retList.add(this.alVertices.get(i).vTexcoord.x);
            retList.add(this.alVertices.get(i).vTexcoord.y);
        }
        return retList;
    }
    public ArrayList<Float> getVertexNormals()
    {
        ArrayList<Float> retList = new ArrayList<Float>();
        for(int i = 0; i < this.alVertices.size(); i++)
        {
            retList.add(this.alVertices.get(i).vNormal.x);
            retList.add(this.alVertices.get(i).vNormal.y);
            retList.add(this.alVertices.get(i).vNormal.z);
        }
        return retList;
    }
}