/*******************************************************************************
 * Copyright (C) 2015 BonitaSoft S.A.
 * BonitaSoft is a trademark of BonitaSoft SA.
 * This software file is BONITASOFT CONFIDENTIAL. Not For Distribution.
 * For commercial licensing information, contact:
 * BonitaSoft, 32 rue Gustave Eiffel – 38000 Grenoble
 * or BonitaSoft US, 51 Federal Street, Suite 305, San Francisco, CA 94107
 *******************************************************************************/
package com.bonitasoft.engine.api.impl;

import java.util.List;

import org.bonitasoft.engine.api.ImportStatus;
import org.bonitasoft.engine.api.impl.SessionInfos;
import org.bonitasoft.engine.api.impl.application.ApplicationAPIDelegate;
import org.bonitasoft.engine.api.impl.application.ApplicationExporterDelegate;
import org.bonitasoft.engine.api.impl.application.ApplicationMenuAPIDelegate;
import org.bonitasoft.engine.api.impl.application.ApplicationPageAPIDelegate;
import org.bonitasoft.engine.api.impl.converter.ApplicationMenuModelConverter;
import org.bonitasoft.engine.api.impl.converter.ApplicationModelConverter;
import org.bonitasoft.engine.api.impl.converter.ApplicationPageModelConverter;
import org.bonitasoft.engine.api.impl.transaction.application.SearchApplicationMenus;
import org.bonitasoft.engine.api.impl.transaction.application.SearchApplicationPages;
import org.bonitasoft.engine.api.impl.transaction.application.SearchApplications;
import org.bonitasoft.engine.api.impl.validator.ApplicationMenuCreatorValidator;
import org.bonitasoft.engine.business.application.ApplicationService;
import org.bonitasoft.engine.business.application.converter.ApplicationContainerConverter;
import org.bonitasoft.engine.business.application.converter.ApplicationMenuNodeConverter;
import org.bonitasoft.engine.business.application.converter.ApplicationNodeConverter;
import org.bonitasoft.engine.business.application.converter.ApplicationPageNodeConverter;
import org.bonitasoft.engine.business.application.exporter.ApplicationContainerExporter;
import org.bonitasoft.engine.business.application.exporter.ApplicationExporter;
import org.bonitasoft.engine.business.application.importer.ApplicationContainerImporter;
import org.bonitasoft.engine.business.application.importer.ApplicationImporter;
import org.bonitasoft.engine.business.application.importer.ApplicationMenuImporter;
import org.bonitasoft.engine.business.application.importer.ApplicationPageImporter;
import org.bonitasoft.engine.business.application.importer.ApplicationsImporter;
import org.bonitasoft.engine.business.application.importer.StrategySelector;
import org.bonitasoft.engine.exception.AlreadyExistsException;
import org.bonitasoft.engine.exception.BonitaRuntimeException;
import org.bonitasoft.engine.exception.CreationException;
import org.bonitasoft.engine.exception.DeletionException;
import org.bonitasoft.engine.exception.ExportException;
import org.bonitasoft.engine.exception.ImportException;
import org.bonitasoft.engine.exception.SearchException;
import org.bonitasoft.engine.exception.UpdateException;
import org.bonitasoft.engine.page.PageService;
import org.bonitasoft.engine.search.SearchOptions;
import org.bonitasoft.engine.search.SearchResult;
import org.bonitasoft.engine.search.descriptor.SearchApplicationDescriptor;
import org.bonitasoft.engine.search.descriptor.SearchApplicationMenuDescriptor;
import org.bonitasoft.engine.search.descriptor.SearchApplicationPageDescriptor;
import org.bonitasoft.engine.search.impl.SearchResultImpl;
import org.bonitasoft.engine.sessionaccessor.SessionAccessor;

