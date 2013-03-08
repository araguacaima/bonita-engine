/*******************************************************************************
 * Copyright (C) 2009, 2013 BonitaSoft S.A.
 * BonitaSoft is a trademark of BonitaSoft SA.
 * This software file is BONITASOFT CONFIDENTIAL. Not For Distribution.
 * For commercial licensing information, contact:
 * BonitaSoft, 32 rue Gustave Eiffel – 38000 Grenoble
 * or BonitaSoft US, 51 Federal Street, Suite 305, San Francisco, CA 94107
 *******************************************************************************/
package com.bonitasoft.engine.identity;

import static org.junit.Assert.assertEquals;

import org.bonitasoft.engine.api.IdentityAPI;
import org.bonitasoft.engine.exception.BonitaException;
import org.bonitasoft.engine.identity.Role;
import org.bonitasoft.engine.identity.RoleBuilder;
import org.bonitasoft.engine.session.APISession;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.bonitasoft.engine.CommonAPISPTest;
import com.bonitasoft.engine.SPBPMTestUtil;
import com.bonitasoft.engine.api.TenantAPIAccessor;

public class SPRoleTest extends CommonAPISPTest {

    private static APISession session;

    @Before
    public void before() throws BonitaException {
        session = SPBPMTestUtil.loginDefaultTenant();
    }

    @After
    public void after() throws BonitaException {
        SPBPMTestUtil.logoutTenant(session);
    }

    @Test
    public void aSameRoleNameCanBeUseInTwoTenants() throws BonitaException {
        final String role = "role";
        final long tenantId1 = SPBPMTestUtil.constructTenant("tenant1", "iconName", "iconPath", "technical_user_username", "technical_user_password");
        final APISession session1 = SPBPMTestUtil.loginTenant(tenantId1);
        final IdentityAPI identityAPI1 = TenantAPIAccessor.getIdentityAPI(session1);
        final RoleBuilder roleBuilder1 = new RoleBuilder().createNewInstance(role);
        final Role role1 = identityAPI1.createRole(roleBuilder1.done());

        final APISession session2 = SPBPMTestUtil.loginDefaultTenant();
        final IdentityAPI identityAPI2 = TenantAPIAccessor.getIdentityAPI(session2);
        final RoleBuilder roleBuilder2 = new RoleBuilder().createNewInstance(role);
        final Role role2 = identityAPI2.createRole(roleBuilder2.done());

        assertEquals(role, role1.getName());
        assertEquals(role1.getName(), role2.getName());
        identityAPI1.deleteRole(role1.getId());
        identityAPI2.deleteRole(role2.getId());

        SPBPMTestUtil.logoutTenant(session1);
        SPBPMTestUtil.destroyTenant(tenantId1);
        SPBPMTestUtil.logoutTenant(session2);
    }

}
