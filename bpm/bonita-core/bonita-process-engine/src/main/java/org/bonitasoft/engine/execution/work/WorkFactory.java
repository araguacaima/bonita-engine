/**
 * Copyright (C) 2013-2014 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation
 * version 2.1 of the License.
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License along with this
 * program; if not, write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth
 * Floor, Boston, MA 02110-1301, USA.
 **/
package org.bonitasoft.engine.execution.work;

import java.util.List;

import org.bonitasoft.engine.bpm.connector.ConnectorEvent;
import org.bonitasoft.engine.core.expression.control.model.SExpressionContext;
import org.bonitasoft.engine.core.operation.model.SOperation;
import org.bonitasoft.engine.core.process.instance.model.event.handling.SMessageInstance;
import org.bonitasoft.engine.core.process.instance.model.event.handling.SWaitingMessageEvent;
import org.bonitasoft.engine.execution.work.failurehandling.FailureHandlingConnectorDefinitionAndInstanceContextWork;
import org.bonitasoft.engine.execution.work.failurehandling.FailureHandlingFlowNodeDefinitionAndInstanceWithProcessContextWork;
import org.bonitasoft.engine.execution.work.failurehandling.FailureHandlingMessageInstanceContextWork;
import org.bonitasoft.engine.execution.work.failurehandling.FailureHandlingProcessDefinitionAndInstanceContextWork;
import org.bonitasoft.engine.work.BonitaWork;

/**
 * Factory to construct works
 * 
 * @author Baptiste Mesta
 * @author Celine Souchet
 */
public class WorkFactory {

    private WorkFactory() {
        // Utility classes should not have a public constructor
    }

    public static BonitaWork createExecuteConnectorOfActivity(final long processDefinitionId, final long flowNodeDefinitionId, final long flowNodeInstanceId,
            final long connectorInstanceId, final String connectorDefinitionName) {
        BonitaWork wrappedWork = new ExecuteConnectorOfActivity(processDefinitionId, flowNodeDefinitionId, flowNodeInstanceId,
                connectorInstanceId, connectorDefinitionName);
        wrappedWork = new FailureHandlingConnectorDefinitionAndInstanceContextWork(wrappedWork, connectorDefinitionName, connectorInstanceId);
        return new FailureHandlingFlowNodeDefinitionAndInstanceWithProcessContextWork(wrappedWork, flowNodeInstanceId);
    }

    public static BonitaWork createExecuteConnectorOfProcess(final long processDefinitionId, final long processInstanceId, final long rootProcessInstanceId,
            final long connectorInstanceId, final String connectorDefinitionName, final ConnectorEvent activationEvent) {
        BonitaWork wrappedWork = new ExecuteConnectorOfProcess(processDefinitionId, connectorInstanceId, connectorDefinitionName, processInstanceId,
                rootProcessInstanceId, activationEvent);
        wrappedWork = new FailureHandlingProcessDefinitionAndInstanceContextWork(wrappedWork, processDefinitionId, processInstanceId, rootProcessInstanceId);
        return new FailureHandlingConnectorDefinitionAndInstanceContextWork(wrappedWork, connectorDefinitionName, connectorInstanceId, activationEvent);
    }

    public static BonitaWork createExecuteFlowNodeWork(final long processInstanceId, final long flowNodeInstanceId, final List<SOperation> operations,
            final SExpressionContext contextDependency) {
        if (processInstanceId <= 0) {
            throw new RuntimeException("It is forbidden to create a ExecuteFlowNodeWork with a processInstanceId equals to " + processInstanceId);
        }
        BonitaWork wrappedWork = new ExecuteFlowNodeWork(flowNodeInstanceId, operations, contextDependency, processInstanceId);
        wrappedWork = new LockProcessInstanceWork(new TxBonitaWork(wrappedWork), processInstanceId);
        return new FailureHandlingFlowNodeDefinitionAndInstanceWithProcessContextWork(wrappedWork, flowNodeInstanceId);
    }

    public static BonitaWork createExecuteMessageCoupleWork(final SMessageInstance messageInstance, final SWaitingMessageEvent waitingMessage) {
        // no target process: we do not wrap in a LockProcessInstanceWork
        BonitaWork work = new TxBonitaWork(new ExecuteMessageCoupleWork(messageInstance.getId(), waitingMessage.getId()));
        if (waitingMessage.getParentProcessInstanceId() > 0) {
            work = new LockProcessInstanceWork(work, waitingMessage.getParentProcessInstanceId());
        }
        work = new FailureHandlingMessageInstanceContextWork(work, messageInstance, waitingMessage);
        return new FailureHandlingFlowNodeDefinitionAndInstanceWithProcessContextWork(work, waitingMessage.getFlowNodeInstanceId());
    }

    public static BonitaWork createNotifyChildFinishedWork(final long processDefinitionId, final long processInstanceId, final long flowNodeInstanceId,
            final long parentId, final String parentType) {
        BonitaWork wrappedWork = new NotifyChildFinishedWork(processDefinitionId, flowNodeInstanceId, parentId, parentType);
        wrappedWork = new LockProcessInstanceWork(new TxBonitaWork(wrappedWork), processInstanceId);
        return new FailureHandlingFlowNodeDefinitionAndInstanceWithProcessContextWork(wrappedWork, flowNodeInstanceId);
    }
}
