(function() {
    'use strict';

    angular
        .module('ninjaskinsApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('home', {
            parent: 'app',
            url: '/',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: 'app/home/home.html',
                    controller: 'HomeController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                mainTranslatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
                    $translatePartialLoader.addPart('home');
                    return $translate.refresh();
                }]
            },
            onEnter: ['HomeSocketService', function(HomeSocketService) {
                HomeSocketService.connect();
                HomeSocketService.subscribe();
            }],
            onExit: ['HomeSocketService', function(HomeSocketService) {
                HomeSocketService.unsubscribe();
                HomeSocketService.disconnect();
            }]
        });
    }
})();
