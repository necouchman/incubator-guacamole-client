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
 * A directive which provides for intercepting context menu clicks and redirecting
 * them to open a custom context menu.
 */
angular.module('element').directive('guacContext', ['$document', function guacContext($document) {

    return {
        restrict: 'A',
        
        scope: {
            
            /**
             * Whether or not the context menu should be visible.
             */
            contextVisible: '='
            
        },

        link: function linkGuacContext($scope, $element, $attrs) {

            // Open context menu when event detected
            $element.bind('contextmenu', function contextMenuTriggered(event) {
                event.preventDefault();
                $scope.$apply(function openContextMenu() {
                    $scope.contextVisible = true;
                });
            });

            // Close context menu on other clicks.
            $(document).on('click', '*', function closeContextMenu(event) {
                $scope.$apply(function closeContextMenu() {
                    $scope.contextVisible = false;
                })
            })

        } // end guacConext link function

    };

}]);
