(function() {
    'use strict';

    angular
        .module('projectApp')
        .controller('DivisionDialogController', DivisionDialogController);

    DivisionDialogController.$inject = ['$timeout', '$scope', 'entity', 'Division', 'Country','$rootScope'];

    function DivisionDialogController ($timeout, $scope, entity, Division, Country,$rootScope) {
        var vm = this;

        vm.division = entity;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.countries = Country.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });


        function save () {
            vm.isSaving = true;
            if(vm.division.active == null) vm.division.active = true;
            if (vm.division.id != null) {
                Division.update(vm.division, onSaveSuccess, onSaveError);
            } else {
                Division.save(vm.division, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('projectApp:divisionUpdate', result);
            clear();
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        function clear () {
            $rootScope.back('division');
        }

        vm.datePickerOpenStatus.lastModifyDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
