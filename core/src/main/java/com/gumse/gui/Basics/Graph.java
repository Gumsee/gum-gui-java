package com.gumse.gui.Basics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import com.gumse.gui.Font.FontManager;
import com.gumse.gui.Primitives.Box;
import com.gumse.gui.Primitives.RenderGUI;
import com.gumse.gui.Primitives.Text;
import com.gumse.maths.ivec2;
import com.gumse.maths.vec2;
import com.gumse.maths.vec3;
import com.gumse.maths.vec4;
import com.gumse.model.VertexArrayObject;
import com.gumse.model.VertexBufferObject;
import com.gumse.shader.Shader;
import com.gumse.shader.ShaderProgram;
import com.gumse.system.Window;

public class Graph extends RenderGUI
{
    private Box background;
    private Text dataText;
    private Text caption;

    private ArrayList<Float> vPositionsData;
    private ArrayList<Float> vData;

    private vec2 pixelSize;
    private String ValueType = "";
    
    private VertexArrayObject pVertexArrayObject;
    private VertexBufferObject pVertexBuffer;

    private int precision = 3;
    private float fBiggestNum;
    private float fSmallestNum;
    private float fSpeed;
    private float fScale;

    private static ShaderProgram GraphShader = null;

    static void initGraphShader()
    {
        if(GraphShader == null)
        {
            Shader vertexShader = new Shader(Shader.SHADER_VERSION_STR + "\n" +
                "in vec3 vertexPosition;\n" +
                "out vec2 Texcoord;\n" +
    
                "void main() \n" +
                "{\n" +
                    "vec2 finalpos = vertexPosition.xy;\n" +
                    "if(finalpos.y > 1) { finalpos.y = 1; }\n" +
                    "gl_Position = vec4(finalpos, 0.0f, 1.0f);\n" +
                    "Texcoord = vec2((vertexPosition.x+1.0)/2.0 * 1, 1 - (vertexPosition.y+1.0)/2.0 * 1);\n" +
                "}\n"
            , Shader.TYPES.VERTEX_SHADER);
    
            Shader fragShader = new Shader(Shader.SHADER_VERSION_STR + "\n" +
                "in vec2 Texcoord;\n" +
                "uniform vec4 color;\n" +
                "out vec4 fragColor;\n" +
    
                "void main(void)\n" +
                "{\n" +
                    "fragColor = color;\n" +
                "}"
                , Shader.TYPES.FRAGMENT_SHADER);
    
            GraphShader = new ShaderProgram();
            GraphShader.addShader(vertexShader);
            GraphShader.addShader(fragShader);
            Map<String, Integer> attrMap = new HashMap<>();
            attrMap.put("vertexPosition", 0);
            GraphShader.build("GraphShader", attrMap);
            GraphShader.addUniform("color");
            GraphShader.addUniform("transformationMatrix");
            GraphShader.addUniform("projectionMatrix");
            GraphShader.addUniform("viewMatrix");
        }
    }
    
    
    public Graph(String name, ivec2 pos, ivec2 size)
    {
        this.sType = "Graph";
        this.vPos.set(pos);
        this.vSize.set(size);
        this.fBiggestNum = 0.0f;
        this.fSmallestNum = Float.MAX_VALUE;
        this.fSpeed = 0.5f;
        this.v4Color = new vec4(0.9f, 0.9f, 0.9f, 1.0f);
        this.vData = new ArrayList<>();
        this.vPositionsData = new ArrayList<>();
    
    
        background = new Box(new ivec2(0,0), new ivec2(100, 100));
        background.setCornerRadius(new vec4(10.0f));
        background.setSizeInPercent(true, true);
        background.setColor(new vec4(0,0,0, 0.5f));
        addElement(background);
    
        dataText = new Text("0", FontManager.getInstance().getDefaultFont(), new ivec2(0, 5), 0);
        dataText.setPositionInPercent(false, false);
        dataText.setCharacterHeight(20);
        dataText.setColor(v4Color);
        addElement(dataText);
    
        caption = new Text(name, FontManager.getInstance().getDefaultFont(), new ivec2(5, 5), 0);
        caption.setPositionInPercent(false, false);
        caption.setCharacterHeight(20);
        caption.setColor(v4Color);
        addElement(caption);
    
        pixelSize = vec2.div(new vec2(1.0f), new vec2(size));
    
        pVertexArrayObject = new VertexArrayObject();
        pVertexBuffer = new VertexBufferObject();
        pVertexBuffer.setData(vPositionsData, GL30.GL_DYNAMIC_DRAW);
        pVertexArrayObject.addAttribute(pVertexBuffer, 0, 3, GL11.GL_FLOAT, 0, 0);
    
        initGraphShader();

        resize();
        reposition();
    }
    
