(function() {
    'use strict';

    angular
        .module('ninjaskinsApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('help', {
            parent: 'app',
            url: '/help',
            data: {
                authorities: [],
                pageTitle: 'help.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/pages/help/help.html',
                    controller: 'HelpController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('help');
                    return $translate.refresh();
                }]
            }
        });
    }
})();
