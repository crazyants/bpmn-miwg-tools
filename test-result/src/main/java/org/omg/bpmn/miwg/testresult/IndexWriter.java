/*
 * The MIT License (MIT)
 * Copyright (c) 2013 OMG BPMN Model Interchange Working Group
 *
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * 
 */
package org.omg.bpmn.miwg.testresult;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

/**
 * Simple wrapper to write an index of the test results created.
 * 
 * @author Tim Stephenson
 */
public class IndexWriter {

    public static void writeXml(String rptName, File idx, List<File> files) {
        PrintWriter out = null;
        try {
            out = new PrintWriter(idx);
            out.println("<menu name=\"" + rptName + "\">");
            for (File f : files) {
                out.print("\t<item name=\"");
                out.print(f.getName());
                out.print("\" href=\"");
                out.print(f.getName());
                out.println("\"/>");
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            out.close();
        }
    }

    /***
     * This function is deprecated and only present until it is ensured that the
     * new method works.
     */
    @Deprecated
    public static void write(String rptName, File idx, List<File> files) {
        PrintWriter out = null;
        try {
            out = new PrintWriter(idx);
            out.println("<!DOCTYPE html><html><head>");
            out.println("\t<link rel=\"stylesheet\" href=\"/css/bootstrap.css\">");
            out.println("\t<link rel=\"stylesheet\" href=\"/css/bpmn-miwg.css\">");
            out.println("</head><body>");
            out.println("\t<div class=\"navbar\"><div class=\"navbar-inner\"><a class=\"brand\" href=\"/\">BPMN-MIWG</a><ul class=\"nav\">");
            out.println("\t\t<li><a href=\"/\">Home</a></li>");
            out.println("\t\t<li><a href=\"/xml-compare/overview.html\">XML Compare</a></li>");
            out.println("\t\t<li><a href=\"/xpath/overview.html\">XPath</a></li>");
            out.println("\t</ul>\t</div></div>");

            out.println("<div class=\"testresults\">");
            for (File f : files) {
                out.print("\t<div class=\"test\">");
                out.print("<a href=\"");
                out.print(f.getName());
                out.print("\">");
                out.print(f.getName());
                out.print("</a>");
                out.println("</div>");
            }
            out.println("</div>");

            out.println("\t<script src=\"http://ajax.googleapis.com/ajax/libs/jquery/1.10.1/jquery.min.js\"><!-- required --></script>");
            out.println("\t<script src=\"/js/bpmn-miwg.js\"><!-- required --></script>");
            out.println("</body></html>");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            out.close();
        }
    }

    /***
     * Writes the test results to an HTML document.
     * 
     * @param rptName
     *            Report name
     * @param idx
     *            Output file
     * @param collection
     *            A list of FileResult instances which build the HTML fragments
     *            for each file using the buildHtml method.
     */
    public static void write2(String rptName, File idx,
            Collection<FileResult> collection) {
        PrintWriter out = null;
        InputStream templateStream = null;
        Scanner scanner = new Scanner(templateStream, "UTF-8");
        try {
            templateStream = IndexWriter.class
                    .getResourceAsStream("/resultspage.html");
            String template = scanner
                    .useDelimiter("\\A").next();
            // individual results are in sub-dir so need to fix links
            template = template.replace("../css", "css").replace("../js", "js");
            out = new PrintWriter(idx);

            StringBuilder sb = new StringBuilder();
            sb.append("<div class=\"testresults\">");
            for (FileResult result : collection) {
                sb.append(result.buildHtml());
            }
            sb.append("</div>");

            out.println(template.replace("{TESTRESULTS}", sb.toString()));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            out.close();
            try {
                templateStream.close();
            } catch (IOException e) {
                ;
            }
            scanner.close();
        }
    }
}