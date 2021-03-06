/**
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

package org.omg.bpmn.miwg.devel;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.omg.bpmn.miwg.api.AnalysisJob;
import org.omg.bpmn.miwg.api.AnalysisRun;
import org.omg.bpmn.miwg.api.ReferenceNotFoundException;
import org.omg.bpmn.miwg.mvn.AnalysisFacade;
import org.omg.bpmn.miwg.scan.BpmnFileScanner;
import org.omg.bpmn.miwg.scan.StandardScanParameters;
import org.omg.bpmn.miwg.schema.SchemaAnalysisTool;
import org.omg.bpmn.miwg.xpath.XpathAnalysisTool;

@RunWith(Parameterized.class)
public class AllTestResults_NoExceptions extends AbstractTest {

	public AllTestResults_NoExceptions(AnalysisJob job) {
		super(job);
		job.overrideReportFolder(null);
	}

	@Parameters
	public static List<Object[]> data() throws IOException,
			ReferenceNotFoundException {
		return BpmnFileScanner.data(new StandardScanParameters(null, null));
	}

	@Test
	@Override
	public void testSchema() throws Exception {
		try {
			job.setSchemaOnly();

			AnalysisRun run = AnalysisFacade.executeAnalysisJob(job);

			run.getResult(SchemaAnalysisTool.class);
		} catch (Exception e) {
			fail("Exception occured: " + e);
			e.printStackTrace();
		}

	}

	@Test
	@Override
	public void testXpath() throws Exception {
		try {
			job.setXpathOnly();

			AnalysisRun run = AnalysisFacade.executeAnalysisJob(job);

			run.getResult(XpathAnalysisTool.class);
		} catch (Exception e) {
			fail("Exception occured: " + e);
			e.printStackTrace();
		}
	}
}
