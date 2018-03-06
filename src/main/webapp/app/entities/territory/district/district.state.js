(function() {
    'use strict';

    angular
        .module('projectApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('district', {
            parent: 'entity',
            url: '/district?page&sort&search',
            data: {
                authorities: ['ROLE_ADMIN','ROLE_MGT'],
                pageTitle: 'projectApp.district.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/territory/district/districts.html',
                    controller: 'DistrictController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'name,asc',
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
                    $translatePartialLoader.addPart('district');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('district-detail', {
            parent: 'district',
            url: '/district/{id}',
            data: {
                authorities: ['ROLE_ADMIN','ROLE_MGT'],
                pageTitle: 'projectApp.district.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/territory/district/district-detail.html',
                    controller: 'DistrictDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('district');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'District', function($stateParams, District) {
                    return District.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('district.new', {
            parent: 'district',
            url: '/new',
            data: {
                authorities: ['ROLE_ADMIN','ROLE_MGT']
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/territory/district/district-dialog.html',
                    controller: 'DistrictDialogController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('district');
                    return $translate.refresh();
                }],
                entity: function () {
                    return null;
                }
            }
        })
        .state('district.edit', {
            parent: 'district',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_ADMIN','ROLE_MGT']
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/territory/district/district-dialog.html',
                    controller: 'DistrictDialogController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('district');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'District', function($stateParams, District) {
                    return District.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('district.delete', {
            parent: 'district',
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
                        obj: ['$stateParams','District','$rootScope', function($stateParams, District,$rootScope) {
                            return {
                                title:'District Delete Operation',
                                callback: function() {
                                    District.delete({id:$stateParams.id},function () {
                                        $rootScope.$broadcast('district','loadAll');
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
