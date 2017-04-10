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
 * Service for displaying prompts for connection parameters
 */
angular.module('prompt').factory('guacPrompt', ['$injector',
        function guacPrompt($injector) {

    // Required services
    var $rootScope            = $injector.get('$rootScope');
    var sessionStorageFactory = $injector.get('sessionStorageFactory');

    var service = {};

    /**
     * Getter/setter which retrieves or sets the current set of prompts,
     * which may simply be false if no prompt is currently shown.
     * 
     * @type Function
     */
    var storedPrompt = sessionStorageFactory.create(false);

    /**
     * Retrieves the current prompt, which may simply be false if
     * no prompt is currently shown.
     * 
     * @type Notification|Boolean
     */
    service.getPrompt = function getPrompt() {
        console.log('Returning storedPrompt()');
        return storedPrompt();
    };

    /**
     * Shows or hides the given prompt as a modal dialog. If a prompt
     * is currently shown, no further prompts will be shown
     * until the current prompt is hidden.
     *
     * @param {Notification|Boolean|Object} prompt
     *     The prompt notification to show.
     *
     * @example
     * 
     * // To show a prompt message with actions
     * guacPrompt.showPrompt({
     *     'title'      : 'Disconnected',
     *     'text'       : {
     *         'key' : 'NAMESPACE.SOME_TRANSLATION_KEY'
     *     },
     *     'actions'    : {
     *         'name'       : 'reconnect',
     *         'callback'   : function () {
     *             // Reconnection code goes here
     *         }
     *     }
     * });
     * 
     * // To hide the prompt message
     * guacPrompt.showPrompt(false);
     */
    service.showPrompt = function showPrompt(prompt) {
        console.log(prompt);
        if (!storedPrompt() || !prompt) {
            console.log('Storing the prompt.');
            storedPrompt(prompt);
        }
    };

    // Hide prompt upon navigation
    $rootScope.$on('$routeChangeSuccess', function() {
        console.log('Hiding prompt.');
        service.showPrompt(false);
    });

    return service;

}]);
