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
 * A directive for prompting for connection parameters
 */
angular.module('prompt').directive('guacPrompt', [function guacPrompt() {

    // Prompt directive
    var directive = {
        restrict    : 'E',
        replace     : true,
        templateUrl : 'app/login/templates/prompt.html'
    };

    // Prompt directive scope
    directive.scope = {

        /**
         * An optional instructional message to display within the prompt
         * dialog.
         *
         * @type TranslatableMessage
         */
        helpText : '=',

        /**
         * The prompt form or set of fields. This will be displayed to the user
         * to capture input to pass on to the connection.
         *
         * @type Field[]
         */
        form : '=',

        /**
         * A map of all field name/value pairs that have already been provided.
         */
        values : '='

    };

    // Controller for prompt directive
    directive.controller = ['$scope', '$injector',
        function promptController($scope, $injector) {
        
        // Required types
        var Error = $injector.get('Error');
        var Field = $injector.get('Field');

        // Required services
        var $route                = $injector.get('$route');

        /**
         * All form values entered by the user, as parameter name/value pairs.
         *
         * @type Object.<String, String>
         */
        $scope.enteredValues = {};

        /**
         * All form fields which have not yet been filled by the user.
         *
         * @type Field[]
         */
        $scope.remainingFields = [];

        // Ensure provided values are included within entered values, even if
        // they have no corresponding input fields
        $scope.$watch('values', function resetEnteredValues(values) {
            angular.extend($scope.enteredValues, values || {});
        });

        // Update field information when form is changed
        $scope.$watch('form', function resetRemainingFields(fields) {

            // If no fields are provided, then no fields remain
            if (!fields) {
                $scope.remainingFields = [];
                return;
            }

            // Filter provided fields against provided values
            $scope.remainingFields = fields.filter(function isRemaining(field) {
                return !(field.name in $scope.values);
            });

            // Set default values for all unset fields
            angular.forEach($scope.remainingFields, function setDefault(field) {
                if (!$scope.enteredValues[field.name])
                    $scope.enteredValues[field.name] = '';
            });

        });

        /**
         * Submits the currently-specified parameter prompts to the connection
         * service, continuing the connection if successful.
         */
        $scope.prompt = function prompt() {

            // Attempt login once existing session is destroyed
            authenticationService.authenticate($scope.enteredValues)

            // Clear and reload upon success
            .then(function loginSuccessful() {
                $scope.enteredValues = {};
                $route.reload();
            })

            // Reset upon failure
            ['catch'](function loginFailed(error) {

                // Clear out passwords if the credentials were rejected for any reason
                if (error.type !== Error.Type.INSUFFICIENT_CREDENTIALS) {

                    // Flag generic error for invalid login
                    if (error.type === Error.Type.INVALID_CREDENTIALS)
                        $scope.loginError = {
                            'key' : 'LOGIN.ERROR_INVALID_LOGIN'
                        };

                    // Display error if anything else goes wrong
                    else
                        $scope.loginError = error.translatableMessage;

                    // Clear all visible password fields
                    angular.forEach($scope.remainingFields, function clearEnteredValueIfPassword(field) {

                        // Remove entered value only if field is a password field
                        if (field.type === Field.Type.PASSWORD && field.name in $scope.enteredValues)
                            $scope.enteredValues[field.name] = '';

                    });
                }

            });

        };

    }];

    return directive;

}]);