import com.bonitasoft.engine.api.ApplicationAPI;
import com.bonitasoft.engine.api.converter.CollectionConverter;
import com.bonitasoft.engine.business.application.Application;
import com.bonitasoft.engine.business.application.ApplicationCreator;
import com.bonitasoft.engine.business.application.ApplicationImportPolicy;
import com.bonitasoft.engine.business.application.ApplicationMenu;
import com.bonitasoft.engine.business.application.ApplicationMenuCreator;
import com.bonitasoft.engine.business.application.ApplicationMenuNotFoundException;
import com.bonitasoft.engine.business.application.ApplicationMenuUpdater;
import com.bonitasoft.engine.business.application.ApplicationNotFoundException;
import com.bonitasoft.engine.business.application.ApplicationPage;
import com.bonitasoft.engine.business.application.ApplicationPageNotFoundException;
import com.bonitasoft.engine.business.application.ApplicationUpdater;
import com.bonitasoft.engine.business.application.impl.ApplicationConverter;
import com.bonitasoft.engine.business.application.impl.ApplicationMenuConverter;
import com.bonitasoft.engine.business.application.impl.ApplicationPageConverter;
import com.bonitasoft.engine.service.TenantServiceAccessor;
import com.bonitasoft.engine.service.impl.ServiceAccessorFactory;
import com.bonitasoft.engine.service.impl.TenantServiceSingleton;

/**
 * @author Elias Ricken de Medeiros
 * @deprecated from version 7.0 on, use {@link org.bonitasoft.engine.api.impl.ApplicationAPIImpl} instead.
 */
@Deprecated
public class ApplicationAPIImpl implements ApplicationAPI {

    @Override
    public Application createApplication(final ApplicationCreator applicationCreator) throws AlreadyExistsException, CreationException {
        final org.bonitasoft.engine.business.application.Application application = getApplicationAPIDelegate().createApplication(
                applicationCreator.getDelegate());
        return new ApplicationConverter().convert(application);
    }

    private ApplicationAPIDelegate getApplicationAPIDelegate() {
        return new ApplicationAPIDelegate(getTenantAccessor(), new ApplicationModelConverter(getTenantAccessor().getPageService()),
                SessionInfos.getUserIdFromSession());
    }

    private ApplicationPageAPIDelegate getApplicationPageAPIDelegate() {
        return new ApplicationPageAPIDelegate(getTenantAccessor(), new ApplicationPageModelConverter(), SessionInfos.getUserIdFromSession());
    }

    private ApplicationMenuAPIDelegate getApplicationMenuAPIDelegate() {
        return new ApplicationMenuAPIDelegate(getTenantAccessor(), new ApplicationMenuModelConverter(),
                new ApplicationMenuCreatorValidator(), SessionInfos.getUserIdFromSession());
    }

    private ApplicationExporterDelegate getApplicationExporterDelegate() {
        final TenantServiceAccessor tenantAccessor = getTenantAccessor();
        final ApplicationService applicationService = tenantAccessor.getApplicationService();
        final PageService pageService = tenantAccessor.getPageService();
        final ApplicationNodeConverter applicationNodeConverter = new ApplicationNodeConverter(tenantAccessor.getProfileService(),
                applicationService, new ApplicationPageNodeConverter(pageService), new ApplicationMenuNodeConverter(applicationService), pageService);
        final ApplicationContainerConverter applicationContainerConverter = new ApplicationContainerConverter(applicationNodeConverter);
        final ApplicationContainerExporter applicationContainerExporter = new ApplicationContainerExporter();
        final ApplicationExporter applicationExporter = new ApplicationExporter(applicationContainerConverter, applicationContainerExporter);
        return new ApplicationExporterDelegate(tenantAccessor.getApplicationService(), applicationExporter);
    }

    private ApplicationsImporter getApplicationImporter(final org.bonitasoft.engine.business.application.ApplicationImportPolicy policy) {
        final TenantServiceAccessor tenantAccessor = getTenantAccessor();
        final ApplicationService applicationService = tenantAccessor.getApplicationService();
        final PageService pageService = tenantAccessor.getPageService();
        final ApplicationPageNodeConverter applicationPageNodeConverter = new ApplicationPageNodeConverter(pageService);
        final ApplicationMenuNodeConverter applicationMenuNodeConverter = new ApplicationMenuNodeConverter(applicationService);
        final ApplicationPageImporter applicationPageImporter = new ApplicationPageImporter(tenantAccessor.getApplicationService(),
                applicationPageNodeConverter);
        final ApplicationMenuImporter applicationMenuImporter = new ApplicationMenuImporter(tenantAccessor.getApplicationService(),
                applicationMenuNodeConverter);
        final ApplicationImporter applicationImporter = new ApplicationImporter(tenantAccessor.getApplicationService(),
                new StrategySelector().selectStrategy(policy), new ApplicationNodeConverter(tenantAccessor.getProfileService(),
                        applicationService, applicationPageNodeConverter, applicationMenuNodeConverter, pageService), applicationPageImporter,
                applicationMenuImporter);
        return new ApplicationsImporter(new ApplicationContainerImporter(), applicationImporter);
    }

