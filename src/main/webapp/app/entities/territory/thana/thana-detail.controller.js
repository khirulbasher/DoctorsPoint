(function() {
    'use strict';

    angular
        .module('projectApp')
        .controller('ThanaDetailController', ThanaDetailController);

    ThanaDetailController.$inject = ['entity'];

    function ThanaDetailController(entity) {
        var vm = this;
        vm.thana = entity;
    }
})();
