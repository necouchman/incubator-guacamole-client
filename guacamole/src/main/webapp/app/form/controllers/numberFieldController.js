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
 * Controller for number fields.
 */
angular.module('form').controller('numberFieldController', ['$scope',
    function numberFieldController($scope) {

    /**
     * This is a regular expression which matches the format for parameter
     * tokens used throughout the client, e.g. ${GUAC_TOKEN}.
     * 
     * @type {RegExp}
     */
    var tokenRegex = /'^\$\{[A-Z_]+\}$'/;

    /**
     * This is the regular expression used by the number field to validate
     * that input is either numeric or a token.
     */
    $scope.numberPattern = /'^((\$\{[A-Z_]+\})|(\d+))*$'/;

    // Update typed value when model is changed
    $scope.$watch('model', function modelChanged(model) {
        
        // If we have a token, keep it.
        if (tokenRegex.test(model))
            $scope.typedValue = model;
        
        // If we have a number, we keep that, too.
        else if (Number(model) || Number(model) === 0)
            $scope.typedValue = model;
        
        // Anything else gets thrown away
        else
            $scope.typedValue = null;
    });

    // Update string value in model when typed value is changed
    $scope.$watch('typedValue', function typedValueChanged(typedValue) {
        
        // If we have a token, keep it.
        if (tokenRegex.test(typedValue))
            $scope.model = typedValue;
        
        // If we have a number, keep that
        else if (typedValue || typedValue === 0)
            $scope.model = typedValue.toString();
        
        // Throw anything else away
        else
            $scope.model = '';
        
    });

}]);
