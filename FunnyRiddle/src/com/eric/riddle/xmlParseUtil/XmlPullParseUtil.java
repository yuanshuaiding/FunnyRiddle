package com.eric.riddle.xmlParseUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

/**
 * ʹ��XmlPullParser�Ĳ������£� 1.����XmlPullParserFactoryʵ���� 2.ʹ�ù���ʵ������XmlPullParser����
 * 3.ʹ��XmlPullParser�������xml�ļ���������
 * 
 * @author dingys
 */
public class XmlPullParseUtil {
	public static List<Map<String, Object>> parseXmlFile(InputStream file) {
		Map<String, Object> item = null;
		List<Map<String, Object>> riddleList = new ArrayList<Map<String, Object>>();
		int eventType = -1;
		String preTagName = "";// ���ڼ�¼��ǰ�������ı�ǩ�ĸ���ǩ
		try {
			XmlPullParserFactory xmlPPF = XmlPullParserFactory.newInstance();
			// ����Ϊtrue����factory������XmlPullParser�ṩ��xml �����ռ��֧��
			xmlPPF.setNamespaceAware(true);
			XmlPullParser xpp = xmlPPF.newPullParser();
			xpp.setInput(new BufferedReader(new InputStreamReader(file)));
			while ((eventType = xpp.next()) != XmlPullParser.END_DOCUMENT) {
				// ֻ�н�������ʼ��ǩ��ʱ�����Ҫ��صĴ���
				if (eventType == XmlPullParser.START_TAG) {
					// ��¼����
					if (xpp.getName().equals("riddle")) {
						preTagName = "riddle";
						item = new HashMap<String, Object>();
					}

					// �����������ǰ��ǩ�ĸ���ǩΪnews�����item������Ը�ֵ
					if (preTagName.equals("riddle")) {
						if (xpp.getName().equals("content"))
							item.put("content", xpp.nextText().trim());
						if (xpp.getName().equals("answer")) {
							// ����������ͽ���Map<String,Object>���뼯����
							item.put("answer", xpp.nextText().trim());
							riddleList.add(item);
						}
					}
				}
			}
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return riddleList;
	}
}