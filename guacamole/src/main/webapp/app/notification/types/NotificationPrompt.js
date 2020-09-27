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
 * Provides the NotificationAction class definition.
 */
angular.module('notification').factory('NotificationPrompt',
        [function defineNotificationAction() {

    /**
     * Creates a new NotificationPrompt, which contains one or more Forms that
     * the user is asked to fill out in response to a notification from
     * Guacamole.
     *
     * @constructor
     * @param {Form} form
     *     The form to display to the user which contains fields
     *     that the user needs to complete in response to the notification.
     *     
     * @param {Client} client
     *     The client that this prompt is associated with, if any.
     */
    var NotificationPrompt = function NotificationPrompt(forms, client) {

        /**
         * Reference to this NotificationPrompt.
         *
         * @type NotificationPrompt
         */
        var prompt = this;
        
        /**
         * The forms to display to the user with this prompt.
         */
        this.forms = forms;
        
        /**
         * The client associated with this prompt, if any.
         * 
         * @type Client
         */
        this.client = client;
        
        /**
         * Responses to the provided prompts that are entered by the user.
         */
        this.responses = {};
    };

    return NotificationPrompt;

}]);
