(function() {
    'use strict';
    angular
        .module('ninjaskinsApp')
        .factory('JackpotDeposit', JackpotDeposit);

    JackpotDeposit.$inject = ['$resource'];

    function JackpotDeposit ($resource) {
        var resourceUrl =  'api/jackpot-deposit';

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
