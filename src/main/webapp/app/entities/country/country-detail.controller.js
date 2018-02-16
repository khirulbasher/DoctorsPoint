(function() {
    'use strict';

    angular
        .module('projectApp')
        .controller('CountryDetailController', CountryDetailController);

    CountryDetailController.$inject = ['$scope', '$state', 'entity'];

    function CountryDetailController($scope, $state, entity) {
        var vm = this;
        vm.country = entity;
        vm.cancel = function() {
            $state.go('country',{},{reload:false});
        };
    }
})();
