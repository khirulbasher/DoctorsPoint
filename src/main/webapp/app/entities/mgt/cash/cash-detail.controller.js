(function() {
    'use strict';

    angular
        .module('projectApp')
        .controller('CashDetailController', CashDetailController);

    CashDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Cash', 'User', 'Transaction'];

    function CashDetailController($scope, $rootScope, $stateParams, previousState, entity, Cash, User, Transaction) {
        var vm = this;

        vm.cash = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('projectApp:cashUpdate', function(event, result) {
            vm.cash = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
