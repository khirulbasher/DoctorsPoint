(function() {
    'use strict';

    angular
        .module('projectApp')
        .controller('PostDetailController', PostDetailController);

    PostDetailController.$inject = ['entity'];

    function PostDetailController(entity) {
        var vm = this;

        vm.post = entity;
    }
})();
