(function() {
    'use strict';

    angular
        .module('projectApp')
        .controller('NotificationDetailController', NotificationDetailController);

    NotificationDetailController.$inject = ['entity'];

    function NotificationDetailController(entity) {
        var vm = this;

        vm.notification = entity;
    }
})();
