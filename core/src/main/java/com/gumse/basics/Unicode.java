// package com.gumse.basics;

// import java.util.ArrayList;

// import com.gumse.tools.Debug;

// public class Unicode
// {
//     private ArrayList<String> vUTF8Chars;
//     public Unicode() 
//     { 
//         vUTF8Chars = new ArrayList<String>();
//     }

//     public Unicode(ArrayList<String> vec) 
//     { 
//         this.vUTF8Chars = vec; 
//     }

//     public Unicode(String utf8)
//     {
//         vUTF8Chars = new ArrayList<String>();
//         int numBytes = 0;
//         for(int i = 0; i < utf8.length();)
//         {
//             char c = utf8.charAt(i);
//             if      ((c & 0x80) == 0x00) numBytes = 1; // 1 Octet  (ASCII)
//             else if ((c & 0xE0) == 0xC0) numBytes = 2; // 2 Octet
//             else if ((c & 0xF0) == 0xE0) numBytes = 3; // 3 Octet
//             else if ((c & 0xF8) == 0xF0) numBytes = 4; // 4 Octet
//             else    Debug.error("UTF-8 Unknown first byte " +  c);

//             System.out.println(utf8.substring(i, i + numBytes));
//             vUTF8Chars.add(utf8.substring(i, i + numBytes));

//             i += numBytes;
//         }
//     }

//     /*public Unicode(std::u32string str)
//     {
//         for(int i = 0; i < str.length(); i++)
//         {
//             vUTF8Chars.add(converter.to_bytes(str[i]));
//         }
//     }*/


//     public void set(int index, String str)
//     {
//         vUTF8Chars.set(index, str);
//     }
//     public String get(int index)
//     {
//         return vUTF8Chars.get(index);
//     }

//     public int getCodepoint(int index)
//     {
//         System.out.println( "\\u" + Integer.toHexString('÷' | 0x10000).substring(1) );
//         return 0;
//     }

//     public String toString()
//     {
//         String ret = "";
//         for(String s : vUTF8Chars)
//             ret += s;
            
//         return ret;
//     }

//     public int length()
//     {
//         return vUTF8Chars.size();
//     }

//     public Unicode substr(int start, int end)
//     {
//         return new Unicode((ArrayList<String>)vUTF8Chars.subList(start, end));
//     }

//     public void append(Unicode unicode)
//     {
//         for(int i = 0; i < unicode.length(); i++)
//         {
//             vUTF8Chars.add(unicode.get(i));
//         }
//     }

//     public void insert(Unicode unicode, int index)
//     {
//         //vUTF8Chars.insert(vUTF8Chars.begin() + index, unicode.begin(), unicode.end());
//     }

//     public void erase(int index)
//     {
//         vUTF8Chars.remove(index);
//     }
// }