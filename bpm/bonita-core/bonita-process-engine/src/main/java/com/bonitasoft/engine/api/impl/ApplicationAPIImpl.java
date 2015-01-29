/*******************************************************************************
 * Copyright (C) 2014 BonitaSoft S.A.
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
import org.bonitasoft.engine.api.impl.converter.ApplicationMenuConvertor;
import org.bonitasoft.engine.api.impl.converter.ApplicationModelConvertor;
import org.bonitasoft.engine.api.impl.converter.ApplicationPageConvertor;
import org.bonitasoft.engine.api.impl.transaction.application.SearchApplications;
import org.bonitasoft.engine.business.application.ApplicationService;
import org.bonitasoft.engine.exception.AlreadyExistsException;
import org.bonitasoft.engine.exception.BonitaRuntimeException;
import org.bonitasoft.engine.exception.CreationException;
import org.bonitasoft.engine.exception.DeletionException;
import org.bonitasoft.engine.exception.ExportException;
import org.bonitasoft.engine.exception.ImportException;
import org.bonitasoft.engine.exception.SearchException;
import org.bonitasoft.engine.exception.UpdateException;
import org.bonitasoft.engine.search.SearchOptions;
import org.bonitasoft.engine.search.SearchResult;
import org.bonitasoft.engine.search.descriptor.SearchApplicationDescriptor;
import org.bonitasoft.engine.search.impl.SearchResultImpl;
import org.bonitasoft.engine.sessionaccessor.SessionAccessor;

import com.bonitasoft.engine.api.ApplicationAPI;
import com.bonitasoft.engine.api.converter.CollectionConverter;
import com.bonitasoft.engine.api.impl.transaction.application.SearchApplicationMenus;
import com.bonitasoft.engine.api.impl.transaction.application.SearchApplicationPages;
import com.bonitasoft.engine.api.impl.validator.ApplicationMenuCreatorValidator;
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
import com.bonitasoft.engine.business.application.converter.ApplicationContainerConverter;
import com.bonitasoft.engine.business.application.converter.ApplicationMenuNodeConverter;
import com.bonitasoft.engine.business.application.converter.ApplicationNodeConverter;
import com.bonitasoft.engine.business.application.converter.ApplicationPageNodeConverter;
import com.bonitasoft.engine.business.application.exporter.ApplicationContainerExporter;
import com.bonitasoft.engine.business.application.exporter.ApplicationExporter;
import com.bonitasoft.engine.business.application.impl.ApplicationConverter;
import com.bonitasoft.engine.business.application.importer.ApplicationContainerImporter;
import com.bonitasoft.engine.business.application.importer.ApplicationImporter;
import com.bonitasoft.engine.business.application.importer.ApplicationMenuImporter;
import com.bonitasoft.engine.business.application.importer.ApplicationPageImporter;
import com.bonitasoft.engine.business.application.importer.ApplicationsImporter;
import com.bonitasoft.engine.business.application.importer.StrategySelector;
import com.bonitasoft.engine.search.descriptor.SearchApplicationMenuDescriptor;
import com.bonitasoft.engine.search.descriptor.SearchApplicationPageDescriptor;
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
        return getApplicationAPIDelegate(null);
    }

    private ApplicationAPIDelegate getApplicationAPIDelegate(final SearchOptions searchOptions) {
        final TenantServiceAccessor tenantAccessor = getTenantAccessor();
        final SearchApplicationDescriptor appSearchDescriptor = tenantAccessor.getSearchEntitiesDescriptor().getSearchApplicationDescriptor();
        final ApplicationModelConvertor convertor = new ApplicationModelConvertor();
        final ApplicationService applicationService = tenantAccessor.getApplicationService();
        final SearchApplications searchApplications = new SearchApplications(applicationService, appSearchDescriptor, searchOptions, convertor);
        return new ApplicationAPIDelegate(tenantAccessor, convertor, SessionInfos.getUserIdFromSession(), searchApplications);
    }

    private ApplicationPageAPIDelegate getApplicationPageAPIDelegate() {
        return getApplicationPageAPIDelegate(null);
    }

    private ApplicationPageAPIDelegate getApplicationPageAPIDelegate(final SearchOptions searchOptions) {
        final TenantServiceAccessor tenantAccessor = getTenantAccessor();
        final SearchApplicationPageDescriptor appPageSearchDescriptor = tenantAccessor.getSearchEntitiesDescriptor().getSearchApplicationPageDescriptor();
        final ApplicationPageConvertor convertor = new ApplicationPageConvertor();
        final ApplicationService applicationService = tenantAccessor.getApplicationService();
        final SearchApplicationPages searchApplicationPages = new SearchApplicationPages(applicationService, convertor, appPageSearchDescriptor,
                searchOptions);
        return new ApplicationPageAPIDelegate(tenantAccessor, convertor, searchApplicationPages, SessionInfos.getUserIdFromSession());
    }

    private ApplicationMenuAPIDelegate getApplicationMenuAPIDelegate() {
        return getApplicationMenuAPIDelegate(null);
    }

    private ApplicationMenuAPIDelegate getApplicationMenuAPIDelegate(final SearchOptions searchOptions) {
        final TenantServiceAccessor tenantAccessor = getTenantAccessor();
        final ApplicationService applicationService = tenantAccessor.getApplicationService();
        final ApplicationMenuConvertor convertor = new ApplicationMenuConvertor();
        final SearchApplicationMenuDescriptor searchDescriptor = tenantAccessor.getSearchEntitiesDescriptor().getSearchApplicationMenuDescriptor();
        final SearchApplicationMenus searchApplicationMenus = new SearchApplicationMenus(applicationService, convertor, searchDescriptor, searchOptions);
        final ApplicationMenuCreatorValidator validator = new ApplicationMenuCreatorValidator();
        return new ApplicationMenuAPIDelegate(tenantAccessor, convertor, searchApplicationMenus, validator,
                SessionInfos.getUserIdFromSession());
    }

    private ApplicationExporterDelegate getApplicationExporterDelegate() {
        final TenantServiceAccessor tenantAccessor = getTenantAccessor();
        final ApplicationService applicationService = tenantAccessor.getApplicationService();
        final ApplicationNodeConverter applicationNodeConverter = new ApplicationNodeConverter(tenantAccessor.getProfileService(),
                applicationService, new ApplicationPageNodeConverter(tenantAccessor.getPageService()), new ApplicationMenuNodeConverter(applicationService));
        final ApplicationContainerConverter applicationContainerConverter = new ApplicationContainerConverter(applicationNodeConverter);
        final ApplicationContainerExporter applicationContainerExporter = new ApplicationContainerExporter();
        final ApplicationExporter applicationExporter = new ApplicationExporter(applicationContainerConverter, applicationContainerExporter);
        return new ApplicationExporterDelegate(tenantAccessor.getApplicationService(), applicationExporter);
    }

    private ApplicationsImporter getApplicationImporter(final ApplicationImportPolicy policy) {
        final TenantServiceAccessor tenantAccessor = getTenantAccessor();
        final StrategySelector strategySelector = new StrategySelector();
        final ApplicationContainerImporter containerImporter = new ApplicationContainerImporter();
        final ApplicationService applicationService = tenantAccessor.getApplicationService();
        final ApplicationPageNodeConverter applicationPageNodeConverter = new ApplicationPageNodeConverter(tenantAccessor.getPageService());
        final ApplicationMenuNodeConverter applicationMenuNodeConverter = new ApplicationMenuNodeConverter(applicationService);
        final ApplicationNodeConverter applicationNodeConverter = new ApplicationNodeConverter(tenantAccessor.getProfileService(),
                applicationService, applicationPageNodeConverter, applicationMenuNodeConverter);
        final ApplicationPageImporter applicationPageImporter = new ApplicationPageImporter(tenantAccessor.getApplicationService(),
                applicationPageNodeConverter);
        final ApplicationMenuImporter applicationMenuImporter = new ApplicationMenuImporter(tenantAccessor.getApplicationService(),
                applicationMenuNodeConverter);
        final ApplicationImporter applicationImporter = new ApplicationImporter(tenantAccessor.getApplicationService(),
                strategySelector.selectStrategy(policy), applicationNodeConverter, applicationPageImporter, applicationMenuImporter);
        return new ApplicationsImporter(containerImporter, applicationImporter);
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
        return getApplicationAPIDelegate().updateApplication(applicationId, updater);
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
        final SearchResult<org.bonitasoft.engine.business.application.Application> searchResult = getApplicationAPIDelegate(searchOptions).searchApplications();
        final List<org.bonitasoft.engine.business.application.Application> results = searchResult.getResult();
        final CollectionConverter<org.bonitasoft.engine.business.application.Application, Application> applicationsConverter = new CollectionConverter<org.bonitasoft.engine.business.application.Application, Application>(
                new ApplicationConverter());
        return new SearchResultImpl<Application>(searchResult.getCount(), applicationsConverter.convert(results));
    }

    @Override
    public ApplicationPage createApplicationPage(final long applicationId, final long pageId, final String token) throws AlreadyExistsException,
            CreationException {
        return getApplicationPageAPIDelegate().createApplicationPage(applicationId, pageId, token);
    }

    @Override
    public ApplicationPage getApplicationPage(final String applicationName, final String applicationPageToken) throws ApplicationPageNotFoundException {
        return getApplicationPageAPIDelegate().getApplicationPage(applicationName, applicationPageToken);
    }

    @Override
    public SearchResult<ApplicationPage> searchApplicationPages(final SearchOptions searchOptions) throws SearchException {
        return getApplicationPageAPIDelegate(searchOptions).searchApplicationPages();
    }

    @Override
    public ApplicationPage getApplicationPage(final long applicationPageId) throws ApplicationPageNotFoundException {
        return getApplicationPageAPIDelegate().getApplicationPage(applicationPageId);
    }

    @Override
    public void deleteApplicationPage(final long applicationPageId) throws DeletionException {
        getApplicationPageAPIDelegate().deleteApplicationPage(applicationPageId);
    }

    @Override
    public void setApplicationHomePage(final long applicationId, final long applicationPageId) throws UpdateException, ApplicationNotFoundException {
        getApplicationPageAPIDelegate().setApplicationHomePage(applicationId, applicationPageId);
    }

    @Override
    public ApplicationPage getApplicationHomePage(final long applicationId) throws ApplicationPageNotFoundException {
        return getApplicationPageAPIDelegate().getApplicationHomePage(applicationId);
    }

    @Override
    public ApplicationMenu createApplicationMenu(final ApplicationMenuCreator applicationMenuCreator) throws CreationException {
        return getApplicationMenuAPIDelegate().createApplicationMenu(applicationMenuCreator);
    }

    @Override
    public ApplicationMenu updateApplicationMenu(final long applicationMenuId, final ApplicationMenuUpdater updater) throws ApplicationMenuNotFoundException,
            UpdateException {
        return getApplicationMenuAPIDelegate().updateApplicationMenu(applicationMenuId, updater);
    }

    @Override
    public ApplicationMenu getApplicationMenu(final long applicationMenuId) throws ApplicationMenuNotFoundException {
        return getApplicationMenuAPIDelegate().getApplicationMenu(applicationMenuId);
    }

    @Override
    public void deleteApplicationMenu(final long applicationMenuId) throws DeletionException {
        getApplicationMenuAPIDelegate().deleteApplicationMenu(applicationMenuId);
    }

    @Override
    public SearchResult<ApplicationMenu> searchApplicationMenus(final SearchOptions searchOptions) throws SearchException {
        return getApplicationMenuAPIDelegate(searchOptions).searchApplicationMenus();
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
        return getApplicationImporter(policy).importApplications(xmlContent, SessionInfos.getUserIdFromSession());
    }

}
