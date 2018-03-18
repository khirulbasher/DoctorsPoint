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
                }]
            }
        })
        .state('cash.new', {
            parent: 'cash',
            url: '/new',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/mgt/cash/cash-dialog.html',
                    controller: 'CashDialogController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('cash');
                    $translatePartialLoader.addPart('transactionType');
                    return $translate.refresh();
                }],

                entity: function () {
                    return null;
                }
            }
        })
        .state('cash.edit', {
            parent: 'cash',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/mgt/cash/cash-dialog.html',
                    controller: 'CashDialogController',
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
                }]
            }
        })
        .state('cash.delete', {
            parent: 'cash',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/entity-global-dialog.html',
                    controller: 'EntityGlobalController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        obj: ['$stateParams','Cash','$rootScope', function($stateParams, Cash,$rootScope) {
                            return {
                                title:'Cash Delete Operation',
                                callback: function() {
                                    Cash.delete({id:$stateParams.id},function () {
                                        $rootScope.$broadcast('cash','loadAll');
                                    });
                                }
                            }
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
