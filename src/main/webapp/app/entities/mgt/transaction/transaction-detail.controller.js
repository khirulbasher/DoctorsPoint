(function() {
    'use strict';

    angular
        .module('projectApp')
        .controller('TransactionDetailController', TransactionDetailController);

    TransactionDetailController.$inject = ['$scope', '$rootScope', 'entity'];

    function TransactionDetailController($scope, $rootScope, entity) {
        var vm = this;

        vm.transaction = entity;

        var unsubscribe = $rootScope.$on('projectApp:transactionUpdate', function(event, result) {
            vm.transaction = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
