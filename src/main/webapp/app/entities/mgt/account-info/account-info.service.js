(function() {
    'use strict';
    angular
        .module('projectApp')
        .factory('AccountInfo', AccountInfo);

    AccountInfo.$inject = ['$resource', 'DateUtils'];

    function AccountInfo ($resource, DateUtils) {
        var resourceUrl =  'api/account-infos/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.lastModifyDate = DateUtils.convertLocalDateFromServer(data.lastModifyDate);
                        data.birthDate = DateUtils.convertLocalDateFromServer(data.birthDate);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.lastModifyDate = DateUtils.convertLocalDateToServer(copy.lastModifyDate);
                    copy.birthDate = DateUtils.convertLocalDateToServer(copy.birthDate);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.lastModifyDate = DateUtils.convertLocalDateToServer(copy.lastModifyDate);
                    copy.birthDate = DateUtils.convertLocalDateToServer(copy.birthDate);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
