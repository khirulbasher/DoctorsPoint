(function() {
    'use strict';

    angular
        .module('projectApp')
        .controller('ThanaDialogController', ThanaDialogController);

    ThanaDialogController.$inject = ['$timeout', '$scope', 'entity', 'Thana', 'District','$state','CustomQuery'];

    function ThanaDialogController ($timeout, $scope, entity, Thana, District,$state,CustomQuery) {
        var vm = this;

        vm.thana = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        CustomQuery.query({table:'country',cols:'id,name',where:"NULL"},function (results) {
            vm.countries=results;
        });

        $scope.loadDivision = function (id) {
            if(id==null) return;
            CustomQuery.query({table:'division',cols:'id,name',where:'country_id = '+id},function (results) {
                vm.divisions = results;
            });
        };

        $scope.loadDistrict = function (id) {
            if(id==null) return;
            CustomQuery.query({table:'district',cols:'id,name',where:'division_id = '+id},function (results) {
                vm.districts = results;
            });
        };

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        if(entity!==null) {
            $scope.division=entity.district.division;
            $scope.country=$scope.division.country;
            $scope.loadDivision($scope.country.id);
            $scope.loadDistrict($scope.division.id);
        };

        function clear () {
            $state.go('thana',{},{reload:false});
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
