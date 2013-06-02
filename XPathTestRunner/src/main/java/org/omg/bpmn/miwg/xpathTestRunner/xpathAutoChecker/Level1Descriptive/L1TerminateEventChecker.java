package org.omg.bpmn.miwg.xpathTestRunner.xpathAutoChecker.Level1Descriptive;

import org.omg.bpmn.miwg.xpathTestRunner.testBase.AbstractXpathTest;
import org.omg.bpmn.miwg.xpathTestRunner.xpathAutoChecker.base.XpathAutoChecker;
import org.w3c.dom.Node;

public class L1TerminateEventChecker implements XpathAutoChecker {

	@Override
	public void check(Node n, AbstractXpathTest test) throws Throwable {
		test.navigateElementX("bpmn:terminateEventDefinition | bpmn:eventDefinitionRef");
	}

	@Override
	public boolean isApplicable(Node n, String param) {
		return param != null && (param.equals("terminateEvent"));
	}

}
