(function () {
    'use strict';

    angular
        .module('projectApp')
        .factory('CustomQuery', CustomQuery)
        .factory('FindOne',FindOne);

    CustomQuery.$inject = ['$resource'];
    FindOne.$inject = ['$resource'];

    function CustomQuery($resource) {
        var service = $resource('out/utility/custom_query/:table/:cols/:where', {}, {
            'query': {method:'GET',isArray:true}
        });

        return service;
    };
    function FindOne($resource) {
        var service = $resource('out/utility/findOne/:table/:where', {}, {
            'query': {method:'GET',isArray:true}
        });

        return service;
    };

})();
