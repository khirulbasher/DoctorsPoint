(function() {
    'use strict';

    angular
        .module('projectApp')
        .controller('UserManagementDialogController',UserManagementDialogController);

    UserManagementDialogController.$inject = ['$state', 'entity', 'User', 'JhiLanguageService','UserService'];

    function UserManagementDialogController ($state, entity, User, JhiLanguageService,UserService) {
        var vm = this;

        //vm.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        UserService.getByMapping('authority',function (results) {
            vm.authorities = results;
        });
        vm.languages = null;
        vm.save = save;
        vm.user = entity;


        JhiLanguageService.getAll().then(function (languages) {
            vm.languages = languages;
        });

        function onSaveSuccess (result) {
            vm.isSaving = false;
            $state.go('user-management',{},{reload:false});
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        function save () {
            vm.isSaving = true;
            if(vm.user.active == null) vm.user.active = true;
            if (vm.user.id != null) {
                User.update(vm.user, onSaveSuccess, onSaveError);
            } else {
                User.save(vm.user, onSaveSuccess, onSaveError);
            }
        }
    }
})();
