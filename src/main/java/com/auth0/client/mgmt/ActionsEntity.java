package com.auth0.client.mgmt;

import com.auth0.json.mgmt.actions.Action;
import com.auth0.json.mgmt.actions.Triggers;
import com.auth0.json.mgmt.actions.Version;
import com.auth0.net.CustomRequest;
import com.auth0.net.EmptyBodyRequest;
import com.auth0.net.Request;
import com.auth0.net.VoidRequest;
import com.auth0.utils.Asserts;
import com.fasterxml.jackson.core.type.TypeReference;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

public class ActionsEntity extends BaseManagementEntity {

    private final static String ACTIONS_BASE_PATH = "api/v2/actions";
    private final static String ACTIONS_PATH = "actions";
    private final static String TRIGGERS_PATH = "triggers";
    private final static String DEPLOY_PATH = "deploy";
    private final static String VERSIONS_PATH = "versions";
    private final static String AUTHORIZATION_HEADER = "Authorization";

    ActionsEntity(OkHttpClient client, HttpUrl baseUrl, String apiToken) {
        super(client, baseUrl, apiToken);
    }

    /**
     * Create an action. A token with {@code create:actions} scope is required.
     *
     * @param action the action to create
     * @return a request to execute
     *
     * @see <a href="https://auth0.com/docs/api/management/v2#!/Actions/post_action">https://auth0.com/docs/api/management/v2#!/Actions/post_action</a>
     */
    public Request<Action> create(Action action) {
        Asserts.assertNotNull(action, "action");

        HttpUrl.Builder builder = baseUrl
            .newBuilder()
            .addPathSegments(ACTIONS_BASE_PATH)
            .addPathSegment(ACTIONS_PATH);

        String url = builder.build().toString();

        CustomRequest<Action> request = new CustomRequest<>(client, url, "POST", new TypeReference<Action>() {
        });

        request.addHeader(AUTHORIZATION_HEADER, "Bearer " + apiToken);
        request.setBody(action);
        return request;
    }

    /**
     * Get an action. A token with {@code read:actions} scope is required.
     *
     * @param actionId the ID of the action to retrieve
     * @return a Request to execute
     *
     * @see <a href="https://auth0.com/docs/api/management/v2#!/Actions/get_action">https://auth0.com/docs/api/management/v2#!/Actions/get_action</a>
     */
    public Request<Action> get(String actionId) {
        Asserts.assertNotNull(actionId, "action ID");

        String url = baseUrl
            .newBuilder()
            .addPathSegments(ACTIONS_BASE_PATH)
            .addPathSegment(ACTIONS_PATH)
            .addPathSegment(actionId)
            .build()
            .toString();

        CustomRequest<Action> request = new CustomRequest<>(client, url, "GET", new TypeReference<Action>() {
        });

        request.addHeader(AUTHORIZATION_HEADER, "Bearer " + apiToken);
        return request;
    }

    /**
     * Delete an action and all of its associated versions. An action must be unbound from all triggers before it can
     * be deleted. A token with {@code delete:action} scope is required.
     *
     * @param actionId the ID of the action to delete.
     * @return a request to execute.
     *
     * @see <a href="https://auth0.com/docs/api/management/v2#!/Actions/delete_action">https://auth0.com/docs/api/management/v2#!/Actions/delete_action</a>
     */
    public Request delete(String actionId) {
        return delete(actionId, false);
    }

    /**
     * Delete an action and all of its associated versions. A token with {@code delete:action} scope is required.
     *
     * @param actionId the ID of the action to delete.
     * @param force whether to force the action deletion even if it is bound to triggers.
     * @return a request to execute.
     *
     * <a href="https://auth0.com/docs/api/management/v2#!/Actions/get_triggers">https://auth0.com/docs/api/management/v2#!/Actions/get_triggers</a>
     */
    public Request delete(String actionId, boolean force) {
        Asserts.assertNotNull(actionId, "action ID");

        String url = baseUrl
            .newBuilder()
            .addPathSegments(ACTIONS_BASE_PATH)
            .addPathSegment(ACTIONS_PATH)
            .addPathSegment(actionId)
            .addQueryParameter("force", String.valueOf(force))
            .build()
            .toString();

        VoidRequest voidRequest = new VoidRequest(client, url, "DELETE");
        voidRequest.addHeader(AUTHORIZATION_HEADER, "Bearer " + apiToken);
        return voidRequest;
    }