    @Override
    public Application getApplication(final long applicationId) throws ApplicationNotFoundException {
        try {
            return new ApplicationConverter().convert(getApplicationAPIDelegate().getApplication(applicationId));
        } catch (final org.bonitasoft.engine.business.application.ApplicationNotFoundException e) {
            throw new ApplicationNotFoundException(applicationId);
        }
    }

    @Override
    public void deleteApplication(final long applicationId) throws DeletionException {
        getApplicationAPIDelegate().deleteApplication(applicationId);
    }

    @Override
    public Application updateApplication(final long applicationId, final ApplicationUpdater updater) throws ApplicationNotFoundException, UpdateException,
            AlreadyExistsException {
        try {
            return new ApplicationConverter().convert(getApplicationAPIDelegate().updateApplication(applicationId, updater.getDelegate()));
        } catch (final org.bonitasoft.engine.business.application.ApplicationNotFoundException e) {
            throw new ApplicationNotFoundException(applicationId);
        }
    }

    private TenantServiceAccessor getTenantAccessor() {
        try {
            final SessionAccessor sessionAccessor = ServiceAccessorFactory.getInstance().createSessionAccessor();
            final long tenantId = sessionAccessor.getTenantId();
            return TenantServiceSingleton.getInstance(tenantId);
        } catch (final Exception e) {
            throw new BonitaRuntimeException(e);
        }
    }

    @Override
    public SearchResult<Application> searchApplications(final SearchOptions searchOptions) throws SearchException {
        final TenantServiceAccessor tenantAccessor = getTenantAccessor();
        final SearchApplicationDescriptor appSearchDescriptor = tenantAccessor.getSearchEntitiesDescriptor().getSearchApplicationDescriptor();
        final ApplicationModelConverter converter = new ApplicationModelConverter(getTenantAccessor().getPageService());
        final ApplicationService applicationService = tenantAccessor.getApplicationService();
        final SearchApplications searchApplications = new SearchApplications(applicationService, appSearchDescriptor, searchOptions, converter);
        final SearchResult<org.bonitasoft.engine.business.application.Application> searchResult = getApplicationAPIDelegate().searchApplications(
                searchApplications);
        return new SearchResultImpl<>(searchResult.getCount(), new CollectionConverter().convert(searchResult.getResult(),
                new ApplicationConverter()));
    }

    @Override
    public ApplicationPage createApplicationPage(final long applicationId, final long pageId, final String token) throws AlreadyExistsException,
            CreationException {
        return new ApplicationPageConverter().convert(getApplicationPageAPIDelegate().createApplicationPage(applicationId, pageId, token));
    }

    @Override
    public ApplicationPage getApplicationPage(final String applicationName, final String applicationPageToken) throws ApplicationPageNotFoundException {
        try {
            return new ApplicationPageConverter().convert(getApplicationPageAPIDelegate().getApplicationPage(applicationName, applicationPageToken));
        } catch (final org.bonitasoft.engine.business.application.ApplicationPageNotFoundException e) {
            throw new ApplicationPageNotFoundException(e.getMessage());
        }
    }

    @Override
    public SearchResult<ApplicationPage> searchApplicationPages(final SearchOptions searchOptions) throws SearchException {
        final TenantServiceAccessor tenantAccessor = getTenantAccessor();
        final SearchApplicationPageDescriptor appPageSearchDescriptor = tenantAccessor.getSearchEntitiesDescriptor().getSearchApplicationPageDescriptor();
        final ApplicationPageModelConverter converter = new ApplicationPageModelConverter();
        final ApplicationService applicationService = tenantAccessor.getApplicationService();
        final SearchApplicationPages searchApplicationPages = new SearchApplicationPages(applicationService, converter, appPageSearchDescriptor, searchOptions);
        final SearchResult<org.bonitasoft.engine.business.application.ApplicationPage> applicationPages = getApplicationPageAPIDelegate()
                .searchApplicationPages(searchApplicationPages);
        return new SearchResultImpl<>(applicationPages.getCount(), new CollectionConverter().convert(applicationPages.getResult(),
                new ApplicationPageConverter()));
    }

    @Override
    public ApplicationPage getApplicationPage(final long applicationPageId) throws ApplicationPageNotFoundException {
        try {
            return new ApplicationPageConverter().convert(getApplicationPageAPIDelegate().getApplicationPage(applicationPageId));
        } catch (final org.bonitasoft.engine.business.application.ApplicationPageNotFoundException e) {
            throw new ApplicationPageNotFoundException(e.getMessage());
        }
    }

