(function() {
    'use strict';

    angular
        .module('projectApp')
        .controller('DistrictDialogController', DistrictDialogController);

    DistrictDialogController.$inject = ['$timeout', '$scope', 'entity', 'District','$rootScope','CustomQuery'];

    function DistrictDialogController ($timeout, $scope, entity, District,$rootScope,CustomQuery) {
        var vm = this;

        vm.district = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.countries=[];
        vm.divisions=[];
        CustomQuery.query({table:'country',cols:'id,name',where:"NULL"},function (results) {
            vm.countries=results;
        });

        $scope.loadDivision = function (id) {

            CustomQuery.query({table:'division',cols:'id,name',where:'country_id = '+id},function (results) {
                vm.divisions = results;
            });
        };
        if(entity!==null) {
            var id=entity.division.country.id;
            $scope.country = {
                'id': id,
                'name': entity.division.country.name
            };
            $scope.loadDivision(id);
        }

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $rootScope.back('district');
        }

        function save () {
            vm.isSaving = true;
            if(vm.district.active == null) vm.district.active = true;
            if (vm.district.id != null) {
                District.update(vm.district, onSaveSuccess, onSaveError);
            } else {
                District.save(vm.district, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('projectApp:districtUpdate', result);
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
