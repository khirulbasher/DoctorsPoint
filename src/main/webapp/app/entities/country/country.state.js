(function() {
    'use strict';

    angular
        .module('projectApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('country', {
            parent: 'entity',
            url: '/country?page&sort&search',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'projectApp.country.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/country/countries.html',
                    controller: 'CountryController',
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
                    $translatePartialLoader.addPart('country');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('country-detail', {
            parent: 'country',
            url: '/detail/{id}',
            data: {
                authorities: ['ROLE_ADMIN','ROLE_MGT'],
                pageTitle: 'projectApp.country.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/country/country-detail.html',
                    controller: 'CountryDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('country');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Country', function($stateParams, Country) {
                    return Country.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('country.new', {
            parent: 'country',
            url: '/new',
            data: {
                authorities: ['ROLE_ADMIN','ROLE_MGT']
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/country/country-dialog.html',
                    controller: 'CountryDialogController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('country');
                    return $translate.refresh();
                }],
                entity: function () {
                    return null;
                }
            }
        })
        .state('country.edit', {
            parent: 'country',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_ADMIN','ROLE_MGT']
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/country/country-dialog.html',
                    controller: 'CountryDialogController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('country');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Country', function($stateParams, Country) {
                    return Country.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('country.delete', {
            parent: 'country',
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
                        obj: ['$stateParams','Country', function($stateParams, Country) {
                            return {
                                title:'Country Delete Operation',
                                callback: function() {
                                    Country.delete({id:$stateParams.id});
                                },
                                parent: 'country'
                            }
                        }]
                    }
                }).result.then(function() {
                    $state.go('country', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }
})();
