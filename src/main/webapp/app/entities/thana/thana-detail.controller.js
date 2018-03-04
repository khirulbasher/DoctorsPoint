(function() {
    'use strict';

    angular
        .module('projectApp')
        .controller('ThanaDetailController', ThanaDetailController);

    ThanaDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Thana', 'District'];

    function ThanaDetailController($scope, $rootScope, $stateParams, previousState, entity, Thana, District) {
        var vm = this;

        vm.thana = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('projectApp:thanaUpdate', function(event, result) {
            vm.thana = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
