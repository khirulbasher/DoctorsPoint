(function() {
    'use strict';

    angular
        .module('projectApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('thana', {
            parent: 'entity',
            url: '/thana?page&sort&search',
            data: {
                authorities: ['ROLE_ADMIN','ROLE_MGT','ROLE_TERRITORY'],
                pageTitle: 'projectApp.thana.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/territory/thana/thanas.html',
                    controller: 'ThanaController',
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
                    $translatePartialLoader.addPart('thana');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('thana-detail', {
            parent: 'thana',
            url: '/thana/{id}',
            data: {
                authorities: ['ROLE_ADMIN','ROLE_MGT','ROLE_TERRITORY'],
                pageTitle: 'projectApp.thana.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/territory/thana/thana-detail.html',
                    controller: 'ThanaDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('thana');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Thana', function($stateParams, Thana) {
                    return Thana.get({id : $stateParams.id}).$promise;
                }]
            }
        }).state('thana.new', {
            parent: 'thana',
            url: '/new',
            data: {
                authorities: ['ROLE_ADMIN','ROLE_MGT','ROLE_TERRITORY']
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/territory/thana/thana-dialog.html',
                    controller: 'ThanaDialogController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('thana');
                    return $translate.refresh();
                }],
                entity: function () {
                    return null;
                }
            }
        })
        .state('thana.edit', {
            parent: 'thana',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_ADMIN','ROLE_MGT','ROLE_TERRITORY']
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/territory/thana/thana-dialog.html',
                    controller: 'ThanaDialogController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('thana');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Thana', function($stateParams, Thana) {
                    return Thana.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('thana.delete', {
            parent: 'thana',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_ADMIN','ROLE_MGT','ROLE_TERRITORY']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/entity-global-dialog.html',
                    controller: 'EntityGlobalController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        obj: ['$stateParams','Thana','$rootScope', function($stateParams, District,$rootScope) {
                            return {
                                title:'Thana Delete Operation',
                                callback: function() {
                                    Thana.delete({id:$stateParams.id},function () {
                                        $rootScope.$broadcast('thana','loadAll');
                                    });
                                }
                            }
                        }]
                    }
                }).result.then(function() {
                    $state.go('district', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
