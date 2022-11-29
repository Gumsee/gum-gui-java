package com.gumse.system.filesystem.XML;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.gumse.tools.Debug;
import com.gumse.tools.Stack;

public class XMLReader
{
    public interface NodeCallback 
    {
        void run(XMLNode node, int depth);
    }

    private String sFileName;
    private String sEncoding;
    private XMLNode pRootNode;

    private ArrayList<XMLNode> vNodesList;

    void loadAttributes(XMLNode node, XMLStreamReader reader)
    {
        for(int i=0; i < reader.getAttributeCount(); i++)
        {
            String name = reader.getAttributeName(i).toString();
            String value= reader.getAttributeValue(i);

            node.addAttribute(name, value);
        }
    }

    /**
     *  This Constructor won't save any data and just call the given function recursively
     */
    public XMLReader(String filename, int allowedtypes, NodeCallback func)
    {
        sFileName = filename;
        vNodesList = new ArrayList<>();

        Stack<XMLNode> pNodeStack = new Stack<>();
        try (InputStream fis = XMLReader.class.getClassLoader().getResourceAsStream(filename)) 
        {
            XMLInputFactory xmlInFact = XMLInputFactory.newInstance();
            XMLStreamReader reader = null;
            reader = xmlInFact.createXMLStreamReader(fis);
            sEncoding = reader.getEncoding();

            while(reader.hasNext()) 
            {
                XMLNode retNode = new XMLNode();

                switch (reader.getEventType()) 
                { 
                    case XMLStreamReader.START_ELEMENT:      retNode.type = XMLNode.NODE_TYPES.ELEMENT;   break;
                    case XMLStreamReader.CHARACTERS:         retNode.type = XMLNode.NODE_TYPES.TEXT;      break;
                    case XMLStreamReader.COMMENT:            retNode.type = XMLNode.NODE_TYPES.COMMENT;   break;
                    case XMLStreamReader.ATTRIBUTE:          retNode.type = XMLNode.NODE_TYPES.ATTRIBUTE; break;
                    case XMLStreamReader.START_DOCUMENT:     retNode.type = XMLNode.NODE_TYPES.DOCUMENT;  break;
                    case XMLStreamReader.ENTITY_REFERENCE:
                    case XMLStreamReader.ENTITY_DECLARATION: retNode.type = XMLNode.NODE_TYPES.ENTITY;    break;
                    default:                                 retNode.type = XMLNode.NODE_TYPES.UNKNOWN;   break;
                }
                if(retNode.type == XMLNode.NODE_TYPES.ELEMENT)
                {
                    retNode.name = reader.getLocalName();
                    loadAttributes(retNode, reader);
                    pNodeStack.push(retNode);
                }
                else if(retNode.type == XMLNode.NODE_TYPES.DOCUMENT)
                {
                    retNode.name = "documentroot";
                    pRootNode = retNode;
                    pNodeStack.push(retNode);
                }
                else if(reader.getEventType() == XMLStreamReader.END_ELEMENT
                     || reader.getEventType() == XMLStreamReader.END_DOCUMENT)
                {
                    retNode = pNodeStack.pop();
                    //Debug.info("Last: " + retNode.name);
                    XMLNode parent = pNodeStack.getLast();
                    retNode.parent = parent;
                    if(parent != null)
                        parent.addChild(retNode);
                }
                else if(retNode.type == XMLNode.NODE_TYPES.TEXT)
                {
                    XMLNode currNode = pNodeStack.getLast();
                    currNode.content += reader.getText();

                    retNode = null;
                }
                else
                {

                }

                reader.next();
            }
        }
        catch(IOException e)        { Debug.error("XMLReader: " + e.getMessage()); return; }
        catch(XMLStreamException e) { Debug.error("XMLReader: " + e.getMessage()); return; }

        if(pRootNode == null)
            Debug.warn("Document " + filename + " doesnt have a document start");
        else
            pNodeStack.pop();
        
        if(pNodeStack.getLength() > 0)
            Debug.warn("Some XML Tags are still in the stack! (" + pNodeStack.getLength() + ")");


        runTree(pRootNode, allowedtypes, func, 0);
    }

    void runTree(XMLNode node, int allowedtypes, NodeCallback func, int depth)
    {
        if(node == null)
            return;

        addNode(node);

        if((allowedtypes & node.type) > 0)
            func.run(node, depth);
            
        for(XMLNode child : node.children)
        {
            runTree(child, allowedtypes, func, depth + 1);
        }
    }

    public void cleanup()
    {
        //Gum::_delete(pRootNode);
    }

    

    ArrayList<XMLNode> getNodeListByName(String name)
    {
        ArrayList<XMLNode> ret = new ArrayList<>();
        for(int i = 0; i < vNodesList.size(); i++)
        {
            if(vNodesList.get(i).name == name)
            {
                ret.add(vNodesList.get(i));
            }
        }
        return ret;
    }

    void addNode(XMLNode node) { this.vNodesList.add(node); }


    XMLNode getRootNode() { return pRootNode; }
    String getEncoding()  { return sEncoding; }
    String getFilename()  { return sFileName; }
};