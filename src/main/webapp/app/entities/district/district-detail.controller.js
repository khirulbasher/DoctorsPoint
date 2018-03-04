(function() {
    'use strict';

    angular
        .module('projectApp')
        .controller('DistrictDetailController', DistrictDetailController);

    DistrictDetailController.$inject = ['entity'];

    function DistrictDetailController(entity) {
        var vm = this;
        vm.district = entity;
    }
})();
