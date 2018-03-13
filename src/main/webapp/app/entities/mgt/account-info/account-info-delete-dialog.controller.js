(function() {
    'use strict';

    angular
        .module('projectApp')
        .controller('AccountInfoDeleteController',AccountInfoDeleteController);

    AccountInfoDeleteController.$inject = ['$uibModalInstance', 'entity', 'AccountInfo'];

    function AccountInfoDeleteController($uibModalInstance, entity, AccountInfo) {
        var vm = this;

        vm.accountInfo = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            AccountInfo.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
