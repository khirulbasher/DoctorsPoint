(function() {
    'use strict';

    angular
        .module('projectApp')
        .controller('AccountInfoDialogController', AccountInfoDialogController);

    AccountInfoDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'AccountInfo', 'Thana', 'Post'];

    function AccountInfoDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, AccountInfo, Thana, Post) {
        var vm = this;

        vm.accountInfo = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.thanas = Thana.query();
        vm.posts = Post.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.accountInfo.id !== null) {
                AccountInfo.update(vm.accountInfo, onSaveSuccess, onSaveError);
            } else {
                AccountInfo.save(vm.accountInfo, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('projectApp:accountInfoUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.lastModifyDate = false;
        vm.datePickerOpenStatus.birthDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
