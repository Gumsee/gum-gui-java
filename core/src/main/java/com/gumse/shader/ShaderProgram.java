package com.gumse.shader;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.opengl.GL30;

import com.gumse.maths.*;
import com.gumse.tools.Output;
import com.gumse.tools.Toolbox;

public class ShaderProgram
{
	private static ShaderProgram pCurrentlyBoundShaderProgram = null;

	private ArrayList<Shader> vShaders;
	private Map<String, Integer> Locations;
	private Map<String, Integer> Attributes;

	private int iProgramID;
	private String sName;
	//Compiles the shaders into a form that your GPU can understand
	private void compileShaders() 
	{
		for(int i = 0; i < vShaders.size(); i++)
		{
			if(!vShaders.get(i).compile())
				break;
		}
	}

	private void linkShaders() 
	{
		for(int i = 0; i < vShaders.size(); i++)
		{
			GL30.glAttachShader(this.iProgramID, vShaders.get(i).getShaderID());
		}

		//Link program
		GL30.glLinkProgram(this.iProgramID);
		if(GL30.glGetProgrami(this.iProgramID, GL30.GL_LINK_STATUS) == GL30.GL_FALSE)
		{
			String errorLog = GL30.glGetProgramInfoLog(iProgramID, 1024);
			GL30.glDeleteProgram(this.iProgramID); //We don't need the program anymore.
			Output.error("ShaderProgram: Linking Error: " + this.sName + ": " + errorLog);
		}
		

		//Validate program
		/*GL30.glValidateProgram(iProgramID);
		if(GL30.glGetProgrami(this.iProgramID, GL30.GL_VALIDATE_STATUS) == GL30.GL_FALSE)
		{
			String errorLog = GL30.glGetProgramInfoLog(iProgramID, 1024);
			GL30.glDeleteProgram(this.iProgramID); //We don't need the program anymore.
			Debug.error("ShaderProgram: Validation Error: " + this.sName + ": " + errorLog);
		}*/
		

		//Always detach shaders after a successful link.
		for(int i = 0; i < vShaders.size(); i++)
		{
			GL30.glDetachShader(this.iProgramID, vShaders.get(i).getShaderID());
			//glDeleteShader(vShaders[i].getShaderID());
		}
	}

	//The : _numAttributes(0) ect. is an initialization list. It is a better way to initialize variables, since it avoids an extra copy. 
	public ShaderProgram()  
	{
		vShaders = new ArrayList<Shader>();
		Locations = new HashMap<String, Integer>();
		Attributes = new HashMap<String, Integer>();
	}

	public void cleanup() 
	{
		if(this.iProgramID != 0)
			GL30.glDeleteProgram(this.iProgramID);
		
		//for(size_t i = 0; i < vShaders.size(); i++)
		//	Gum::_delete(vShaders[i]);

		Locations.clear();
		vShaders.clear();
	}


	//Adds an attribute to our shader. SHould be called between compiling and linking.
	public void addAttribute(String attributeName, int number) 
	{
		GL30.glBindAttribLocation(this.iProgramID, number, attributeName);
		Attributes.put(attributeName, number);
	}

	public void addUniform(String Name) { Locations.put(Name, GetUniformLocation(Name)); }
	public void addUniform(String Name, int size)
	{
		for (int i = 0; i < size; i++)
		{
			addUniform(Name + "[" + i + "]");
		}
	}

	public void addTexture(String Name, int index)
	{
		Locations.put(Name, GetUniformLocation(Name));
		use();
		loadUniform(Name, index);
		unuse();
	}

	public void use()   { GL30.glUseProgram(iProgramID); setCurrentlyBoundShader(this); }
	public void unuse() { GL30.glUseProgram(0);          setCurrentlyBoundShader(null); }


	public int GetUniformLocation(String UniformName) { return GL30.glGetUniformLocation(iProgramID, UniformName); }
	public void addShader(Shader shader)              { this.vShaders.add(shader); }
	public void removeShader(int index)               { this.vShaders.remove(index); }


