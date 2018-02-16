(function() {
    'use strict';
    angular
        .module('projectApp')
        .controller('EntityGlobalController',EntityGlobalController);

    EntityGlobalController.$inject = ['$uibModalInstance','obj','$rootScope']

    function EntityGlobalController($uibModalInstance,obj,$rootScope) {
        var vm = this;

        vm.clear = clear;
        vm.callback = callback;
        vm.obj=obj;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function callback () {
            vm.obj.callback();
            clear();
            $rootScope.$broadcast(vm.obj.parent,'loadAll');
        }
    }
})();
