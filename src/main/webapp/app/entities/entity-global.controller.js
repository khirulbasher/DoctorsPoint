(function() {
    'use strict';
    angular
        .module('projectApp')
        .controller('EntityGlobalController',EntityGlobalController);

    EntityGlobalController.$inject = ['$uibModalInstance','obj']

    function EntityGlobalController($uibModalInstance,obj) {
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
        }
    }
})();
