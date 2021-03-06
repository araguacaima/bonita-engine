/**
 * Copyright (C) 2015 BonitaSoft S.A.
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
package org.bonitasoft.engine.core.process.instance.model.builder.event.trigger;

import org.bonitasoft.engine.core.process.instance.model.event.trigger.STimerEventTriggerInstance;

/**
 * @author Elias Ricken de Medeiros
 * @author Celine Souchet
 */
public interface STimerEventTriggerInstanceBuilderFactory extends SEventTriggerInstanceBuilderFactory {

    STimerEventTriggerInstanceBuilder createNewTimerEventTriggerInstance(long eventInstanceId, String eventInstanceName, long executionDate,
            String jobTriggerName);

    STimerEventTriggerInstanceBuilder createNewInstance(STimerEventTriggerInstance sEventTriggerInstance);

}
