(function() {
    'use strict';

    angular
        .module('projectApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('cash', {
            parent: 'entity',
            url: '/cash?page&sort&search',
            data: {
                authorities: ['ROLE_ADMIN','ROLE_MGT','ROLE_TRANSACTION'],
                pageTitle: 'projectApp.cash.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/mgt/cash/cash.html',
                    controller: 'CashController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('cash');
                    $translatePartialLoader.addPart('transactionType');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('cash-detail', {
            parent: 'cash',
            url: '/cash/{id}',
            data: {
                authorities: ['ROLE_ADMIN','ROLE_MGT','ROLE_TRANSACTION'],
                pageTitle: 'projectApp.cash.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/mgt/cash/cash-detail.html',
                    controller: 'CashDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('cash');
                    $translatePartialLoader.addPart('transactionType');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Cash', function($stateParams, Cash) {
                    return Cash.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'cash',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('cash-detail.edit', {
            parent: 'cash-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_ADMIN','ROLE_MGT','ROLE_TRANSACTION']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/mgt/cash/cash-dialog.html',
                    controller: 'CashDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Cash', function(Cash) {
                            return Cash.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('cash.new', {
            parent: 'cash',
            url: '/new',
            data: {
                authorities: ['ROLE_ADMIN','ROLE_MGT','ROLE_TRANSACTION']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/mgt/cash/cash-dialog.html',
                    controller: 'CashDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                cash: null,
                                lastTransactionDate: null,
                                transactionType: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('cash', null, { reload: 'cash' });
                }, function() {
                    $state.go('cash');
                });
            }]
        })
        .state('cash.edit', {
            parent: 'cash',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_ADMIN','ROLE_MGT','ROLE_TRANSACTION']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/mgt/cash/cash-dialog.html',
                    controller: 'CashDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Cash', function(Cash) {
                            return Cash.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('cash', null, { reload: 'cash' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('cash.delete', {
            parent: 'cash',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_ADMIN','ROLE_MGT','ROLE_TRANSACTION']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/mgt/cash/cash-delete-dialog.html',
                    controller: 'CashDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Cash', function(Cash) {
                            return Cash.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('cash', null, { reload: 'cash' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
