/*
Copyright (c) 2011 Omar Hussain

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

The Software shall be used for Good, not Evil.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/
package com.badpx.particleandroid.utils;



import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Stack;

import org.xmlpull.v1.XmlPullParser;


//import android.util.Log;
import android.util.Xml;

/**
* This class will parse a plist xml file and store the contents in a 
* hierarchical structure
* 
* @author Omar
*
* Code reused from akoscz's PList Parser
*/
public class PListParser {
	
	public class PListInvalidException extends Exception{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		public PListInvalidException(String err){
			super(err);
		}
		
	}
	
	public class PListConstants {
		public static final java.lang.String TAG_PLIST = "plist";
		public static final java.lang.String TAG_DICT = "dict";
		public static final java.lang.String TAG_PLIST_ARRAY = "array";
		public static final java.lang.String TAG_KEY = "key";
		public static final java.lang.String TAG_INTEGER = "integer";
		public static final java.lang.String TAG_STRING = "string";
		public static final java.lang.String TAG_REAL = "real";
		public static final java.lang.String TAG_DATE = "date";
		public static final java.lang.String TAG_BOOL_TRUE = "true";
		public static final java.lang.String TAG_BOOL_FALSE = "false";
		public static final java.lang.String TAG_DATA = "data";
	}

	
   public Boolean isValidPList = false;
   Stack<Object> stack = null;
   /**
    * 
    */
	public Object root = null;
   
   /**
    * Constructor.
    * Parse a plist file from the given InputStream.
    * 
    * @param inputStream The InputStream which has the bytes of the plist file we need to parse.
    */
   public PListParser(InputStream inputStream) {
      parse(inputStream);
   }

 
   /**
    * Utility method which uses a XmlPullParser to iterate through the XML elements
    * and build up a hierarchical structure representing the 
    * plist configuration file.
    * 
    * @param inputStream The InputStream which contains the plist XML file.
    */
   public void parse(InputStream inputStream) {
      XmlPullParser parser = Xml.newPullParser();
      
      try {
         parser.setInput(inputStream, null);
         
         int eventType = parser.getEventType();
         
         boolean done = false;
         String name = null;
         String key = null;
         
         stack = new Stack<Object>();
         while (eventType != XmlPullParser.END_DOCUMENT && !done) {
            switch(eventType) {
               case XmlPullParser.START_DOCUMENT:
                  //Log.d(TAG, "START_DOCUMENT");
                  break;
               case XmlPullParser.START_TAG:
                  name = parser.getName();
                  if(name.equalsIgnoreCase(PListConstants.TAG_PLIST)){
                	  isValidPList = true;
                  }else{
                	  if(!isValidPList){
                		  throw new PListInvalidException("File not valid PList.");
                	  }else{
                		  
                		  
                		  if(name.equalsIgnoreCase(PListConstants.TAG_DICT)) {
                				HashMap<String,Object> tmpDict = new HashMap<String,Object>();
                				this.pushObjectToTopContainer(key, tmpDict);
              				  	stack.push(tmpDict);
                			}else if(name.equalsIgnoreCase(PListConstants.TAG_PLIST_ARRAY)) {
                				ArrayList<Object> tmpArr = new ArrayList<Object>();
                				this.pushObjectToTopContainer(key, tmpArr);
              				  	stack.push(tmpArr);
                		  	}else if(name.equalsIgnoreCase(PListConstants.TAG_KEY)) {
                		  		key = parser.nextText();
                		  	}else if(name.equalsIgnoreCase(PListConstants.TAG_INTEGER)) {
                		  		this.pushObjectToTopContainer(key, new Integer(parser.nextText()));
                		  	}else if(name.equalsIgnoreCase(PListConstants.TAG_STRING)) {
                		  		this.pushObjectToTopContainer(key, parser.nextText());
                		  	}else if(name.equalsIgnoreCase(PListConstants.TAG_BOOL_FALSE)) {
                		  		this.pushObjectToTopContainer(key, false);
                		  	}else if(name.equalsIgnoreCase(PListConstants.TAG_BOOL_TRUE)) {
                		  		this.pushObjectToTopContainer(key,true);
                		  	}else if(name.equalsIgnoreCase(PListConstants.TAG_DATA)){
                		  		byte[] data = parser.nextText().getBytes();
                		  		this.pushObjectToTopContainer(key, data);
                		  	}else if(name.equalsIgnoreCase(PListConstants.TAG_DATE)){
                		  		Date dt = new Date(Date.parse(parser.nextText()));
                		  		this.pushObjectToTopContainer(key, dt);
                		  	}else if(name.equalsIgnoreCase(PListConstants.TAG_REAL)){
                		  		Float val = new Float(Float.parseFloat(parser.nextText().trim()));
                		  		this.pushObjectToTopContainer(key, val);
                		  	}
            
                		  if(root == null){
                			  root = stack.peek();
                		  }
                	  }
                  }
                  
                  break;
               case XmlPullParser.END_TAG:
                  name = parser.getName();
   
                  if(name.equalsIgnoreCase(PListConstants.TAG_DICT)
                		  || name.equalsIgnoreCase(PListConstants.TAG_PLIST_ARRAY)) {
                     if(!stack.empty()) {
                        stack.pop();
                     }
                  } 
                  else if(name.equalsIgnoreCase(PListConstants.TAG_PLIST)) {
                     done = true;
                  }
                  break;
               case XmlPullParser.END_DOCUMENT:
                  //Log.d(TAG, "END_DOCUMENT");
                  break;
   
            }
            eventType = parser.next();
         }
      }catch (Exception ex){
         ex.printStackTrace();
      }      
   }

     @SuppressWarnings({ "unchecked" })
	private void pushObjectToTopContainer(String key, Object object){
    	 if(!stack.empty()){
    	 Object topContainer =stack.peek();
    	 if(topContainer != null){
    		 if(topContainer.getClass() == ArrayList.class){
    			  ArrayList<Object> arr = (ArrayList<Object>)topContainer;
    			  arr.add(object);
    		 }else if(topContainer.getClass() == HashMap.class){
    			 HashMap<String,Object> dict = (HashMap<String,Object>)topContainer;
    			 dict.put(key, object);
    		 }
    	 }
    	 }
     }

}
