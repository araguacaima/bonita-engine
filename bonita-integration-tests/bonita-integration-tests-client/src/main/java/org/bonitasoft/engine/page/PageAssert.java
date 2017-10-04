package org.bonitasoft.engine.page;

import static java.lang.String.format;

import java.util.Date;

import org.assertj.core.api.AbstractAssert;

/**
 * {@link Page} specific assertions - Generated by CustomAssertionGenerator.
 */
public class PageAssert extends AbstractAssert<PageAssert, Page> {

    /**
     * Creates a new </code>{@link PageAssert}</code> to make assertions on actual Page.
     * 
     * @param actual the Page we want to make assertions on.
     */
    public PageAssert(Page actual) {
        super(actual, PageAssert.class);
    }

    /**
     * An entry point for PageAssert to follow AssertJ standard <code>assertThat()</code> statements.<br>
     * With a static import, one's can write directly : <code>assertThat(myPage)</code> and get specific assertion with code completion.
     * 
     * @param actual the Page we want to make assertions on.
     * @return a new </code>{@link PageAssert}</code>
     */
    public static PageAssert assertThat(Page actual) {
        return new PageAssert(actual);
    }

    /**
     * Verifies that the actual Page's contentName is equal to the given one.
     * 
     * @param contentName the given contentName to compare the actual Page's contentName to.
     * @return this assertion object.
     * @throws AssertionError - if the actual Page's contentName is not equal to the given one.
     */
    public PageAssert hasContentName(String contentName) {
        // check that actual Page we want to make assertions on is not null.
        isNotNull();

        // we overrides the default error message with a more explicit one
        String errorMessage = format("\nExpected <%s> contentName to be:\n  <%s>\n but was:\n  <%s>", actual, contentName, actual.getContentName());

        // check
        if (!actual.getContentName().equals(contentName)) {
            throw new AssertionError(errorMessage);
        }

        // return the current assertion for method chaining
        return this;
    }

    /**
     * Verifies that the actual Page's contentType is equal to the given one.
     * 
     * @param contentType the given contentType to compare the actual Page's contentType to.
     * @return this assertion object.
     * @throws AssertionError - if the actual Page's contentType is not equal to the given one.
     */
    public PageAssert hasContentType(String contentType) {
        // check that actual Page we want to make assertions on is not null.
        isNotNull();

        // we overrides the default error message with a more explicit one
        String errorMessage = format("\nExpected <%s> contentType to be:\n  <%s>\n but was:\n  <%s>", actual, contentType, actual.getContentType());

        // check
        if (!actual.getContentType().equals(contentType)) {
            throw new AssertionError(errorMessage);
        }

        // return the current assertion for method chaining
        return this;
    }

    /**
     * Verifies that the actual Page's description is equal to the given one.
     * 
     * @param description the given description to compare the actual Page's description to.
     * @return this assertion object.
     * @throws AssertionError - if the actual Page's description is not equal to the given one.
     */
    public PageAssert hasDescription(String description) {
        // check that actual Page we want to make assertions on is not null.
        isNotNull();

        // we overrides the default error message with a more explicit one
        String errorMessage = format("\nExpected <%s> description to be:\n  <%s>\n but was:\n  <%s>", actual, description, actual.getDescription());

        // check
        if (!actual.getDescription().equals(description)) {
            throw new AssertionError(errorMessage);
        }

        // return the current assertion for method chaining
        return this;
    }

    /**
     * Verifies that the actual Page's displayName is equal to the given one.
     * 
     * @param displayName the given displayName to compare the actual Page's displayName to.
     * @return this assertion object.
     * @throws AssertionError - if the actual Page's displayName is not equal to the given one.
     */
    public PageAssert hasDisplayName(String displayName) {
        // check that actual Page we want to make assertions on is not null.
        isNotNull();

        // we overrides the default error message with a more explicit one
        String errorMessage = format("\nExpected <%s> displayName to be:\n  <%s>\n but was:\n  <%s>", actual, displayName, actual.getDisplayName());

        // check
        if (!actual.getDisplayName().equals(displayName)) {
            throw new AssertionError(errorMessage);
        }

        // return the current assertion for method chaining
        return this;
    }

