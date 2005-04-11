/* $Id$ */
/*
 * Copyright  2000-2004 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package org.firebirdsql.isql.isqlBase;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import junit.framework.AssertionFailedError;
import junit.framework.Test;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.optional.junit.FormatterElement;
import org.apache.tools.ant.taskdefs.optional.junit.JUnitResultFormatter;
import org.apache.tools.ant.taskdefs.optional.junit.JUnitTest;
import org.apache.tools.ant.taskdefs.optional.junit.JUnitTestRunner;
import org.apache.tools.ant.taskdefs.optional.junit.JUnitVersionHelper;
import org.apache.tools.ant.util.DOMElementWriter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

/**
 * Prints XML output of the test to a specified Writer.
 * 
 * 
 * @see FormatterElement
 */

public class XMLJUnitResultFormatter
		implements
			JUnitResultFormatter,
			XMLConstants {

	private static DocumentBuilder getDocumentBuilder() {
		try {
			return DocumentBuilderFactory.newInstance().newDocumentBuilder();
		} catch (Exception exc) {
			throw new ExceptionInInitializerError(exc);
		}
	}

	/**
	 * The XML document.
	 */
	private Document doc;
	/**
	 * The wrapper for the whole testsuite.
	 */
	private Element rootElement;
	/**
	 * Element for the current test.
	 */
	private Hashtable testElements = new Hashtable();
	/**
	 * tests that failed.
	 */
	private Hashtable failedTests = new Hashtable();
	/**
	 * Timing helper.
	 */
	private Hashtable testStarts = new Hashtable();
	/**
	 * Where to write the log to.
	 */
	private OutputStream out;

	public XMLJUnitResultFormatter() {
	}

	public void setOutput(OutputStream out) {
		this.out = out;
	}

	public void setSystemOutput(String out) {
		formatOutput(SYSTEM_OUT, out);
	}

	public void setSystemError(String out) {
		formatOutput(SYSTEM_ERR, out);
	}

	/**
	 * The whole testsuite started.
	 */
	public void startTestSuite(JUnitTest suite) {
		doc = getDocumentBuilder().newDocument();
		rootElement = doc.createElement(TESTSUITE);
		rootElement.setAttribute(ATTR_NAME, suite.getName());

		// Output properties
		Element propsElement = doc.createElement(PROPERTIES);
		rootElement.appendChild(propsElement);
		Properties props = suite.getProperties();
		if (props != null) {
			Enumeration e = props.propertyNames();
			while (e.hasMoreElements()) {
				String name = (String) e.nextElement();
				Element propElement = doc.createElement(PROPERTY);
				propElement.setAttribute(ATTR_NAME, name);
				propElement.setAttribute(ATTR_VALUE, props.getProperty(name));
				propsElement.appendChild(propElement);
			}
		}
	}

	/**
	 * The whole testsuite ended.
	 */
	public void endTestSuite(JUnitTest suite) throws BuildException {
		rootElement.setAttribute(ATTR_TESTS, "" + suite.runCount());
		rootElement.setAttribute(ATTR_FAILURES, "" + suite.failureCount());
		rootElement.setAttribute(ATTR_ERRORS, "" + suite.errorCount());
		rootElement.setAttribute(ATTR_TIME, "" + (suite.getRunTime() / 1000.0));
		if (out != null) {
			Writer wri = null;
			try {
				wri = new BufferedWriter(new OutputStreamWriter(out, "UTF8"));
				wri.write("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
				(new DOMElementWriter()).write(rootElement, wri, 0, "  ");
				wri.flush();
			} catch (IOException exc) {
				throw new BuildException("Unable to write log file", exc);
			} finally {
				if (out != System.out && out != System.err) {
					if (wri != null) {
						try {
							wri.close();
						} catch (IOException e) {
							// ignore
						}
					}
				}
			}
		}
	}

	/**
	 * Interface TestListener.
	 * 
	 * <p>
	 * A new Test is started.
	 */
	public void startTest(Test t) {
		testStarts.put(t, new Long(System.currentTimeMillis()));
	}

	/**
	 * Interface TestListener.
	 * 
	 * <p>
	 * A Test is finished.
	 */
	public void endTest(Test test) {
		// Fix for bug #5637 - if a junit.extensions.TestSetup is
		// used and throws an exception during setUp then startTest
		// would never have been called
		if (!testStarts.containsKey(test)) {
			startTest(test);
		}

		Element currentTest = null;
		if (!failedTests.containsKey(test)) {
			currentTest = doc.createElement(TESTCASE);
			currentTest.setAttribute(ATTR_NAME, JUnitVersionHelper
					.getTestCaseName(test));
			// a TestSuite can contain Tests from multiple classes,
			// even tests with the same name - disambiguate them.
			currentTest.setAttribute(ATTR_CLASSNAME, test.getClass().getName());
			rootElement.appendChild(currentTest);
			testElements.put(test, currentTest);
		} else {
			currentTest = (Element) testElements.get(test);
		}

		Long l = (Long) testStarts.get(test);
		currentTest.setAttribute(ATTR_TIME, ""
				+ ((System.currentTimeMillis() - l.longValue()) / 1000.0));
	}

	/**
	 * Interface TestListener for JUnit &lt;= 3.4.
	 * 
	 * <p>
	 * A Test failed.
	 */
	public void addFailure(Test test, Throwable t) {
		formatError(FAILURE, test, t);
	}

	/**
	 * Interface TestListener for JUnit &gt; 3.4.
	 * 
	 * <p>
	 * A Test failed.
	 */
	public void addFailure(Test test, AssertionFailedError t) {
		addFailure(test, (Throwable) t);
	}

	/**
	 * Interface TestListener.
	 * 
	 * <p>
	 * An error occurred while running the test.
	 */
	public void addError(Test test, Throwable t) {
		formatError(ERROR, test, t);
	}

	private void formatError(String type, Test test, Throwable t) {
		boolean skipTrace = false;
		final String SEPARATOR = System.getProperty("line.separator");
		if (test != null) {
			endTest(test);
			failedTests.put(test, test);
		}

		Element nested = doc.createElement(type);
		Element currentTest = null;
		if (test != null) {
			currentTest = (Element) testElements.get(test);
		} else {
			currentTest = rootElement;
		}

		currentTest.appendChild(nested);

		String message = t.getMessage();
		String diffFileName = null;
		if (message != null && message.length() > 0) {
			// hack the JUnit message for special ISQL cases!
			if (message.startsWith("ISQL output does not match")) {
				skipTrace = true;
				diffFileName = message.substring((message.indexOf(":") + 2),
						(message.indexOf(" expected")));
				// message = new String("ISQL output does not match benchmark
				// run.");
			} else if (message.startsWith("ISQL exited abnormally")) {
				skipTrace = true;
				// message = new String(message.substring(0,
				// message.indexOf("expected:")));
			}
			nested.setAttribute(ATTR_MESSAGE, message);
		}
		nested.setAttribute(ATTR_TYPE, t.getClass().getName());

		String strace = JUnitTestRunner.getFilteredTrace(t);
		if (skipTrace) {
			strace = new String(strace.replaceAll("at .*\\.invoke", ""));
			strace = new String(strace.replaceAll("junit\\.framework.*was",""));
			// strace = new
			// String(strace.replaceAll("junit.framework.AssertionFailedError.*<1>",""));
			// if we have a special diff file to display, show it now.
			if (diffFileName != null)
				try {
					BufferedReader is = new BufferedReader(new FileReader(
							diffFileName));
					String s = null;
					while ((s = is.readLine()) != null) {
						strace = strace + s;
						strace = strace + "\n";
					}
					is.close();
				} catch (IOException ioe) {
					strace = "JUnit formatter error - could not open isql ouptut file \""
							+ diffFileName
							+ "\". Perhaps no ouptut file was created?";
				}
			//			strace.replaceAll("\tat.*\\)", "");
			skipTrace = false;
		} // remove traceback

		Text trace = doc.createTextNode(strace);
		//		try {
		//			PrintWriter pw = new PrintWriter(
		//					new FileWriter("/foo.txt", true));
		//			pw.println("trace:" + trace);
		//			pw.close();
		//		} catch (IOException ioe) {
		//		}
		nested.appendChild(trace);
	}
	private void formatOutput(String type, String output) {
		Element nested = doc.createElement(type);
		rootElement.appendChild(nested);
		nested.appendChild(doc.createCDATASection(output));
	}

} // XMLJUnitResultFormatter
