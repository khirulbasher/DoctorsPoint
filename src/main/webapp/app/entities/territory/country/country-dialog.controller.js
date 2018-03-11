(function() {
    'use strict';

    angular
        .module('projectApp')
        .controller('CountryDialogController', CountryDialogController);

    CountryDialogController.$inject = ['$timeout', '$scope', 'entity', 'Country','$state'];

    function CountryDialogController ($timeout, $scope, entity, Country,$state) {
        var vm = this;

        vm.country = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $state.go('country',{reload:false});
        }

        function save () {
            vm.isSaving = true;
            if(vm.country.active == null) vm.country.active = true;
            if (vm.country.id != null) {
                Country.update(vm.country, onSaveSuccess, onSaveError);
            } else {
                Country.save(vm.country, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('projectApp:countryUpdate', result);
            clear();
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
