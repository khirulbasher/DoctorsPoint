(function() {
    'use strict';

    angular
        .module('projectApp')
        .controller('ThanaDeleteController',ThanaDeleteController);

    ThanaDeleteController.$inject = ['$uibModalInstance', 'entity', 'Thana'];

    function ThanaDeleteController($uibModalInstance, entity, Thana) {
        var vm = this;

        vm.thana = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Thana.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
