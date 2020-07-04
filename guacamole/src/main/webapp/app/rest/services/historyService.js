/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

/**
 * Service for operating on history records via the REST API.
 */
angular.module('rest').factory('historyService', ['$injector',
        function historyService($injector) {

    // Required services
    var requestService        = $injector.get('requestService');
    var authenticationService = $injector.get('authenticationService');

    var service = {};

    /**
     * Makes a request to the REST API to get the usage history of all
     * accessible connections, returning a promise that provides the
     * corresponding array of @link{ConnectionHistoryEntry} objects if
     * successful.
     *
     * @param {String} dataSource
     *     The unique identifier of the data source containing the connection
     *     history records to be retrieved. This identifier corresponds to an
     *     AuthenticationProvider within the Guacamole web application.
     *
     * @param {String[]} [requiredContents]
     *     The set of arbitrary strings to filter with. A ConnectionHistoryEntry
     *     must contain each of these values within the associated username,
     *     connection name, start date, or end date to appear in the result. If
     *     null, no filtering will be performed.
     *
     * @param {String[]} [sortPredicates]
     *     The set of predicates to sort against. The resulting array of
     *     ConnectionHistoryEntry objects will be sorted according to the
     *     properties and sort orders defined by each predicate. If null, the
     *     order of the resulting entries is undefined. Valid values are listed
     *     within ConnectionHistoryEntry.SortPredicate.
     *
     * @returns {Promise.<ConnectionHistoryEntry[]>}
     *     A promise which will resolve with an array of
     *     @link{ConnectionHistoryEntry} objects upon success.
     */
    service.getAllConnectionHistory = function getAllConnectionHistory(dataSource,
        requiredContents, sortPredicates) {

        // Build HTTP parameters set
        var httpParameters = {
            token : authenticationService.getCurrentToken()
        };

        // Filter according to contents if restrictions are specified
        if (requiredContents)
            httpParameters.contains = requiredContents;

        // Sort according to provided predicates, if any
        if (sortPredicates)
            httpParameters.order = sortPredicates;

        // Retrieve connection history
        return requestService({
            method  : 'GET',
            url     : 'api/session/data/' + encodeURIComponent(dataSource) + '/history/connections',
            params  : httpParameters
        });

    };
    
    /**
     * Makes a request to the REST API to get the usage history of the
     * connection specified by the provided identifier, returning a promise that
     * provides the corresponding array of @link{ConnectionHistoryEntry} objects
     * if successful.
     *
     * @param {String} dataSource
     *     The unique identifier of the data source containing the connection
     *     history records to be retrieved. This identifier corresponds to an
     *     AuthenticationProvider within the Guacamole web application.
     *
     * @param {String} identifier
     *     The connection identifier for which to retrieve history.  This
     *     identifier corresponds to the unique identifier of the connection
     *     within the specified dataSource.
     * 
     * @param {String[]} [requiredContents]
     *     The set of arbitrary strings to filter with. A ConnectionHistoryEntry
     *     must contain each of these values within the associated username,
     *     connection name, start date, or end date to appear in the result. If
     *     null, no filtering will be performed.
     *
     * @param {String[]} [sortPredicates]
     *     The set of predicates to sort against. The resulting array of
     *     ConnectionHistoryEntry objects will be sorted according to the
     *     properties and sort orders defined by each predicate. If null, the
     *     order of the resulting entries is undefined. Valid values are listed
     *     within ConnectionHistoryEntry.SortPredicate.
     *
     * @returns {Promise.<ConnectionHistoryEntry[]>}
     *     A promise which will resolve with an array of
     *     @link{ConnectionHistoryEntry} objects upon success.
     */
    service.getConnectionHistory = function getConnectionHistory(dataSource,
        identifier, requiredContents, sortPredicates) {

        // Build HTTP parameters set
        var httpParameters = {
            token : authenticationService.getCurrentToken()
        };

        // Filter according to contents if restrictions are specified
        if (requiredContents)
            httpParameters.contains = requiredContents;

        // Sort according to provided predicates, if any
        if (sortPredicates)
            httpParameters.order = sortPredicates;

        // Retrieve connection history
        return requestService({
            method  : 'GET',
            url     : 'api/session/data/' + encodeURIComponent(dataSource) + '/history/connections/' + encodeURIComponent(identifier),
            params  : httpParameters
        });

    };
    
    /**
     * Makes a request to the REST API to get the activity history records
     * for all accessible users, returning a promise that provides the
     * corresponding array of @link{ActivityRecordEntry} objects if successful.
     *
     * @param {String} dataSource
     *     The unique identifier of the data source containing the user activity
     *     records to be retrieved. This identifier corresponds to an
     *     AuthenticationProvider within the Guacamole web application.
     * 
     * @param {String[]} [requiredContents]
     *     The set of arbitrary strings to filter with. An ActivityRecordEntry
     *     must contain each of these values within the associated username,
     *     connection name, start date, or end date to appear in the result. If
     *     null, no filtering will be performed.
     *
     * @param {String[]} [sortPredicates]
     *     The set of predicates to sort against. The resulting array of
     *     ConnectionHistoryEntry objects will be sorted according to the
     *     properties and sort orders defined by each predicate. If null, the
     *     order of the resulting entries is undefined. Valid values are listed
     *     within ConnectionHistoryEntry.SortPredicate.
     *
     * @returns {Promise.<ActivityRecordEntry[]>}
     *     A promise which will resolve with an array of
     *     @link{ActivityRecordEntry} objects upon success.
     */
    service.getAllUserActivity = function getAllUserActivity(dataSource,
        requiredContents, sortPredicates) {

        // Build HTTP parameters set
        var httpParameters = {
            token : authenticationService.getCurrentToken()
        };

        // Filter according to contents if restrictions are specified
        if (requiredContents)
            httpParameters.contains = requiredContents;

        // Sort according to provided predicates, if any
        if (sortPredicates)
            httpParameters.order = sortPredicates;

        // Retrieve user activity
        return requestService({
            method  : 'GET',
            url     : 'api/session/data/' + encodeURIComponent(dataSource) + '/history/users',
            params  : httpParameters
        });

    };
    
    /**
     * Makes a request to the REST API to get the activity history of the
     * user specified by the identifier, returning a promise that provides
     * the corresponding array of @link{ActivityRecordEntry} objects if
     * successful.
     *
     * @param {String} dataSource
     *     The unique identifier of the data source containing the user activity
     *     records to be retrieved. This identifier corresponds to an
     *     AuthenticationProvider within the Guacamole web application.
     *     
     * @param {String} identifier
     *     The unique identifier of the user within the provided data source
     *     for which to retrieve activity records.
     * 
     * @param {String[]} [requiredContents]
     *     The set of arbitrary strings to filter with. An ActivityRecordEntry
     *     must contain each of these values within the associated username,
     *     connection name, start date, or end date to appear in the result. If
     *     null, no filtering will be performed.
     *
     * @param {String[]} [sortPredicates]
     *     The set of predicates to sort against. The resulting array of
     *     ConnectionHistoryEntry objects will be sorted according to the
     *     properties and sort orders defined by each predicate. If null, the
     *     order of the resulting entries is undefined. Valid values are listed
     *     within ConnectionHistoryEntry.SortPredicate.
     *
     * @returns {Promise.<ActivityRecordEntry[]>}
     *     A promise which will resolve with an array of
     *     @link{ActivityRecordEntry} objects upon success.
     */
    service.getUserActivity = function getUserActivity(dataSource,
        identifier, requiredContents, sortPredicates) {

        // Build HTTP parameters set
        var httpParameters = {
            token : authenticationService.getCurrentToken()
        };

        // Filter according to contents if restrictions are specified
        if (requiredContents)
            httpParameters.contains = requiredContents;

        // Sort according to provided predicates, if any
        if (sortPredicates)
            httpParameters.order = sortPredicates;

        // Retrieve user activity
        return requestService({
            method  : 'GET',
            url     : 'api/session/data/' + encodeURIComponent(dataSource) + '/history/users/' + encodeURIComponent(identifier),
            params  : httpParameters
        });

    };

    return service;

}]);
