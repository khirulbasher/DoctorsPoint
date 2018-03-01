(function () {
    'use strict';

    angular
        .module('projectApp')
        .factory('User', User)
        .factory('UserService', UserService);

    User.$inject = ['$resource'];
    UserService.$inject = ['$resource'];

    function User($resource) {
        var service = $resource('api/users/:login', {}, {
            'query': {method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'save': {method: 'POST'},
            'update': {method: 'PUT'},
            'delete': {method: 'DELETE'},
        });

        return service;
    }

    function UserService($resource) {
        var service = $resource('api/userService/:id', {}, {
            'get': {method: 'GET', isArray: false},
            'getByMapping':{ method:'POST', isArray:true }
        });
        return service;
    }
})();
