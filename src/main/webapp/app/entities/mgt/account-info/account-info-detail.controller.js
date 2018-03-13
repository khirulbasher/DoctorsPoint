(function() {
    'use strict';

    angular
        .module('projectApp')
        .controller('AccountInfoDetailController', AccountInfoDetailController);

    AccountInfoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'AccountInfo', 'Thana', 'Post'];

    function AccountInfoDetailController($scope, $rootScope, $stateParams, previousState, entity, AccountInfo, Thana, Post) {
        var vm = this;

        vm.accountInfo = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('projectApp:accountInfoUpdate', function(event, result) {
            vm.accountInfo = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
