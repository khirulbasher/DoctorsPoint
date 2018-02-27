(function() {
    'use strict';
    angular
        .module('projectApp')
        .factory('District', District);

    District.$inject = ['$resource', 'DateUtils'];

    function District ($resource, DateUtils) {
        var resourceUrl =  'api/districts/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.lastModifyDate = DateUtils.convertLocalDateFromServer(data.lastModifyDate);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.lastModifyDate = DateUtils.convertLocalDateToServer(copy.lastModifyDate);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.lastModifyDate = DateUtils.convertLocalDateToServer(copy.lastModifyDate);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
