package com.iradetskiy.vkapi;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public class VKUsersGetResponse extends DefaultHandler {
	
	public final static String USER = "user";
	
	private String thisElement;
	private VKUserItem item;
	private List<VKUserItem> items = new ArrayList<VKUserItem>();
	
	public static VKUsersGetResponse fromXml(String xml) {
		VKUsersGetResponse response = new VKUsersGetResponse(); 
		try {
			SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
			parser.parse(new ByteArrayInputStream(xml.getBytes()), response);
		} catch (SAXException | IOException | ParserConfigurationException e) {
			e.printStackTrace();
		}
		return response;
	}
	
	@Override
	public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
	  thisElement = qName; 
	}

	@Override
	public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
	  thisElement = "";
	  if (qName.equals(USER)) {
      	items.add(item);
      }
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		if (thisElement.equals(USER)) {
        	item = new VKUserItem();
        }
		
        if (thisElement.equals(VKGetUsersRequest.FIELD_FIRST_NAME)) {
        	item.first_name = new String(ch, start, length);
        }
        
        if (thisElement.equals(VKGetUsersRequest.FIELD_LAST_NAME)) {
        	item.last_name = new String(ch, start, length);
        }
        
        if (thisElement.equals(VKGetUsersRequest.FIELD_UID)) {
        	item.uid = new String(ch, start, length);
        }
	}
	
	public List<VKUserItem> getResults() {
		return items;
	}
}
