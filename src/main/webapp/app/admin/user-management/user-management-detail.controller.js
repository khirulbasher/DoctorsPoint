(function() {
    'use strict';

    angular
        .module('projectApp')
        .controller('UserManagementDetailController', UserManagementDetailController);

    UserManagementDetailController.$inject = ['entity'];

    function UserManagementDetailController(entity) {
        var vm = this;
        vm.user = entity;
    }
})();
