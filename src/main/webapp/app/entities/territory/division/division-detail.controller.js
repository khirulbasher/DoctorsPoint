(function() {
    'use strict';

    angular
        .module('projectApp')
        .controller('DivisionDetailController', DivisionDetailController);

    DivisionDetailController.$inject = ['entity'];

    function DivisionDetailController(entity) {
        var vm = this;

        vm.division = entity;
    }
})();
