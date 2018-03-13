(function() {
    'use strict';

    angular
        .module('projectApp')
        .controller('CashDeleteController',CashDeleteController);

    CashDeleteController.$inject = ['$uibModalInstance', 'entity', 'Cash'];

    function CashDeleteController($uibModalInstance, entity, Cash) {
        var vm = this;

        vm.cash = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Cash.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
