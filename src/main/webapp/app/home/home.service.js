(function() {
    'use strict';
    angular
        .module('ninjaskinsApp')
        .factory('JackpotDeposit', JackpotDeposit);

    JackpotDeposit.$inject = ['$resource'];

    function JackpotDeposit ($resource) {
        var resourceUrl =  'api/jackpot-deposit';

        return $resource(resourceUrl, {}, {
            'update': { method:'POST' }
        });
    }


    angular
        .module('ninjaskinsApp')
        .factory('AllJackpotDeposit', AllJackpotDeposit);

    AllJackpotDeposit.$inject = ['$resource'];

    function AllJackpotDeposit ($resource) {
        var resourceUrl =  'api/jackpot-deposits';

        return $resource(resourceUrl, {}, {
            'get': {
                method: 'GET',
                isArray: true,
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            }
        });
    }
})();
