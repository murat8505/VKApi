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

public class VKAudioSearchResponse extends DefaultHandler{
	public static final String AUDIO = "audio";
	public static final String COUNT = "count";
	
	private int mTotalCount;
	private List<VKAudioItem> items = new ArrayList<VKAudioItem>();
	private String thisElement;
	private VKAudioItem item;

	public static VKAudioSearchResponse fromXml(String xml) {
		VKAudioSearchResponse response = new VKAudioSearchResponse(); 
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
	  if (qName.equals(AUDIO)) {
      	items.add(item);
      }
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		
		if (thisElement.equals(COUNT)) {
        	mTotalCount = Integer.parseInt(new String(ch, start, length));
        	if (mTotalCount == 0) {
        		return;
        	}
        } else if (thisElement.equals(AUDIO)) {
        	item = new VKAudioItem();
        } else if (thisElement.equals(VKAudioItem.AID)) {
        	item.aid = new String(ch, start, length);
        } else if (thisElement.equals(VKAudioItem.OID)) {
        	item.owner_id = new String(ch, start, length);
        } else if (thisElement.equals(VKAudioItem.ARTIST)) {
        	item.artist = new String(ch, start, length);
        } else if (thisElement.equals(VKAudioItem.TITLE)) {
        	item.title = new String(ch, start, length);
        } else if (thisElement.equals(VKAudioItem.DURATION)) {
        	item.duration = Integer.parseInt(new String(ch, start, length));
        } else if (thisElement.equals(VKAudioItem.URL)) {
        	item.url = new String(ch, start, length);
        } else if (thisElement.equals(VKAudioItem.LYRICS_ID)) {
        	item.lyrics_id = new String(ch, start, length);
        }
	}
	
	public List<VKAudioItem> getItems() {
		return items;
	}
	
	public int getCount() {
		return mTotalCount;
	}
}
