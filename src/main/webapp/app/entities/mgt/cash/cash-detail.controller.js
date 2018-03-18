(function() {
    'use strict';

    angular
        .module('projectApp')
        .controller('CashDetailController', CashDetailController);

    CashDetailController.$inject = ['entity'];

    function CashDetailController(entity) {
        var vm = this;

        vm.cash = entity;
    }
})();