    public void cleanup() { }
    
    public void render()
    {
        renderchildren();
    
        GraphShader.use();
        GraphShader.loadUniform("color", v4Color);
        
        GL11.glViewport(vActualPos.x, Window.CurrentlyBoundWindow.getSize().y - background.getSize().y - vActualPos.y, background.getSize().x, background.getSize().y);
        pVertexArrayObject.bind();
        pVertexBuffer.setData(vPositionsData, GL30.GL_DYNAMIC_DRAW);
        
        GL11.glDrawArrays(GL11.GL_LINE_STRIP, 0, vData.size());
        pVertexArrayObject.unbind();
        GL11.glViewport(0,0, Window.CurrentlyBoundWindow.getSize().x, Window.CurrentlyBoundWindow.getSize().y);
        GraphShader.unuse();
    }

    private vec3 getPosValue(int index)
    {
        return new vec3(vPositionsData.get(index * 3 + 0), 
                        vPositionsData.get(index * 3 + 1), 
                        vPositionsData.get(index * 3 + 2));
    }

    public void remPosData(int index)
    {
        vData.remove(index);
        vPositionsData.remove(index * 3 + 0);
        vPositionsData.remove(index * 3 + 1);
        vPositionsData.remove(index * 3 + 2);
    }

    public void setPosDataX(int index, float f)
    {
        vPositionsData.set(index * 3 + 0, f);
    }

    public void setPosDataY(int index, float f)
    {
        vPositionsData.set(index * 3 + 1, f);
    }

    protected void updateOnColorChange()
    {
        dataText.setColor(v4Color);
        caption.setColor(v4Color);
    }

    public void addData(float data)
    {
        if(Float.isInfinite(data) || Float.isNaN(data))
            return;
        vData.add(data);
        vPositionsData.add(1.0f);
        vPositionsData.add(0.0f);
        vPositionsData.add(0.0f);
        if(data > fBiggestNum)
        {
            fBiggestNum = data;
            calculateScale();
        }
        else if(data < fSmallestNum)
        {
            fSmallestNum = data;
            calculateScale();
        }

        ///Debug.info(data + " " + fBiggestNum + " " + fSmallestNum);


        for(int i = 0; i < vData.size(); i++)
        {
            setPosDataY(i, vData.get(i) * fScale);
            vPositionsData.set(i * 3, vPositionsData.get(i * 3) - pixelSize.x * fSpeed);
            //if(getPosValue(i).x < -1)
            //    remPosData(i);
        }
    
		dataText.setString(String.format("%."+precision+"f", vData.get(vData.size()-1)) + ValueType);
        dataText.setPosition(new ivec2(background.getSize().x - dataText.getSize().x - 15, 5));
    }

    public void calculateScale()
    {
        float span = fBiggestNum - fSmallestNum;
        fScale = 1 / span;
        fScale *= 2.0f;

        //Debug.info(fScale);
    }
    
    public void setPrecision(int precision)    { this.precision = precision; }
    public void setValueType(String type) { this.ValueType = type; }
};