    @Override
    public void deleteApplicationPage(final long applicationPageId) throws DeletionException {
        getApplicationPageAPIDelegate().deleteApplicationPage(applicationPageId);
    }

    @Override
    public void setApplicationHomePage(final long applicationId, final long applicationPageId) throws UpdateException, ApplicationNotFoundException {
        try {
            getApplicationPageAPIDelegate().setApplicationHomePage(applicationId, applicationPageId);
        } catch (final org.bonitasoft.engine.business.application.ApplicationNotFoundException e) {
            throw new ApplicationNotFoundException(applicationId);
        }
    }

    @Override
    public ApplicationPage getApplicationHomePage(final long applicationId) throws ApplicationPageNotFoundException {
        try {
            return new ApplicationPageConverter().convert(getApplicationPageAPIDelegate().getApplicationHomePage(applicationId));
        } catch (final org.bonitasoft.engine.business.application.ApplicationPageNotFoundException e) {
            throw new ApplicationPageNotFoundException(e.getMessage());
        }
    }

    @Override
    public ApplicationMenu createApplicationMenu(final ApplicationMenuCreator applicationMenuCreator) throws CreationException {
        return new ApplicationMenuConverter().convert(getApplicationMenuAPIDelegate().createApplicationMenu(applicationMenuCreator.getDelegate()));
    }

    @Override
    public ApplicationMenu updateApplicationMenu(final long applicationMenuId, final ApplicationMenuUpdater updater) throws ApplicationMenuNotFoundException,
            UpdateException {
        try {
            return new ApplicationMenuConverter().convert(getApplicationMenuAPIDelegate().updateApplicationMenu(applicationMenuId, updater.getDelegate()));
        } catch (final org.bonitasoft.engine.business.application.ApplicationMenuNotFoundException e) {
            throw new ApplicationMenuNotFoundException(e.getMessage());
        }
    }

    @Override
    public ApplicationMenu getApplicationMenu(final long applicationMenuId) throws ApplicationMenuNotFoundException {
        try {
            return new ApplicationMenuConverter().convert(getApplicationMenuAPIDelegate().getApplicationMenu(applicationMenuId));
        } catch (final org.bonitasoft.engine.business.application.ApplicationMenuNotFoundException e) {
            throw new ApplicationMenuNotFoundException(e.getMessage());
        }
    }

    @Override
    public void deleteApplicationMenu(final long applicationMenuId) throws DeletionException {
        getApplicationMenuAPIDelegate().deleteApplicationMenu(applicationMenuId);
    }

    @Override
    public SearchResult<ApplicationMenu> searchApplicationMenus(final SearchOptions searchOptions) throws SearchException {
        final TenantServiceAccessor tenantAccessor = getTenantAccessor();
        final ApplicationService applicationService = tenantAccessor.getApplicationService();
        final ApplicationMenuModelConverter converter = new ApplicationMenuModelConverter();
        final SearchApplicationMenuDescriptor searchDescriptor = tenantAccessor.getSearchEntitiesDescriptor().getSearchApplicationMenuDescriptor();
        final SearchApplicationMenus searchApplicationMenus = new SearchApplicationMenus(applicationService, converter, searchDescriptor, searchOptions);
        final SearchResult<org.bonitasoft.engine.business.application.ApplicationMenu> applicationMenus = getApplicationMenuAPIDelegate()
                .searchApplicationMenus(searchApplicationMenus);
        return new SearchResultImpl<>(applicationMenus.getCount(), new CollectionConverter().convert(applicationMenus.getResult(),
                new ApplicationMenuConverter()));
    }

    @Override
    public List<String> getAllPagesForProfile(final long profileId) {
        return getApplicationPageAPIDelegate().getAllPagesForProfile(profileId);
    }

    @Override
    public byte[] exportApplications(final long... applicationIds) throws ExportException {
        return getApplicationExporterDelegate().exportApplications(applicationIds);
    }

    @Override
    public List<ImportStatus> importApplications(final byte[] xmlContent, final ApplicationImportPolicy policy) throws ImportException, AlreadyExistsException {
        return getApplicationImporter(org.bonitasoft.engine.business.application.ApplicationImportPolicy.valueOf(policy.name())).importApplications(xmlContent,
                SessionInfos.getUserIdFromSession());
    }

}
