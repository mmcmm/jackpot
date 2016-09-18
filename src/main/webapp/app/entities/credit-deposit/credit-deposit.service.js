(function() {
    'use strict';
    angular
        .module('ninjaskinsApp')
        .factory('CreditDeposit', CreditDeposit);

    CreditDeposit.$inject = ['$resource'];

    function CreditDeposit ($resource) {
        var resourceUrl =  'api/credit-deposit';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'POST' }
        });
    }
})();
