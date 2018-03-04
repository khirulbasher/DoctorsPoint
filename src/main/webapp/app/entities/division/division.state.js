(function() {
    'use strict';

    angular
        .module('projectApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('division', {
            parent: 'entity',
            url: '/division?page&sort&search',
            data: {
                authorities: ['ROLE_ADMIN','ROLE_MGT'],
                pageTitle: 'projectApp.division.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/division/divisions.html',
                    controller: 'DivisionController',
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
                    $translatePartialLoader.addPart('division');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('division-detail', {
            parent: 'division',
            url: '/division/{id}',
            data: {
                authorities: ['ROLE_ADMIN','ROLE_MGT'],
                pageTitle: 'projectApp.division.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/division/division-detail.html',
                    controller: 'DivisionDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('division');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Division', function($stateParams, Division) {
                    return Division.get({id : $stateParams.id}).$promise;
                }]
            }
        }).state('division.new', {
            parent: 'division',
            url: '/new',
            data: {
                authorities: ['ROLE_ADMIN','ROLE_MGT']
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/division/division-dialog.html',
                    controller: 'DivisionDialogController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('division');
                    return $translate.refresh();
                }],
                entity: function () {
                    return null;
                }
            }
        })
        .state('division.edit', {
            parent: 'division',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_ADMIN','ROLE_MGT']
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/division/division-dialog.html',
                    controller: 'DivisionDialogController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('division');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Division', function($stateParams, Division) {
                    return Division.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('division.delete', {
            parent: 'division',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_ADMIN','ROLE_MGT']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/entity-global-dialog.html',
                    controller: 'EntityGlobalController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        obj: ['$stateParams','Division','$rootScope', function($stateParams, Division,$rootScope) {
                            return {
                                title:'Division Delete Operation',
                                callback: function() {
                                    Division.delete({id:$stateParams.id},function () {
                                        $rootScope.$broadcast('division','loadAll');
                                    });
                                }
                            }
                        }]
                    }
                }).result.then(function() {
                    $state.go('division', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
