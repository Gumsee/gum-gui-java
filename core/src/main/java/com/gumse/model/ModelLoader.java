package com.gumse.model;

import java.util.ArrayList;

import org.lwjgl.assimp.AIFace;
import org.lwjgl.assimp.AIMatrix4x4;
import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AINode;
import org.lwjgl.assimp.AIPropertyStore;
import org.lwjgl.assimp.AIScene;
import org.lwjgl.assimp.Assimp;

import com.gumse.maths.mat4;
import com.gumse.maths.vec2;
import com.gumse.tools.*;
import org.lwjgl.system.MemoryUtil;
import java.nio.ByteBuffer;


public class ModelLoader 
{
    private AIScene pScene;
    private ArrayList<Mesh> alMeshes;
    public ModelLoader()
    {
        alMeshes = new ArrayList<Mesh>();
    }
    
    static mat4 mat4_cast(AIMatrix4x4 m) { 
        return new mat4(m.a1(), m.a2(), m.a3(), m.a4(),
                        m.b1(), m.b2(), m.b3(), m.b4(),
                        m.c1(), m.c2(), m.c3(), m.c4(),
                        m.d1(), m.d2(), m.d3(), m.d4());
    }
    
    public void load(String filename, Class<?> classtouse)
    {
        AIPropertyStore settings = Assimp.aiCreatePropertyStore();
        Assimp.aiSetImportPropertyInteger(settings, Assimp.AI_CONFIG_PP_SLM_VERTEX_LIMIT, 65535);


        ByteBuffer modelBuffer;
        modelBuffer = Toolbox.loadResourceToByteBuffer(filename, classtouse);
        //Debug.info(Integer.toString(imageBuffer.capacity()));
        if(modelBuffer.equals(null))
        {
            throw new RuntimeException();
        }

        pScene = Assimp.aiImportFileFromMemory(modelBuffer,
            Assimp.aiProcess_Triangulate | //Assimp.aiProcess_GenSmoothNormals | 
            Assimp.aiProcess_FlipUVs | Assimp.aiProcess_CalcTangentSpace | 
            Assimp.aiProcess_LimitBoneWeights | Assimp.aiProcess_SplitLargeMeshes | 
            Assimp.aiProcess_OptimizeMeshes | Assimp.aiProcess_JoinIdenticalVertices, "");



        // check for errors
        if(pScene == null || (pScene.mFlags() & Assimp.AI_SCENE_FLAGS_INCOMPLETE) == 1) // if is Not Zero
        {
            Debug.error("ObjectLoader: " + filename + " ERROR::ASSIMP:: " + Assimp.aiGetErrorString());
            return;
        }
        this.processNode(pScene.mRootNode());
        Debug.info("Loaded Object: " + filename + ", numMeshes: " + Integer.toString(alMeshes.size()) + ", Children: " + Integer.toString(pScene.mRootNode().mNumChildren()));

        MemoryUtil.memFree(modelBuffer);
    }

    private void processNode(AINode node)
    {
        // process each mesh located at the current node
        for(int i = 0; i < node.mNumMeshes(); i++)
        {
            // the node object only contains indices to index the actual objects in the scene. 
            // the scene contains all the data, node is just to keep stuff organized (like relations between nodes).
            AIMesh mesh = AIMesh.create(pScene.mMeshes().get(node.mMeshes().get(i)));
            processMesh(mesh);
            //retMesh.offsetMatrix = mat4_cast(pScene.mRootNode().mTransformation()).mul(mat4_cast(node.mTransformation()));
        }
        
        // after we've processed all of the meshes (if any) we then recursively process each of the children nodes
        for(int i = 0; i < node.mNumChildren(); i++)
        {
            AINode childNode = AINode.create(node.mChildren().get(i));
            processNode(childNode);
        }
    } 

    private Mesh processMesh(AIMesh mesh)
    {
        Mesh pMesh = new Mesh();
        pMesh.sName = mesh.mName().toString();
        //pMesh.iMatIndex = mesh.mMaterialIndex();

        // Walk through each of the mesh's positions
        for(int i = 0; i < mesh.mNumVertices(); i++)
        {
            Vertex currentVertex = new Vertex();
            // positions
            currentVertex.vPosition.x = mesh.mVertices().get(i).x();
            currentVertex.vPosition.y = mesh.mVertices().get(i).y();
            currentVertex.vPosition.z = mesh.mVertices().get(i).z();

            // normals
            currentVertex.vNormal.x = mesh.mNormals().get(i).x();
            currentVertex.vNormal.y = mesh.mNormals().get(i).y();
            currentVertex.vNormal.z = mesh.mNormals().get(i).z();

            // texture coordinates
            if(mesh.mTextureCoords().get(0) != 0) // does the mesh contain texture coordinates?
                currentVertex.vTexcoord = new vec2(mesh.mTextureCoords(0).get(i).x(), mesh.mTextureCoords(0).get(i).y());
            
            // tangent
            currentVertex.vTangent.x = mesh.mTangents().get(i).x();
            currentVertex.vTangent.y = mesh.mTangents().get(i).y();
            currentVertex.vTangent.z = mesh.mTangents().get(i).z();
            // // bitangent
            // vector.x = mesh.mBitangents[i].x;
            // vector.y = mesh.mBitangents[i].y;
            // vector.z = mesh.mBitangents[i].z;
            // vertex.Bitangent = vector;

            pMesh.alVertices.add(currentVertex);
        }

        // now go through each of the mesh's faces (a face is a mesh its triangle) and retrieve the corresponding vertex indices.
        for(int i = 0; i < mesh.mNumFaces(); i++)
        {
            AIFace face = mesh.mFaces().get(i);
            
            // retrieve all indices of the face and store them in the indices vector
            for(int j = 0; j < face.mNumIndices(); j++)
                pMesh.alIndices.add(face.mIndices().get(j));
        }

        alMeshes.add(pMesh);
        return pMesh;
    }

    public AIScene getScene()      { return this.pScene; }
    public int numMeshes()         { return alMeshes.size(); }
    public Mesh getMesh(int index) { return alMeshes.get(index); }
    
}
