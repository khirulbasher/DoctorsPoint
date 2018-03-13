(function() {
    'use strict';
    angular
        .module('projectApp')
        .factory('Cash', Cash);

    Cash.$inject = ['$resource', 'DateUtils'];

    function Cash ($resource, DateUtils) {
        var resourceUrl =  'api/cash/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.lastTransactionDate = DateUtils.convertLocalDateFromServer(data.lastTransactionDate);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.lastTransactionDate = DateUtils.convertLocalDateToServer(copy.lastTransactionDate);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.lastTransactionDate = DateUtils.convertLocalDateToServer(copy.lastTransactionDate);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
