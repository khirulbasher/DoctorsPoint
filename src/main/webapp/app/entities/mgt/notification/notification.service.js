(function() {
    'use strict';
    angular
        .module('projectApp')
        .factory('Notification', Notification);

    Notification.$inject = ['$resource', 'DateUtils'];

    function Notification ($resource, DateUtils) {
        var resourceUrl =  'api/notifications/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.lastModifyDate = DateUtils.convertLocalDateFromServer(data.lastModifyDate);
                        data.occurDate = DateUtils.convertLocalDateFromServer(data.occurDate);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.lastModifyDate = DateUtils.convertLocalDateToServer(copy.lastModifyDate);
                    copy.occurDate = DateUtils.convertLocalDateToServer(copy.occurDate);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.lastModifyDate = DateUtils.convertLocalDateToServer(copy.lastModifyDate);
                    copy.occurDate = DateUtils.convertLocalDateToServer(copy.occurDate);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
