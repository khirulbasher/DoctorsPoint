(function() {
    'use strict';

    angular
        .module('projectApp')
        .controller('CashDialogController', CashDialogController);

    CashDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Cash', 'User', 'Transaction'];

    function CashDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Cash, User, Transaction) {
        var vm = this;

        vm.cash = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.users = User.query({filter: 'cash-is-null'});
        $q.all([vm.cash.$promise, vm.users.$promise]).then(function() {
            if (!vm.cash.user || !vm.cash.user.id) {
                return $q.reject();
            }
            return User.get({id : vm.cash.user.id}).$promise;
        }).then(function(user) {
            vm.users.push(user);
        });
        vm.lasttransactionids = Transaction.query({filter: 'cash-is-null'});
        $q.all([vm.cash.$promise, vm.lasttransactionids.$promise]).then(function() {
            if (!vm.cash.lastTransactionId || !vm.cash.lastTransactionId.id) {
                return $q.reject();
            }
            return Transaction.get({id : vm.cash.lastTransactionId.id}).$promise;
        }).then(function(lastTransactionId) {
            vm.lasttransactionids.push(lastTransactionId);
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.cash.id !== null) {
                Cash.update(vm.cash, onSaveSuccess, onSaveError);
            } else {
                Cash.save(vm.cash, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('projectApp:cashUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.lastTransactionDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
