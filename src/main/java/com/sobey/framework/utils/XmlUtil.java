/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sobey.framework.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

/**
 * 
 * @author WENLP
 */
public class XmlUtil {
	/**
	 * 将XML文件的字符串转换为Document对象
	 * 
	 * @param docStr
	 * @return
	 * @throws DocumentException
	 */
	public static Document getDocument(String docStr) throws DocumentException {
		Document document = null;
		document = DocumentHelper.parseText(docStr);
		return document;
	}

	/**
	 * 使用SAXReader读取XML文件并转换为Document对象
	 * 
	 * @param file
	 * @return
	 * @throws DocumentException
	 */
	public static Document getDocument(File file) throws DocumentException {
		SAXReader saxReader = new SAXReader();
		Document document = saxReader.read(file);
		return document;
	}

	/**
	 * 使用SAXReader读取XML文件并转换为Document对象
	 * 
	 * @param file
	 * @return
	 * @throws DocumentException
	 */
	public static Document getDocument(InputStream is) throws DocumentException {
		SAXReader saxReader = new SAXReader();
		Document document = saxReader.read(is);
		return document;
	}

	/**
	 * 将Document对象写入XML文件
	 * 
	 * @param file
	 * @param document
	 * @throws DocumentException
	 */
	public static void writeDocument(File file, Document document) throws DocumentException {
		XMLWriter output;
		try {
			OutputFormat format = new OutputFormat("    ", true);
			format.setEncoding("UTF-8");
			format.setNewlines(true);
			format.setLineSeparator("\n\r");

			output = new XMLWriter(new FileWriter(file), format);
			output.write(document);
			output.close();
		} catch (IOException ex) {
			Logger.getLogger(XmlUtil.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public Document generateDocument() {
		// 使用 DocumentHelper 类创建一个文档实例。 DocumentHelper 是生成 XML 文档节点的 dom4j API
		// 工厂类。
		Document document = DocumentHelper.createDocument();
		// 使用 addElement() 方法创建根元素 catalog 。 addElement() 用于向 XML 文档中增加元素。
		Element catalogElement = document.addElement("catalog");
		// 在 catalog 元素中使用 addComment() 方法添加注释“An XML catalog”。
		catalogElement.addComment("An XML Catalog");
		// 在 catalog 元素中使用 addProcessingInstruction() 方法增加一个处理指令。
		catalogElement.addProcessingInstruction("target", "text");
		// 在 catalog 元素中使用 addElement() 方法增加 journal 元素。
		Element journalElement = catalogElement.addElement("journal");
		// 使用 addAttribute() 方法向 journal 元素添加 title 和 publisher 属性。
		journalElement.addAttribute("title", "XML Zone");
		journalElement.addAttribute("publisher", "IBM developerWorks");
		// 向 article 元素中添加 journal 元素
		Element articleElement = journalElement.addElement("article");
		// 为 article 元素增加 level 和 date 属性。
		articleElement.addAttribute("level", "Intermediate");
		articleElement.addAttribute("date", "December-2001");
		// 向 article 元素中增加 title 元素。
		Element titleElement = articleElement.addElement("title");
		// 使用 setText() 方法设置 article 元素的文本。
		titleElement.setText("Java configuration with XML Schema");
		// 在 article 元素中增加 author 元素。
		Element authorElement = articleElement.addElement("author");
		// 在 author 元素中增加 firstname 元素并设置该元素的文本
		Element firstNameElement = authorElement.addElement("firstname");
		firstNameElement.setText("Marcello");
		// 在 author 元素中增加 lastname 元素并设置该元素的文本。
		Element lastNameElement = authorElement.addElement("lastname");
		lastNameElement.setText("Vitaletti");
		// 使用 addDocType() 方法添加文档类型说明。
		// 这样就向 XML 文档中增加文档类型说明：
		// <!DOCTYPE catalog SYSTEM "file://c:/Dtds/catalog.dtd">
		// document.addDocType("catalog",null,"file://c:/Dtds/catalog.dtd");
		// XMLWriter output;
		// try {
		// OutputFormat format = new OutputFormat("    ", true);
		// format.setEncoding("UTF-8");
		// format.setNewlines(true);
		// format.setLineSeparator("\n\r");
		//
		// output = new XMLWriter(new FileWriter(new File("D://catalog.xml")),
		// format);
		// output.write(document);
		// output.close();
		// } catch (IOException ex) {
		// Logger.getLogger(XmlUtil.class.getName()).log(Level.SEVERE, null,
		// ex);
		// }
		return document;
	}

	@SuppressWarnings("rawtypes")
	public Document modifyDocument(Document document) {
		try {
			// 使用 XPath 表达式从 article 元素中获得 level 节点列表。
			// 如果 level 属性值是“Intermediate”则改为“Introductory”。
			List list = document.selectNodes("//article/@level");
			Iterator iter = list.iterator();
			while (iter.hasNext()) {
				Attribute attribute = (Attribute) iter.next();
				if (attribute.getValue().equals("Intermediate")) {
					attribute.setValue("Introductory");
				}
			}

			list = document.selectNodes("//article/@date");
			iter = list.iterator();
			while (iter.hasNext()) {
				Attribute attribute = (Attribute) iter.next();
				if (attribute.getValue().equals("December-2001")) {
					attribute.setValue("October-2002");
				}
			}

			// 获取 article 元素列表，从 article 元素中的 title 元素得到一个迭代器，
			// 并修改 title 元素的文本。
			list = document.selectNodes("//article");
			iter = list.iterator();
			while (iter.hasNext()) {
				Element element = (Element) iter.next();
				Iterator iterator = element.elementIterator("title");
				while (iterator.hasNext()) {
					Element titleElement = (Element) iterator.next();
					if (titleElement.getText().equals("Java configuration with XMLSchema")) {
						titleElement.setText("Create flexible and extensible XML schema");
					}
				}
			}

			// 通过和 title 元素类似的过程修改 author 元素。
			list = document.selectNodes("//article/author");
			iter = list.iterator();
			while (iter.hasNext()) {
				Element element = (Element) iter.next();
				Iterator iterator = element.elementIterator("firstname");
				while (iterator.hasNext()) {
					Element firstNameElement = (Element) iterator.next();
					if (firstNameElement.getText().equals("Marcello")) {
						firstNameElement.setText("Ayesha");
					}
				}
			}
			list = document.selectNodes("//article/author");
			iter = list.iterator();
			while (iter.hasNext()) {
				Element element = (Element) iter.next();
				Iterator iterator = element.elementIterator("lastname");
				while (iterator.hasNext()) {
					Element lastNameElement = (Element) iterator.next();
					if (lastNameElement.getText().equals("Vitaletti")) {
						lastNameElement.setText("Malik");
					}
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return document;
	}

	public static void main(String[] argv) {
		XmlUtil xmlUtil = new XmlUtil();
		try {
			Document document = xmlUtil.generateDocument();
			writeDocument(new File("D:/catalog.xml"), document);
			document = getDocument(new File("D:/catalog.xml"));
			document = xmlUtil.modifyDocument(document);
			writeDocument(new File("D:/catalog-modified.xml"), document);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		// xmlUtil.test();
	}

	public void test() {
		StringBuilder sb_xml = new StringBuilder();
		sb_xml.append("<root>");
		sb_xml.append("  <cp>");
		sb_xml.append("    <cpguid>1001</cpguid>");
		sb_xml.append("  </cp>");
		sb_xml.append("  <upload>");
		sb_xml.append("    <content>");
		sb_xml.append("      <guid>fcca7c3aa2ba40349768847df0942ce0</guid>");
		sb_xml.append("      <createdate>2011-07-29 03:41:56</createdate>");
		sb_xml.append("      <ctype>live</ctype>");
		sb_xml.append("      <title>&#x76F4;&#x64AD;&#x4E0B;&#x53D1;&#x6D4B;&#x8BD5;29</title>");
		sb_xml.append("     <expiredate>2012-07-02 12:00:00</expiredate>");
		sb_xml.append("      <timeShift>1</timeShift>");
		sb_xml.append("     <timeShiftDate>1</timeShiftDate>");
		sb_xml.append("      <files>");
		sb_xml.append("        <file>");
		sb_xml.append("          <format>FLV</format>");
		sb_xml.append("          <name>http://172.18.3.43:8088/flv100</name>");
		sb_xml.append("          <bitrate>800K</bitrate>");
		sb_xml.append("         <codec>H264</codec>");
		sb_xml.append("          <server>HTTP</server>");
		sb_xml.append("          <p2p>0</p2p>");
		sb_xml.append("        </file>");
		sb_xml.append("      </files>");
		sb_xml.append("    </content>");
		sb_xml.append("  </upload>");
		sb_xml.append("</root>");

		Document doc;
		try {
			doc = getDocument(sb_xml.toString());
			Node node = doc.selectSingleNode("//upload/content/ctype");
			String ctype = "";
			if (node != null) {
				ctype = node.getText();
			}
			System.out.println("--->ctype:" + ctype);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}

}
