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
})();
