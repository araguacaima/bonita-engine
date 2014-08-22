/**
 * Copyright (C) 2012 BonitaSoft S.A.
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
package org.bonitasoft.engine.core.process.instance.model.archive.builder.impl;

import org.bonitasoft.engine.core.process.definition.model.STransitionDefinition;
import org.bonitasoft.engine.core.process.definition.model.TransitionState;
import org.bonitasoft.engine.core.process.instance.model.SFlowNodeInstance;
import org.bonitasoft.engine.core.process.instance.model.SStateCategory;
import org.bonitasoft.engine.core.process.instance.model.archive.builder.SATransitionInstanceBuilder;
import org.bonitasoft.engine.core.process.instance.model.archive.builder.SATransitionInstanceBuilderFactory;
import org.bonitasoft.engine.core.process.instance.model.archive.impl.SATransitionInstanceImpl;

/**
 * @author Hongwen Zang
 */
public class SATransitionInstanceBuilderFactoryImpl extends SAFlowElementInstanceBuilderFactoryImpl implements SATransitionInstanceBuilderFactory {

    @Override
    public SATransitionInstanceBuilder createNewTransitionInstance(final STransitionDefinition sTransitionDefinition,
            final SFlowNodeInstance sFlowNodeInstance, final TransitionState state) {
        final SATransitionInstanceImpl entity = new SATransitionInstanceImpl(sTransitionDefinition, sFlowNodeInstance);
        entity.setState(state);
        entity.setStateCategory(SStateCategory.ABORTING);
        return new SATransitionInstanceBuilderImpl(entity);
    }

}
