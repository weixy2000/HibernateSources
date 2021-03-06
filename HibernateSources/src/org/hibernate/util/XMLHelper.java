/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * Copyright (c) 2008, Red Hat Middleware LLC or third-party contributors as
 * indicated by the @author tags or express copyright attribution
 * statements applied by the authors.  All third-party contributions are
 * distributed under license by Red Hat Middleware LLC.
 *
 * This copyrighted material is made available to anyone wishing to use, modify,
 * copy, or redistribute it subject to the terms and conditions of the GNU
 * Lesser General Public License, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this distribution; if not, write to:
 * Free Software Foundation, Inc.
 * 51 Franklin Street, Fifth Floor
 * Boston, MA  02110-1301  USA
 *
 */
package org.hibernate.util;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.dom4j.io.DOMReader;
import org.dom4j.io.SAXReader;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.dom4j.Element;
import org.dom4j.DocumentFactory;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;

/**
 * 小型辅助类，懒加载DOM和SAX读取器，之后可保证快速使用它们。
 */
public final class XMLHelper {

	private static final Logger log = LoggerFactory.getLogger(XMLHelper.class);
	public static final EntityResolver DEFAULT_DTD_RESOLVER = new DTDEntityResolver();

	private DOMReader domReader;
	private SAXReader saxReader;

	/**
	 * 创建一个dom4j的SAXReader同时将所有校验错误信息保存到errorList中
	 */
	public SAXReader createSAXReader(String file, List errorsList, EntityResolver entityResolver) {
		if (saxReader==null) saxReader = new SAXReader();
		saxReader.setEntityResolver(entityResolver);//实体解析器
		saxReader.setErrorHandler( new ErrorLogger(file, errorsList) );//错误监听器
		saxReader.setMergeAdjacentText(true);//合并相邻的文本为true
		saxReader.setValidation(true);//校验true
		return saxReader;
	}

	/**
	 * 创建一个dom4j的DOMReader
	 */
	public DOMReader createDOMReader() {
		if (domReader==null) domReader = new DOMReader();
		return domReader;
	}

	/**
	 *　描述： 错误日志，监听错误信息
	 */
	public static class ErrorLogger implements ErrorHandler {
		private String file;
		private List errors;
		ErrorLogger(String file, List errors) {
			this.file=file;
			this.errors = errors;
		}
		public void error(SAXParseException error) {
			log.error( "Error parsing XML: " + file + '(' + error.getLineNumber() + ") " + error.getMessage() );
			errors.add(error);
		}
		public void fatalError(SAXParseException error) {
			error(error);
		}
		public void warning(SAXParseException warn) {
			log.warn( "Warning parsing XML: " + file + '(' + warn.getLineNumber() + ") " + warn.getMessage() );
		}
	}

	/**
	 * 创建一个结点
	 * @param 结点名称
	 * @return
	 */
	public static Element generateDom4jElement(String elementName) {
		return DocumentFactory.getInstance().createElement( elementName );
	}

	public static void dump(Element element) {
		try {
			// try to "pretty print" it
			OutputFormat outformat = OutputFormat.createPrettyPrint();
			XMLWriter writer = new XMLWriter( System.out, outformat );
			writer.write( element );
			writer.flush();
			System.out.println( "" );
		}
		catch( Throwable t ) {
			// otherwise, just dump it
			System.out.println( element.asXML() );
		}

	}
}
