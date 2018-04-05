(function() {
    'use strict';

    angular
        .module('projectApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('transaction', {
            parent: 'entity',
            url: '/transaction?page&sort&search',
            data: {
                authorities: ['ROLE_ADMIN','ROLE_MGT','ROLE_TRANSACTION'],
                pageTitle: 'projectApp.transaction.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/mgt/transaction/transactions.html',
                    controller: 'TransactionController',
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
                    $translatePartialLoader.addPart('transaction');
                    $translatePartialLoader.addPart('transactionType');
                    $translatePartialLoader.addPart('approvalStatus');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('transaction-detail', {
            parent: 'transaction',
            url: '/transaction/{id}',
            data: {
                authorities: ['ROLE_ADMIN','ROLE_MGT','ROLE_TRANSACTION'],
                pageTitle: 'projectApp.transaction.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/mgt/transaction/transaction-detail.html',
                    controller: 'TransactionDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('transaction');
                    $translatePartialLoader.addPart('transactionType');
                    $translatePartialLoader.addPart('approvalStatus');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Transaction', function($stateParams, Transaction) {
                    return Transaction.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('transaction.new', {
            parent: 'transaction',
            url: '/new',
            data: {
                authorities: ['ROLE_ADMIN','ROLE_MGT','ROLE_TRANSACTION']
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/mgt/transaction/transaction-dialog.html',
                    controller: 'TransactionDialogController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('transaction');
                    $translatePartialLoader.addPart('transactionType');
                    $translatePartialLoader.addPart('approvalStatus');
                    return $translate.refresh();
                }],
                entity: function() {
                    return null;
                }
            }
        })
        .state('transaction.edit', {
            parent: 'transaction',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_ADMIN','ROLE_MGT','ROLE_TRANSACTION']
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/mgt/transaction/transaction-dialog.html',
                    controller: 'TransactionDialogController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('transaction');
                    $translatePartialLoader.addPart('transactionType');
                    $translatePartialLoader.addPart('approvalStatus');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Transaction', function($stateParams, Transaction) {
                    return Transaction.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('transaction.delete', {
            parent: 'transaction',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_ADMIN','ROLE_MGT','ROLE_TRANSACTION']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/entity-global-dialog.html',
                    controller: 'EntityGlobalController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        obj: ['$stateParams','Transaction','$rootScope', function($stateParams, Transaction,$rootScope) {
                            return {
                                title:'Transaction Delete Operation',
                                callback: function() {
                                    Transaction.delete({id:$stateParams.id},function () {
                                        $rootScope.$broadcast('transaction','loadAll');
                                    });
                                }
                            }
                        }]
                    }
                }).result.then(function() {
                    $state.go('transaction', null, { reload: 'transaction' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
