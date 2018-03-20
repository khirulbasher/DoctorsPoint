(function() {
    'use strict';

    angular
        .module('projectApp')
        .controller('NotificationDialogController', NotificationDialogController);

    NotificationDialogController.$inject = ['$timeout', '$scope', '$stateParams', 'entity', 'Notification','$state'];

    function NotificationDialogController ($timeout, $scope, $stateParams, entity, Notification,$state) {
        var vm = this;

        vm.notification = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $state.go('notification',{},{reload:true});
        }

        function save () {
            vm.isSaving = true;
            if (vm.notification.id !== null) {
                Notification.update(vm.notification, onSaveSuccess, onSaveError);
            } else {
                Notification.save(vm.notification, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('projectApp:notificationUpdate', result);
            clear();
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.occurDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
