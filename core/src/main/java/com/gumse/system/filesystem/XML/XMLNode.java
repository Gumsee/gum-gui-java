package com.gumse.system.filesystem.XML;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.gumse.maths.ivec2;
import com.gumse.tools.Toolbox;

public class XMLNode
{
    public interface AttributeCallback 
    {
        void run(String name, String value);
    }

    public static class NODE_TYPES
    {
        public static final int UNKNOWN   = 0b0000001;
        public static final int ELEMENT   = 0b0000010;
        public static final int TEXT      = 0b0000100;
        public static final int COMMENT   = 0b0001000;
        public static final int ATTRIBUTE = 0b0010000;
        public static final int DOCUMENT  = 0b0100000;
        public static final int ENTITY    = 0b1000000;
        public static final int ALL       = 0b1111111;
    };

    public String name = "";
    public String content = "";
    public int type;

    public XMLNode parent;
    public Object userptr;
    public ArrayList<XMLNode> children;
    public Map<String, String> mAttributes;

    public XMLNode() 
    {
        children = new ArrayList<>();
        mAttributes = new HashMap<>();
    }
    public XMLNode(String name, int type)
    {
        this();

        this.name = name;
        this.type = type;
    }

    public void cleanup()
    {
        for(XMLNode child : children)
        {
            if(child != null)
                child.cleanup();
        }
    }

    public void addAttribute(String attrname, String value)
    {
        this.mAttributes.put(attrname, value);
    }

    public boolean hasAttribute(String attr)
    {
        return this.mAttributes.containsKey(attr);
    }

    public String getAttribute(String attr)
    {
        String ret = this.mAttributes.get(attr);
        if(ret == null)
            return "";

        return ret;
    }

    public int getIntAttribute(String attr, int def)
    {
        if(hasAttribute(attr))
            return Toolbox.StringToInt(Toolbox.strExtractNumbers(getAttribute(attr)));
        return def;
    }

    public float getFloatAttribute(String attr, float def)
    {
        if(hasAttribute(attr))
            return Toolbox.StringToFloat(Toolbox.strExtractNumbers(getAttribute(attr)));
        return def;
    }

    public double getDoubleAttribute(String attr, double def)
    {
        if(hasAttribute(attr))
            return Toolbox.StringToDouble(Toolbox.strExtractNumbers(getAttribute(attr)));
        return def;
    }

    public ivec2 getIvec2Attribute(String attr, ivec2 def)
    {
        if(hasAttribute(attr))
            return new ivec2(Toolbox.StringToVec2(getAttribute(attr)));
        return def;
    }

    public void addChild(XMLNode child)
    {
        children.add(child);
    }

    void retrieveAttributes(AttributeCallback func)
    {
        for(Map.Entry<String, String> attr : mAttributes.entrySet())
            func.run(attr.getKey(), attr.getValue());
    }
};