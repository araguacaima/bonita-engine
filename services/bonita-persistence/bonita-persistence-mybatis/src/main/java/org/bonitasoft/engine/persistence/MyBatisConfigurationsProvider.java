/*******************************************************************************
 * Copyright (C) 2009, 2012 BonitaSoft S.A.
 * BonitaSoft is a trademark of BonitaSoft SA.
 * This software file is BONITASOFT CONFIDENTIAL. Not For Distribution.
 * For commercial licensing information, contact:
 * BonitaSoft, 32 rue Gustave Eiffel – 38000 Grenoble
 * or BonitaSoft US, 51 Federal Street, Suite 305, San Francisco, CA 94107
 *******************************************************************************/
package org.bonitasoft.engine.persistence;

import java.util.List;

/**
 * @author Baptiste Mesta
 */
public class MyBatisConfigurationsProvider implements AbstractMyBatisConfigurationsProvider {

    private List<MyBatisConfiguration> myBatisConfigurations;

    public void setMyBatisConfigurations(final List<MyBatisConfiguration> myBatisConfigurations) {
        this.myBatisConfigurations = myBatisConfigurations;
    }

    @Override
    public List<? extends AbstractMyBatisConfiguration> getConfigurations() {
        return this.myBatisConfigurations;
    }

}
