(function() {
    'use strict';

    angular
        .module('projectApp')
        .controller('DistrictDetailController', DistrictDetailController);

    DistrictDetailController.$inject = ['$scope', '$rootScope', '$state', 'entity'];

    function DistrictDetailController($scope, $rootScope, $state, entity) {
        var vm = this;
        vm.district = entity;
        $scope.clear = function () {
            $state.go('district',{},{reload:false});
        }
    }
})();
