(function() {
    'use strict';

    angular
        .module('projectApp')
        .controller('TransactionDialogController', TransactionDialogController);

    TransactionDialogController.$inject = ['$timeout', '$scope', 'entity', 'Transaction', 'User', '$state'];

    function TransactionDialogController ($timeout, $scope, entity, Transaction, User,$state) {
        var vm = this;

        vm.transaction = entity;
        vm.clear = clear;
        vm.save = save;
        vm.users = User.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $state.go('transaction',{},{reload:false});
        }

        function save () {
            vm.isSaving = true;
            if (vm.transaction.id !== null) {
                Transaction.update(vm.transaction, onSaveSuccess, onSaveError);
            } else {
                Transaction.save(vm.transaction, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('projectApp:transactionUpdate', result);
            clear();
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
