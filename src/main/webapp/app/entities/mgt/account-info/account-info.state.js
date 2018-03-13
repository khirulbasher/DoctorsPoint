(function() {
    'use strict';

    angular
        .module('projectApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('account-info', {
            parent: 'entity',
            url: '/account-info?page&sort&search',
            data: {
                authorities: ['ROLE_ADMIN','ROLE_MGT','ROLE_TRANSACTION'],
                pageTitle: 'projectApp.accountInfo.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/mgt/account-info/account-infos.html',
                    controller: 'AccountInfoController',
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
                    $translatePartialLoader.addPart('accountInfo');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('account-info-detail', {
            parent: 'account-info',
            url: '/account-info/{id}',
            data: {
                authorities: ['ROLE_ADMIN','ROLE_MGT','ROLE_TRANSACTION'],
                pageTitle: 'projectApp.accountInfo.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/mgt/account-info/account-info-detail.html',
                    controller: 'AccountInfoDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('accountInfo');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'AccountInfo', function($stateParams, AccountInfo) {
                    return AccountInfo.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'account-info',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('account-info-detail.edit', {
            parent: 'account-info-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_ADMIN','ROLE_MGT','ROLE_TRANSACTION']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/mgt/account-info/account-info-dialog.html',
                    controller: 'AccountInfoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['AccountInfo', function(AccountInfo) {
                            return AccountInfo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('account-info.new', {
            parent: 'account-info',
            url: '/new',
            data: {
                authorities: ['ROLE_ADMIN','ROLE_MGT','ROLE_TRANSACTION']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/mgt/account-info/account-info-dialog.html',
                    controller: 'AccountInfoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                firstName: null,
                                lastName: null,
                                email: null,
                                active: false,
                                latitude: null,
                                longitude: null,
                                lastModifiedBy: null,
                                lastModifyDate: null,
                                birthDate: null,
                                userId: null,
                                village: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('account-info', null, { reload: 'account-info' });
                }, function() {
                    $state.go('account-info');
                });
            }]
        })
        .state('account-info.edit', {
            parent: 'account-info',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_ADMIN','ROLE_MGT','ROLE_TRANSACTION']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/mgt/account-info/account-info-dialog.html',
                    controller: 'AccountInfoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['AccountInfo', function(AccountInfo) {
                            return AccountInfo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('account-info', null, { reload: 'account-info' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('account-info.delete', {
            parent: 'account-info',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_ADMIN','ROLE_MGT','ROLE_TRANSACTION']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/mgt/account-info/account-info-delete-dialog.html',
                    controller: 'AccountInfoDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['AccountInfo', function(AccountInfo) {
                            return AccountInfo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('account-info', null, { reload: 'account-info' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
