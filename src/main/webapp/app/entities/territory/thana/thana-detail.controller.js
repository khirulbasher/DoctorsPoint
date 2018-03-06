(function() {
    'use strict';

    angular
        .module('projectApp')
        .controller('ThanaDetailController', ThanaDetailController);

    ThanaDetailController.$inject = ['$scope','entity'];

    function ThanaDetailController($scope, entity) {
        var vm = this;
        vm.thana = entity;
    }
})();