	public void build(String name)
	{
		Map<String, Integer> defaultShaderAttributes = new HashMap<String, Integer>();
		defaultShaderAttributes.put("vertexPosition", 0);
		defaultShaderAttributes.put("TextureCoords", 1);
		defaultShaderAttributes.put("Normals", 2);
		defaultShaderAttributes.put("TransMatrix", 3);
		defaultShaderAttributes.put("tangentNormals", 7);
		defaultShaderAttributes.put("jointIndices", 8);
		defaultShaderAttributes.put("weights", 9);
		defaultShaderAttributes.put("individualColor", 10);

		build(name, defaultShaderAttributes);
	}

	public void build(String name, Map<String, Integer> attributes)
	{
		this.sName = name;
		Output.debug("ShaderProgram: Creating Shader Program for " + sName);
		iProgramID = GL30.glCreateProgram();
		compileShaders();
		

		for(Map.Entry<String, Integer> attribute : attributes.entrySet())
		{
			addAttribute(attribute.getKey(), attribute.getValue());
			Output.debug("Adding attribute " + attribute.getKey() + " (" + attribute.getValue() + ")");
		}

		if(attributes.size() > 0)
		{
			Output.debug("");
		}

		Output.debug("ShaderProgram: Linking " + name);
		linkShaders();


		Output.debug("ShaderProgram: Adding default Uniforms " + name);
		addUniform("transformationMatrix");
		addUniform("viewMatrix");
		addUniform("projectionMatrix");
	}

	public void rebuild()
	{
		//glDeleteProgram(iProgramID);
		//build();
	}

	public void loadUniform(String uniformname, boolean var)   			{ GL30.glUniform1i(Locations.get(uniformname), var ? 1 : 0); }
	public void loadUniform(String uniformname, vec2 var) 				{ GL30.glUniform2f(Locations.get(uniformname), var.x, var.y); }
	public void loadUniform(String uniformname, ivec2 var) 				{ GL30.glUniform2i(Locations.get(uniformname), var.x, var.y); }
	public void loadUniform(String uniformname, vec3 var) 				{ GL30.glUniform3f(Locations.get(uniformname), var.x, var.y, var.z); }
	//public void LoadUniform(String uniformName, ivec3 var) 		    	{ GL30.glUniform3i(Locations.get(uniformName), var.x, var.y, var.z); }
	public void loadUniform(String uniformname, vec4 var) 				{ GL30.glUniform4f(Locations.get(uniformname), var.x, var.y, var.z, var.w); }
	public void loadUniform(String uniformname, mat4 var) 			    { FloatBuffer mat = Toolbox.array2D2FloatBuffer(var.getData()); GL30.glUniformMatrix4fv(Locations.get(uniformname), false, mat); }
	public void loadUniform(String uniformname, float var) 				{ GL30.glUniform1f(Locations.get(uniformname), var); }
	public void loadUniform(String uniformname, int var) 				{ GL30.glUniform1i(Locations.get(uniformname), var); }
    //public void loadUniform(String uniformName, mat4 value)             { FloatBuffer mat = Toolbox.array2D2FloatBuffer(value.getData()); GL30.glUniformMatrix4fv(Locations.get(uniformName), false, mat); }
	//public void LoadUniform(String uniformName, ArrayList<mat4> var) 	{ for (int i = 0; i < var.size(); i++) { LoadUniform(uniformName + "[" + i + "]", var.get(i)); } }


	//
	// Setter
	//
	public void setName(String name) 				            { this.sName = name; }
	public static void setCurrentlyBoundShader(ShaderProgram program) { pCurrentlyBoundShaderProgram = program; }


	//
	// Getter
	//
	public String getName()           					        { return sName; }
	public int getProgramID()           				        { return iProgramID; }
	public static ShaderProgram getCurrentlyBoundShader() 			{ return pCurrentlyBoundShaderProgram; }
	public Shader getShader(int index) 						{ return this.vShaders.get(index); }
};