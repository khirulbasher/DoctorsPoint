(function() {
    'use strict';

    angular
        .module('projectApp')
        .controller('CashDialogController', CashDialogController);

    CashDialogController.$inject = ['$timeout', '$scope', 'entity', 'Cash','$state'];

    function CashDialogController ($timeout, $scope, entity, Cash,$state) {
        var vm = this;

        vm.cash = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $state.go('cash',{},{reload:false});
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
            clear();
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }
    }
})();
