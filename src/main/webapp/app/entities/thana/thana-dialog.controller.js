(function() {
    'use strict';

    angular
        .module('projectApp')
        .controller('ThanaDialogController', ThanaDialogController);

    ThanaDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Thana', 'District'];

    function ThanaDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Thana, District) {
        var vm = this;

        vm.thana = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.districts = District.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.thana.id !== null) {
                Thana.update(vm.thana, onSaveSuccess, onSaveError);
            } else {
                Thana.save(vm.thana, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('projectApp:thanaUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.lastModifyDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
