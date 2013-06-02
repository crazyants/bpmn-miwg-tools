package org.omg.bpmn.miwg.xpathTestRunner.tests;

import java.io.File;

import org.omg.bpmn.miwg.xpathTestRunner.testBase.AbstractXpathTest;
import org.omg.bpmn.miwg.xpathTestRunner.testBase.ArtifactType;
import org.omg.bpmn.miwg.xpathTestRunner.testBase.Direction;
import org.w3c.dom.Node;

public class B_2_0_Test extends AbstractXpathTest {

	@Override
	public String getName() {
		return "B.2.0";
	}

	@Override
	public boolean isApplicable(File file) {
		String fn = file.getName();
		return // fn.equals("B.2.0-export.bpmn")||
		fn.equals("B.2.0-roundtrip.bpmn") || fn.equals("B.2.0.bpmn");
	}

	@Override
	public void execute(File file) throws Throwable {

		Node n;
		{
			loadFile(file);

			selectElement("//bpmn:collaboration");

			navigateElementX("bpmn:messageFlow[@name='Message Flow 1']",
					"messageFlowL2");
			navigateElementX("bpmn:messageFlow[@name='Message Flow 2']");

			{
				selectProcess("//bpmn:process[@id=//bpmn:participant[@name='Participant']/@processRef]");

				navigateElementX("bpmn:startEvent[@name='Start Event 1 Timer']");
				checkTimerEvent();

				navigateElementX("bpmn:task[@name='Abstract Task 1']");

				navigateElementX("bpmn:sendTask[@name='Send Task 2']");
				checkMessageFlow("Message Flow 1", Direction.Output);

				navigateElementX("bpmn:userTask[@name='User Task 3']");
				checkAssociation(ArtifactType.DataObject, "Data Object",
						Direction.Input);

				navigateElementX("bpmn:dataObjectReference[@name='Data Object']");

				{
					selectElement("bpmn:inclusiveGateway[@name='Inclusive Gateway 1']");
					navigateGatewaySequenceFlow("Conditional Sequence Flow");
					navigateGatewaySequenceFlow("Default Sequence Flow 1");
					checkDefaultSequenceFlow();
					pop();
				}
				navigateElementX("bpmn:serviceTask[@name='Service Task 4']");

				navigateElementX("bpmn:intermediateThrowEvent[@name='Intermediate Event Signal Throw 1']");
				checkSignalEvent();

				{
					selectElement("bpmn:subProcess[@name='Collapsed Sub-Process 1 Multi-Instances']");
					navigateElementX("bpmn:multiInstanceLoopCharacteristics[@isSequential='false']");
					pop();
				}

				navigateElementX("bpmn:task[@name='Task 5']");

				navigateBoundaryEvent("Boundary Intermediate Event Non-Interrupting Conditional");
				checkCancelActivity(false);
				checkParallelMultiple(false);
				checkConditionalEvent();

				navigateElementX("bpmn:parallelGateway[@name='Parallel Gateway 2']");
				navigateElementX("bpmn:endEvent[@name='End Event 1 Message']");
				checkMessageEvent();
				{
					selectElement("//bpmn:globalUserTask[@name='Call Activity calling a Global User Task']");
					navigateElementXByParam(
							"//bpmn:callActivity[@calledElement='%s' and @name='Call Activity calling a Global User Task']",
							"@id");
					pop();
				}

				{
					selectElement("bpmn:subProcess[@name='Expanded Sub-Process 1']");
					navigateElementX("bpmn:startEvent[@name='Start Event 2']");
					{
						selectElement("bpmn:userTask[@name='User Task 7 Standard Loop']");
						navigateElementX("bpmn:standardLoopCharacteristics");
						pop();
					}
					navigateElementX("bpmn:endEvent[@name='End Event 2']");
					pop();
				}

				navigateElementX("bpmn:userTask[@name='User Task 8']");
				navigateBoundaryEvent("");
				checkEscalationEvent();

				navigateFollowingElement("bpmn:task", "Task 9");

				navigateFollowingElement("bpmn:intermediateCatchEvent",
						"Intermediate Event Conditional Catch");
				checkConditionalEvent();

				navigateFollowingElement("bpmn:task", "Task 10");
				checkMessageFlow("Message Flow 2", Direction.Input);

				navigateFollowingElement("bpmn:endEvent", "End Event 3 Signal");
				checkSignalEvent();

				pop();
			}

			{
				selectProcess("//bpmn:process[@id=//bpmn:participant[@name='Pool']/@processRef]");

				navigateElementX("bpmn:laneSet/bpmn:lane[@name='Lane 1']");
				navigateElementX("bpmn:laneSet/bpmn:lane[@name='Lane 2']");

				navigateElement("bpmn:startEvent", "Start Event 2 Message");
				checkMessageEvent();
				checkMessageFlow("Message Flow 1", Direction.Input);

				navigateFollowingElement("bpmn:task", "Task 11");

				n = navigateFollowingElement("bpmn:eventBasedGateway",
						"Event Base Gateway 3");
				checkEventGatewayExclusive(true);

				navigateFollowingElement(n, "bpmn:intermediateCatchEvent",
						"Intermediate Event Timer Catch");

				navigateFollowingElement(n, "bpmn:intermediateCatchEvent",
						"Intermediate Event Message Catch");
				checkMessageEvent();

				navigateFollowingElement(n, "bpmn:receiveTask",
						"Receive Task 20");

				{
					// Flow following the intermediate timer event

					{
						selectElement("bpmn:callActivity",
								"Expanded Call Activity");
						selectCallActivityProcess();

						navigateElement("bpmn:startEvent", "Start Event 3");

						navigateElement("bpmn:startEvent",
								"Start Event 4 Conditional");

						navigateFollowingElement("bpmn:userTask",
								"User Task 12 Muti-Inst. Seq.");
						checkMultiInstanceSequential();

						n = navigateFollowingElement("bpmn:userTask",
								"User Task 13");
						navigateBoundaryEvent("Boundary Intermediate Event Interrupting Message");
						checkCancelActivity(true);
						checkMessageEvent();

						navigateFollowingElement("bpmn:endEvent",
								"End Event 5 Terminate");
						checkTerminateEvent();

						navigateFollowingElement(n, "bpmn:serviceTask",
								"Service Task 14");

						navigateFollowingElement("bpmn:endEvent", "End Event 4");

						pop();
						pop();
					}

					n = navigateFollowingElement("bpmn:serviceTask",
							"Service Task 15");
					navigateBoundaryEvent("Boundary Intermediate Event Interrupting Conditional");
					checkConditionalEvent();
					checkCancelActivity(true);

					navigateFollowingElement("bpmn:intermediateThrowEvent",
							"Intermediate Event Link Throw");
					checkLinkEvent();

					navigateFollowingElement(n, "bpmn:userTask", "User Task 16");

					navigateFollowingElement("bpmn:endEvent",
							"End Event 6 Message");
					checkMessageFlow("Message Flow 2", Direction.Output);

				}

				{
					// FLOW following the event message catch

					navigateElementX(
							"bpmn:subProcess[@name='Collapsed Sub-Process 2']",
							"L2CollapsedSubProcess");
					checkAssociation(ArtifactType.DataStoreReference,
							"Data Store Reference", Direction.Input);

					{
						selectElement("bpmn:exclusiveGateway[@name='Exclusive Gateway 4']");
						navigateGatewaySequenceFlow("Default Sequence Flow 2");
						checkDefaultSequenceFlow();
						pop();
					}

					navigateElementX("bpmn:intermediateThrowEvent[@name='Intermediate Event Message Throw']");
					checkMessageEvent();

					n = navigateElementX("bpmn:callActivity[@name='Collapsed Call Activity']");

					navigateBoundaryEvent("Boundary Intermediate Event Non-Interrupting Escalation");
					checkEscalationEvent();

					navigateFollowingElement("bpmn:task", "Task 18");

					navigateFollowingElement("bpmn:intermediateThrowEvent", "");
					checkEscalationEvent();

					navigateFollowingElement("bpmn:task", "Task 23");

					n = navigateFollowingElement(n, "bpmn:task", "Task 17");
					navigateBoundaryEvent("Boundary Intermediate Event Non-Interrupting Message");
					checkCancelActivity(false);
					checkParallelMultiple(false);
					checkMessageEvent();

					navigateFollowingElement("bpmn:task", "Task 19");

					navigateFollowingElement("bpmn:endEvent",
							"End Event 7 None");

					navigateFollowingElement(n, "bpmn:endEvent",
							"End Event 7 None");

				}

				{
					// FLOW following the receive Task 20

					navigateElementX("bpmn:receiveTask[@name='Receive Task 20']");

					n = navigateFollowingElement("bpmn:task", "Task 21");
					navigateBoundaryEvent("Boundary Intermediate Event Interrupting Timer");
					checkCancelActivity(true);
					checkTimerEvent();

					navigateFollowingElement("bpmn:task", "Task 27");

					navigateFollowingElement("bpmn:inclusiveGateway",
							"Inclusive Gateway 6");

					{
						selectFollowingElement(n, "bpmn:subProcess",
								"Expanded Sub-Process 2");
						navigateElement("bpmn:startEvent", "Start Event 5 None");

						navigateFollowingElement("bpmn:serviceTask",
								"Service Task 22");
						checkMultiInstanceParallel();

						navigateFollowingElement("bpmn:endEvent",
								"End Event 8 None");

						pop();
					}

					navigateFollowingElement("bpmn:task", "Task 23");
					navigateBoundaryEvent("Boundary Intermediate Event Non-Interrupting Signal");
					checkSignalEvent();
					checkCancelActivity(false);

					navigateFollowingElement("bpmn:task", "Task 24");

					navigateFollowingElement("bpmn:endEvent",
							"End Event 11 Escatation");

				}

				{
					navigateElement("bpmn:startEvent", "Start Event 6 Signal");

					navigateFollowingElement("bpmn:task", "Task 25");

					navigateFollowingElement("bpmn:parallelGateway",
							"Parallel Gateway 5");

					navigateFollowingElement("bpmn:task", "Task 26");

					navigateFollowingElement("bpmn:intermediateThrowEvent",
							"Intermediate Event Signal Throw 2");

					navigateFollowingElement("bpmn:inclusiveGateway",
							"Inclusive Gateway 6");

					navigateFollowingElement("bpmn:task", "Task 28");

					navigateFollowingElement("bpmn:parallelGateway",
							"Parallel Gateway 7");

					navigateFollowingElement("bpmn:endEvent",
							"End Event 11 Escatation");

					navigateElement("bpmn:parallelGateway",
							"Parallel Gateway 5");

					navigateFollowingElement("bpmn:subProcess",
							"Expanded Sub-Process 3");

					navigateElement("bpmn:intermediateCatchEvent",
							"Intermediate Event Link Catch");

					{
						selectFollowingElement("bpmn:subProcess",
								"Expanded Sub-Process 3");

						navigateElement("bpmn:startEvent", "Start Event 7");

						navigateFollowingElement("bpmn:intermediateCatchEvent",
								"Intermediate Event Signal Catch");
						checkSignalEvent();

						navigateFollowingElement("bpmn:task", "Task 31");

						n = navigateFollowingElement("bpmn:exclusiveGateway",
								"Exclusive Gateway 7");

						navigateFollowingElement(n, "bpmn:endEvent",
								"End Event 12");

						navigateFollowingElement(n, "bpmn:endEvent",
								"End Event 13 Error");
						checkErrorEvent();

						pop();
					}

					navigateFollowingElement("bpmn:task", "Task 32");
					navigateBoundaryEvent("");

					navigateFollowingElement("bpmn:task", "Task 33");

					navigateFollowingElement("bpmn:endEvent", "End Event 14");

				}

				pop();
			}

			pop();
		}

	}
}