    /**
     * Get the set of triggers currently available. A trigger is an extensibility point to which actions can be bound.
     * A token with {@code read:actions} scope is required.
     *
     * @return a request to execute.
     */
    public Request<Triggers> getTriggers() {
        String url = baseUrl
            .newBuilder()
            .addPathSegments(ACTIONS_BASE_PATH)
            .addPathSegment(TRIGGERS_PATH)
            .build()
            .toString();

        CustomRequest<Triggers> request = new CustomRequest<>(client, url, "GET", new TypeReference<Triggers>() {
        });

        request.addHeader(AUTHORIZATION_HEADER, "Bearer " + apiToken);
        return request;
    }

    /**
     * Update an existing action. If this action is currently bound to a trigger, updating it will not affect any user
     * flows until the action is deployed. Requires a token with {@code update:actions} scope.
     *
     * @param actionId the ID of the action to update.
     * @param action the updated action.
     * @return a request to execute.
     *
     * @see <a href="https://auth0.com/docs/api/management/v2#!/Actions/patch_action">https://auth0.com/docs/api/management/v2#!/Actions/patch_action</a>
     */
    public Request<Action> update(String actionId, Action action) {
        Asserts.assertNotNull(actionId, "action ID");
        Asserts.assertNotNull(action, "action");

        String url = baseUrl
            .newBuilder()
            .addPathSegments(ACTIONS_BASE_PATH)
            .addPathSegment(ACTIONS_PATH)
            .addPathSegment(actionId)
            .build()
            .toString();

        CustomRequest<Action> request = new CustomRequest<>(client, url, "PATCH", new TypeReference<Action>() {
        });

        request.setBody(action);
        request.addHeader(AUTHORIZATION_HEADER, "Bearer " + apiToken);
        return request;
    }

    /**
     * Deploy an action. Deploying an action will create a new immutable version of the action. If the action is
     * currently bound to a trigger, then the system will begin executing the newly deployed version of the action
     * immediately. Otherwise, the action will only be executed as a part of a flow once it is bound to that flow.
     * Requires a token with {@code create:actions}.
     *
     * @param actionId the ID of the action to deploy.
     * @return a request to execute.
     *
     * @see <a href="https://auth0.com/docs/api/management/v2#!/Actions/post_deploy_action">https://auth0.com/docs/api/management/v2#!/Actions/post_deploy_action</a>
     */
    public Request<Version> deploy(String actionId) {
        Asserts.assertNotNull(actionId, "action ID");

        String url = baseUrl
            .newBuilder()
            .addPathSegments(ACTIONS_BASE_PATH)
            .addPathSegment(ACTIONS_PATH)
            .addPathSegment(actionId)
            .addPathSegment(DEPLOY_PATH)
            .build()
            .toString();

        EmptyBodyRequest<Version> request = new EmptyBodyRequest<>(client, url, "POST", new TypeReference<Version>() {
        });

        request.addHeader(AUTHORIZATION_HEADER, "Bearer " + apiToken);
        return request;
    }

    /**
     * Retrieve a specific version of an action. An action version is created whenever
     * an action is deployed. An action version is immutable, once created. Requires a token with {@code read:actions} scope.
     *
     * @param actionId the ID of the action for which to retrieve the version.
     * @param actionVersionId the ID of the specific version to retrieve.
     * @return a request to execute.
     *
     * @see <a href="https://auth0.com/docs/api/management/v2#!/Actions/get_action_version">https://auth0.com/docs/api/management/v2#!/Actions/get_action_version</a>
     */
    public Request<Version> getVersion(String actionId, String actionVersionId) {
        Asserts.assertNotNull(actionId, "action ID");
        Asserts.assertNotNull(actionVersionId, "action version ID");

        String url = baseUrl
            .newBuilder()
            .addPathSegments(ACTIONS_BASE_PATH)
            .addPathSegment(ACTIONS_PATH)
            .addPathSegment(actionId)
            .addPathSegment(VERSIONS_PATH)
            .addPathSegment(actionVersionId)
            .build()
            .toString();

        CustomRequest<Version> request = new CustomRequest<>(client, url, "GET", new TypeReference<Version>() {
        });

        request.addHeader(AUTHORIZATION_HEADER, "Bearer " + apiToken);
        return request;
    }

    // TODO GET actions

    // TODO GET action service status

    // TODO GET an execution

    // TODO GET an action's versions

    // TODO GET trigger bindings

    // TODO PATCH trigger bindings

    // TODO POST test an action

    // TODO POST roll back to previous action version
}
