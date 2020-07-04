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
 * Service which defines the ActivityRecordEntry class.
 */
angular.module('rest').factory('ActivityRecordEntry',
        [function defineActivityRecordEntry() {
            
    /**
     * The object returned by REST API calls when representing the data
     * associated with an activity record entry in a user's history. Each
     * activity entry represents the time at which a particular user activity
     * began, and, if applicable, the time the activity ended.
     * 
     * @constructor
     * @param {ActivityRecordEntry|Object} [template={}]
     *     The object whose properties should be copied within the new
     *     ActivityRecordEntry.
     */
    var ActivityRecordEntry = function ActivityRecordEntry(template) {

        // Use empty object by default
        template = template || {};

        /**
         * The date and time the activity began.
         *
         * @type Date
         */
        this.startDate = template.startDate;

        /**
         * The date and time the activity ended, or null if the activity is
         * still in progress or if the end time is unknown.
         *
         * @type Date
         */
        this.endDate = template.endDate;

        /**
         * The hostname or IP address of the remote host that performed the
         * activity associated with this record, if known.
         *
         * @type String
         */
        this.remoteHost = template.remoteHost;

        /**
         * The name of the user who performed or is performing the activity
         * associated with this record.
         *
         * @type String 
         */
        this.username = template.username;

        /**
         * Whether the activity is still in progress.
         * 
         * @type Boolean
         */
        this.active = template.active;

    };

    /**
     * All possible predicates for sorting ActiveRecordEntry objects using
     * the REST API. By default, each predicate indicates ascending order. To
     * indicate descending order, add "-" to the beginning of the predicate.
     *
     * @type Object.<String, String>
     */
    ActivityRecordEntry.SortPredicate = {

        /**
         * The date and time that the activity record began.
         */
        START_DATE : 'startDate'

    };

    return ActivityRecordEntry;

}]);