    /**
     * Verifies that the actual Page's installationDate is equal to the given one.
     * 
     * @param installationDate the given installationDate to compare the actual Page's installationDate to.
     * @return this assertion object.
     * @throws AssertionError - if the actual Page's installationDate is not equal to the given one.
     */
    public PageAssert hasInstallationDate(Date installationDate) {
        // check that actual Page we want to make assertions on is not null.
        isNotNull();

        // we overrides the default error message with a more explicit one
        String errorMessage = format("\nExpected <%s> installationDate to be:\n  <%s>\n but was:\n  <%s>", actual, installationDate,
                actual.getInstallationDate());

        // check
        if (!actual.getInstallationDate().equals(installationDate)) {
            throw new AssertionError(errorMessage);
        }

        // return the current assertion for method chaining
        return this;
    }

    /**
     * Verifies that the actual Page's installedBy is equal to the given one.
     * 
     * @param installedBy the given installedBy to compare the actual Page's installedBy to.
     * @return this assertion object.
     * @throws AssertionError - if the actual Page's installedBy is not equal to the given one.
     */
    public PageAssert hasInstalledBy(long installedBy) {
        // check that actual Page we want to make assertions on is not null.
        isNotNull();

        // we overrides the default error message with a more explicit one
        String errorMessage = format("\nExpected <%s> installedBy to be:\n  <%s>\n but was:\n  <%s>", actual, installedBy, actual.getInstalledBy());

        // check
        if (actual.getInstalledBy() != installedBy) {
            throw new AssertionError(errorMessage);
        }

        // return the current assertion for method chaining
        return this;
    }

    /**
     * Verifies that the actual Page's lastUpdatedBy is equal to the given one.
     * 
     * @param lastUpdatedBy the given lastUpdatedBy to compare the actual Page's lastUpdatedBy to.
     * @return this assertion object.
     * @throws AssertionError - if the actual Page's lastUpdatedBy is not equal to the given one.
     */
    public PageAssert hasLastUpdatedBy(long lastUpdatedBy) {
        // check that actual Page we want to make assertions on is not null.
        isNotNull();

        // we overrides the default error message with a more explicit one
        String errorMessage = format("\nExpected <%s> lastUpdatedBy to be:\n  <%s>\n but was:\n  <%s>", actual, lastUpdatedBy, actual.getLastUpdatedBy());

        // check
        if (actual.getLastUpdatedBy() != lastUpdatedBy) {
            throw new AssertionError(errorMessage);
        }

        // return the current assertion for method chaining
        return this;
    }

    /**
     * Verifies that the actual Page's name is equal to the given one.
     * 
     * @param name the given name to compare the actual Page's name to.
     * @return this assertion object.
     * @throws AssertionError - if the actual Page's name is not equal to the given one.
     */
    public PageAssert hasName(String name) {
        // check that actual Page we want to make assertions on is not null.
        isNotNull();

        // we overrides the default error message with a more explicit one
        String errorMessage = format("\nExpected <%s> name to be:\n  <%s>\n but was:\n  <%s>", actual, name, actual.getName());

        // check
        if (!actual.getName().equals(name)) {
            throw new AssertionError(errorMessage);
        }

        // return the current assertion for method chaining
        return this;
    }

    /**
     * Verifies that the actual Page's processDefinitionId is equal to the given one.
     * 
     * @param processDefinitionId the given processDefinitionId to compare the actual Page's processDefinitionId to.
     * @return this assertion object.
     * @throws AssertionError - if the actual Page's processDefinitionId is not equal to the given one.
     */
    public PageAssert hasProcessDefinitionId(Long processDefinitionId) {
        // check that actual Page we want to make assertions on is not null.
        isNotNull();

        // we overrides the default error message with a more explicit one
        String errorMessage = format("\nExpected <%s> processDefinitionId to be:\n  <%s>\n but was:\n  <%s>", actual, processDefinitionId,
                actual.getProcessDefinitionId());

        // check
        if (!actual.getProcessDefinitionId().equals(processDefinitionId)) {
            throw new AssertionError(errorMessage);
        }

        // return the current assertion for method chaining
        return this;
    }

}
