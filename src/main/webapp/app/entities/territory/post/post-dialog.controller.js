(function() {
    'use strict';

    angular
        .module('projectApp')
        .controller('PostDialogController', PostDialogController);

    PostDialogController.$inject = ['$timeout', '$scope', 'entity', 'Post', 'CustomQuery','$state'];

    function PostDialogController ($timeout, $scope, entity, Post, CustomQuery,$state) {
        var vm = this;

        vm.post = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.thanas =[];
        vm.countries=[];
        vm.districts=[];
        vm.divisions=[];

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

        $scope.loadThana = function (id) {
            CustomQuery.query({table:'thana',cols:'id,name',where:'district_id = '+id},function (results) {
                vm.thanas = results;
            });
        };

        if(entity!=null) {
            vm.post.thana= entity.thana;
            $scope.district= entity.thana.district;
            $scope.division = $scope.district.division;
            $scope.country = $scope.division.country;

            $scope.loadDivision($scope.country.id);
            $scope.loadDistrict($scope.division.id);
            $scope.loadThana($scope.district.id);
        }

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $state.go('post',{},{reload:false});
        }

        function save () {
            vm.isSaving = true;
            if(vm.post.active == null) vm.post.active = true;
            if (vm.post.id !== null) {
                Post.update(vm.post, onSaveSuccess, onSaveError);
            } else {
                Post.save(vm.post, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('projectApp:postUpdate', result);
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
