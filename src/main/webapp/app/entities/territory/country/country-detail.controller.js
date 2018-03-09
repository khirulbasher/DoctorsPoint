(function() {
    'use strict';

    angular
        .module('projectApp')
        .controller('CountryDetailController', CountryDetailController);

    CountryDetailController.$inject = ['entity'];

    function CountryDetailController(entity) {
        var vm = this;
        vm.country = entity;
    }
})();
